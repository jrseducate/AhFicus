package com.jrseducate.ahficus.items;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.item.ItemFood;

abstract public class AhFicusItemFood extends ItemFood
{
    public static final String RegistryName = null;

    public AhFicusItemFood(String RegistryName, int amount, float saturation, boolean isWolfFood)
    {
        super(amount, saturation, isWolfFood);

        registerItem(RegistryName);
    }
    
    public AhFicusItemFood(String RegistryName, int amount, boolean isWolfFood)
    {
        super(amount, isWolfFood);
        
        registerItem(RegistryName);
    }
    
    public void registerItem(String RegistryName)
    {
        AhFicus.ItemManager.registerItem(this, RegistryName);
    }
}
