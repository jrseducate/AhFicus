package com.jrseducate.ahficus.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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

    // TODO: Change this to looking up from a configuration file
    public int getItemColor(String registryName, ItemStack stack, int tintIndex)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        
        switch(registryName)
        {
            case ItemWandFocus.RegistryName:
            case ItemBoundWand.RegistryName:
                if(stack.hasTagCompound() && nbt.hasKey("focus_type"))
                {
                    switch(nbt.getString("focus_type"))
                    {
                        case "levitation":
                            return 0xFFFF00FF;
                    }
                }
        }
        
        return -1;
    }

    // TODO: Change to represent all additionalText on items
    public String getFocusTypeName(String focusType)
    {
        switch(focusType)
        {
            case "levitation":
                return "Levitation";
        }
        
        return null;
    }
}
