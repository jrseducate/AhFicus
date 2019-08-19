package com.jrseducate.ahficus.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ItemCustomRendering
{
    public void customRender(EntityPlayer player, ItemStack itemStack);
}
