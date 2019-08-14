package com.jrseducate.ahficus.items;

import net.minecraft.creativetab.CreativeTabs;

public class ItemWand extends AhFicusItem
{
    public static final String RegistryName = "wand";
    
    public ItemWand()
    {
        super(RegistryName);
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(64);
    }
}
