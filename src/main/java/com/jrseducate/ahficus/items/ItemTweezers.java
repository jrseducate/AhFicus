package com.jrseducate.ahficus.items;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemTweezers extends AhFicusItem implements ItemPreventDefaultRightClick
{
    public static final String RegistryName = "tweezers";
    
    public ItemTweezers()
    {
        super(RegistryName);
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
        setMaxDamage(7);
    }
    
    protected void pluckPlayerHair(ItemStack itemStack, EntityPlayer player, EntityPlayer target)
    {
        ItemStack strandOfHair = new ItemStack(AhFicus.ItemManager.getItem(ItemStrandOfHair.class));
        
        NBTTagCompound nbt = strandOfHair.hasTagCompound() ? strandOfHair.getTagCompound() : new NBTTagCompound();
        
        nbt.setString("player", target.getName());
        nbt.setString("player_display", target.getDisplayNameString());
        
        strandOfHair.setTagCompound(nbt);
        
        player.addItemStackToInventory(strandOfHair);
        
        itemStack.damageItem(1, player);
    }
    
    protected void pluckEntityHair(ItemStack itemStack, EntityPlayer player, Entity target)
    {
        ItemStack strandOfHair = new ItemStack(AhFicus.ItemManager.getItem(ItemStrandOfHair.class));
        
        NBTTagCompound nbt = strandOfHair.hasTagCompound() ? strandOfHair.getTagCompound() : new NBTTagCompound();
        
        if(target.hasCustomName())
        {
            nbt.setString("entity_custom", target.getCustomNameTag());
        }
        else
        {
            nbt.setString("entity", target.getName());
        }
        
        nbt.setString("entity_display", target.getDisplayName().getUnformattedText());
        
        strandOfHair.setTagCompound(nbt);
        
        player.addItemStackToInventory(strandOfHair);
        
        itemStack.damageItem(1, player);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack heldItemStack = handIn == EnumHand.MAIN_HAND ? playerIn.getHeldItemMainhand() : playerIn.getHeldItemOffhand();
        
        if(AhFicus.isServer(playerIn.world))
        {
            EntityPlayer target = playerIn;
            
            pluckPlayerHair(heldItemStack, playerIn, target);
        }
        
        return ActionResult.newResult(EnumActionResult.SUCCESS, heldItemStack);
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if(AhFicus.isServer(player.world))
        {
            if(entity instanceof EntityPlayer)
            {
                EntityPlayer target = (EntityPlayer) entity;
                
                pluckPlayerHair(stack, player, target);
            }
            else
            {
                pluckEntityHair(stack, player, entity);
            }
        }
        
        return true;
    }
}
