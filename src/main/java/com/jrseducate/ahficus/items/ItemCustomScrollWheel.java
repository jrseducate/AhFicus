package com.jrseducate.ahficus.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ItemCustomScrollWheel
{
    public boolean onScrollWheelValid(EntityPlayer player, ItemStack stack, NBTTagCompound nbt, int scrollDir);
    
    public void onScrollWheel(EntityPlayer player, ItemStack stack, NBTTagCompound nbt, int scrollDir);
}
