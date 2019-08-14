package com.jrseducate.ahficus.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.jrseducate.ahficus.reference.Reference;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class AhFicusItemManager
{
    public static Map<Class<?>, Item> Items;
    
    public AhFicusItemManager()
    {
        Items = new HashMap<Class<?>, Item>();
    }
    
    public void init()
    {
        initItems();
    }
    
    public void initItems()
    {
        addItem(ItemFoodEnderCookie.class);
        addItem(ItemStrandOfHair.class);
        addItem(ItemTweezers.class);
        addItem(ItemWand.class);
        addItem(ItemBoundWand.class);
        addItem(ItemWandFocus.class);
        addItem(ItemRitualStone.class);
    }
    
    public void addItem(Class<?> itemClass)
    {
        try
        {
            Items.put(itemClass, (Item) itemClass.newInstance());
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public Item getItem(Class<?> itemClass)
    {
        return Items.get(itemClass);
    }
    
    public Item[] getItems()
    {
        return Items.values().toArray(new Item[0]);
    }

    public Item[] getColoredItems()
    {
        List<Item> items = new ArrayList<Item>();
        
        for(Item item : Items.values())
        {
            if(item instanceof IItemColor)
            {
                items.add(item);
            }
        }
        
        return items.toArray(new Item[0]);
    }
    
    public void registerItem(Item item, String RegistryName)
    {
        item.setRegistryName(Reference.MOD_ID, RegistryName);
        final ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName());
        item.setUnlocalizedName(registryName.toString());
    }
}
