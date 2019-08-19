package com.jrseducate.ahficus.items.helpers;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AhFicusItemHelper
{
    public String helperTypeName;
    
    public AhFicusItemHelper(String helperTypeName)
    {
        this.helperTypeName = helperTypeName;
    }
    
    abstract public void onItemRightClick(EntityPlayer player, ItemStack stack, NBTTagCompound nbt);
    
    abstract public void onLeftClickEntity(EntityPlayer player, Entity entity, ItemStack stack, NBTTagCompound nbt);
    
    abstract public void onUpdate(Entity entity, ItemStack stack, NBTTagCompound nbt, boolean isSelected);
    
    abstract public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, ItemStack stack, NBTTagCompound nbt,
            EnumFacing facing, float hitX, float hitY, float hitZ);

    abstract public String getKey();
    
    public int getItemColor(ItemStack stack, int tintIndex)
    {
        return -1;
    }
    
    public int getItemColor(ItemStack stack, int tintIndex, boolean isActive)
    {
        return -1;
    }
    
    public void addItemInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        
    }
    
    public void customRender(EntityPlayer player, ItemStack stack, NBTTagCompound nbt)
    {
        
    }
}
