package com.jrseducate.ahficus.items;

import java.util.Objects;

import com.jrseducate.ahficus.reference.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemWand extends Item
{
    public static final String RegistryName = "wand";
    
    public ItemWand()
    {
        super();
        
        setRegistryName(Reference.MOD_ID, RegistryName);
        final ResourceLocation registryName = Objects.requireNonNull(getRegistryName());
        setUnlocalizedName(registryName.toString());
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(64);
    }
}
