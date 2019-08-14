package com.jrseducate.ahficus.items.helpers.wandfocus;

import java.util.List;

import com.jrseducate.ahficus.items.ItemBoundWand;
import com.jrseducate.ahficus.items.helpers.AhFicusItemHelper;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public abstract class ItemHelperWandFocus extends AhFicusItemHelper
{
    String focusType;
    String focusDisplayName;
    int damage;
    int color;
    
    public ItemHelperWandFocus(String focusType, String focusDisplayName, int damage, int color)
    {
        super("wand_focus");
        
        this.focusType = focusType;
        this.focusDisplayName = focusDisplayName;
        this.damage = damage;
        this.color = color;
    }
    
    public void damageItem(ItemStack stack, EntityLivingBase entity)
    {
        stack.damageItem(damage, entity);
    }

    @Override
    public String getKey()
    {
        return helperTypeName + ":" + focusType;
    }
    
    public int getItemColor(ItemStack stack, int tintIndex, boolean isActive)
    {
        Item item = stack.getItem();
        
        if(item instanceof ItemBoundWand && tintIndex == 0)
        {
            return -1;
        }
        
        int color = this.color;
        
        if(!isActive)
        {
            color = color & 0x00AAAAAA;
        }
        
        return color | 0xFF000000;
    }
    
    @Override
    public void addItemInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GREEN + focusDisplayName);
    }
}
