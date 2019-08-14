package com.jrseducate.ahficus.items;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.item.Item;

abstract public class AhFicusItem extends Item
{
    public static final String RegistryName = null;
    
    public AhFicusItem(String RegistryName)
    {
        super();
        
        registerItem(RegistryName);
    }
    
    public void registerItem(String RegistryName)
    {
        AhFicus.ItemManager.registerItem(this, RegistryName);
    }
}
