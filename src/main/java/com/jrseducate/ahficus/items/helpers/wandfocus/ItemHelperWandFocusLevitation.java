package com.jrseducate.ahficus.items.helpers.wandfocus;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemHelperWandFocusLevitation extends ItemHelperWandFocus
{
    public ItemHelperWandFocusLevitation()
    {
        super("levitation", "Levitation", 2500, 0x00FF00FF);
    }
    
    public void clearNBT(ItemStack stack, NBTTagCompound nbt)
    {
        nbt.removeTag("target");
        nbt.removeTag("target_uuid");
    }

    @Override
    public void onItemRightClick(EntityPlayer player, ItemStack stack, NBTTagCompound nbt)
    {
        if(nbt.hasKey("target"))
        {
            clearNBT(stack, nbt);
            damageItem(stack, player);
        }
        
        stack.setTagCompound(nbt);
    }

    @Override
    public void onLeftClickEntity(EntityPlayer player, Entity entity, ItemStack stack, NBTTagCompound nbt)
    {
        if(!nbt.hasKey("target"))
        {
            nbt.setInteger("target", entity.getEntityId());
            nbt.setString("target_uuid", entity.getCachedUniqueIdString());
        }
        else
        {                        
            int entityId = nbt.getInteger("target");

            clearNBT(stack, nbt);
            
            Entity target = player.world.getEntityByID(entityId);
            
            if(target != null)
            {
                Vec3d velocity = player.getLookVec().scale(3);
                target.addVelocity(velocity.x, velocity.y, velocity.z);
            }
            
            damageItem(stack, player);
        }
        
        stack.setTagCompound(nbt);
    }

    @Override
    public void onUpdate(Entity entity, ItemStack stack, NBTTagCompound nbt, boolean isSelected)
    {
        if(nbt.hasKey("target") && AhFicus.isServer(entity.world))
        {
            int entityId = nbt.getInteger("target");
            String targetUUID = nbt.getString("target_uuid");
            
            Entity target = entity.world.getEntityByID(entityId);
            String actualTargetUUID = target != null ? target.getCachedUniqueIdString() : null;
            
            if(target != null && targetUUID != null && actualTargetUUID != null && targetUUID.equals(actualTargetUUID) && isSelected)
            {
                Vec3d eyePos = new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
                Vec3d targetPos = eyePos.subtract(0, 0.5f, 0).add(entity.getLookVec().scale(3));
                
                target.setVelocity(0, 0, 0);
                target.setPosition(targetPos.x, targetPos.y, targetPos.z);
            }
            else
            {
                clearNBT(stack, nbt);
                damageItem(stack, entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null);
            }
        }
        
        stack.setTagCompound(nbt);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, ItemStack stack, NBTTagCompound nbt, EnumFacing facing,
            float hitX, float hitY, float hitZ)
    {
        return EnumActionResult.PASS;
    }
    
    @Override
    public int getItemColor(ItemStack stack, int tintIndex)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        
        return super.getItemColor(stack, tintIndex, stack.hasTagCompound() && nbt.hasKey("target") && nbt.hasKey("target_uuid"));
    }

}
