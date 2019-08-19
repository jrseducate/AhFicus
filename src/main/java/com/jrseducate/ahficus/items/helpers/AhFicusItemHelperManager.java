package com.jrseducate.ahficus.items.helpers;

import java.util.HashMap;

import com.jrseducate.ahficus.items.helpers.wandfocus.ItemHelperWandFocusChronos;
import com.jrseducate.ahficus.items.helpers.wandfocus.ItemHelperWandFocusFinder;
import com.jrseducate.ahficus.items.helpers.wandfocus.ItemHelperWandFocusFire;
import com.jrseducate.ahficus.items.helpers.wandfocus.ItemHelperWandFocusLevitation;

public class AhFicusItemHelperManager
{
    HashMap<String, AhFicusItemHelper> itemHelpers;
    
    public AhFicusItemHelperManager()
    {
        itemHelpers = new HashMap<String, AhFicusItemHelper>();
    }
    
    public void init()
    {
        addItemHelper(new ItemHelperWandFocusLevitation());
        addItemHelper(new ItemHelperWandFocusChronos());
        addItemHelper(new ItemHelperWandFocusFire());
        addItemHelper(new ItemHelperWandFocusFinder());
    }
    
    public void addItemHelper(AhFicusItemHelper itemHelper)
    {
        itemHelpers.put(itemHelper.getKey(), itemHelper);
    }
    
    public AhFicusItemHelper getItemHelper(String key)
    {
        return itemHelpers.get(key);
    }
}
