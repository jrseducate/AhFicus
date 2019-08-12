package com.jrseducate.ahficus.items;

import java.util.List;
import java.util.Objects;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.reference.Reference;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

// TODO: Add Helpers for different focus_type(s)
public class ItemBoundWand extends Item implements IItemColor
{
    public static final String RegistryName = "bound_wand";
    
    public ItemBoundWand()
    {
        super();
        
        setRegistryName(Reference.MOD_ID, RegistryName);
        final ResourceLocation registryName = Objects.requireNonNull(getRegistryName());
        setUnlocalizedName(registryName.toString());
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
        // TODO: See about having conditional Max Damage based on the focus_type
        setMaxDamage(3);
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        if(stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            
            if(nbt.hasKey("focus_type"))
            {
                tooltip.add(TextFormatting.GREEN + AhFicus.ItemManager.getFocusTypeName(nbt.getString("focus_type")));
                return;
            }
        }

        tooltip.add(TextFormatting.RED + "Broken Bound Wand :(");
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        
        if(stack.hasTagCompound())
        {
            switch(nbt.getString("focus_type"))
            {
                case "levitation":
                    
                    if(!nbt.hasKey("target"))
                    {
                        nbt.setInteger("target", entity.getEntityId());
                        nbt.setString("target_uuid", entity.getCachedUniqueIdString());
                    }
                    else
                    {                        
                        int entityId = nbt.getInteger("target");
                        
                        nbt.removeTag("target");
                        nbt.removeTag("target_uuid");
                        
                        Entity target = player.world.getEntityByID(entityId);
                        
                        if(target != null)
                        {
                            Vec3d velocity = player.getLookVec().scale(3);
                            target.addVelocity(velocity.x, velocity.y, velocity.z);
                        }
                        
                        stack.damageItem(1, player);
                    }
                    
                    break;
            }
        }
        
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        NBTTagCompound nbt = itemStack.getTagCompound();
        
        if(itemStack.hasTagCompound())
        {
            switch(nbt.getString("focus_type"))
            {
                case "levitation":
                    
                    if(nbt.hasKey("target"))
                    {
                        nbt.removeTag("target");
                        nbt.removeTag("target_uuid");
                        
                        itemStack.damageItem(1, playerIn);
                    }
                    
                    break;
            }
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {        
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        NBTTagCompound nbt = stack.getTagCompound();
        
        if(stack.hasTagCompound())
        {
            switch(nbt.getString("focus_type"))
            {
                case "levitation":
                    
                    if(nbt.hasKey("target"))
                    {
                        int entityId = nbt.getInteger("target");
                        String targetUUID = nbt.getString("target_uuid");
                        
                        Entity target = worldIn.getEntityByID(entityId);
                        String actualTargetUUID = target != null ? target.getCachedUniqueIdString() : null;
                        
                        if(target != null && targetUUID != null && actualTargetUUID != null && targetUUID.equals(actualTargetUUID) && isSelected)
                        {
                            Vec3d eyePos = new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ);
                            Vec3d targetPos = eyePos.subtract(0, 0.5f, 0).add(entityIn.getLookVec().scale(3));
                            
                            target.setVelocity(0, 0, 0);
                            target.setPosition(targetPos.x, targetPos.y, targetPos.z);
                        }
                        else
                        {
                            nbt.removeTag("target");
                            nbt.removeTag("target_uuid");
                            stack.damageItem(1, entityIn instanceof EntityLivingBase ? (EntityLivingBase)entityIn : null);
                        }
                    }
                    
                    break;
            }
        }
    }
    
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex)
    {
        switch(tintIndex)
        {
            case 1:
                return AhFicus.ItemManager.getItemColor(RegistryName, stack, 0);
        }
        
        return -1;
    }
}
