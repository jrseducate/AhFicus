package com.jrseducate.ahficus.items;

import java.util.List;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.items.helpers.AhFicusItemHelper;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemWandFocus extends AhFicusItem implements IItemColor
{
    public static final String RegistryName = "wand_focus";
    
    public ItemWandFocus()
    {
        super(RegistryName);
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(64);
    }
    
    public AhFicusItemHelper getItemHelper(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        
        if(stack.hasTagCompound() && nbt.hasKey("focus_type"))
        {
            return AhFicus.ItemHelperManager.getItemHelper("wand_focus:" + nbt.getString("focus_type"));
        }
        
        return null;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        AhFicusItemHelper itemHelper = getItemHelper(stack);
        
        if(itemHelper != null)
        {
            itemHelper.addItemInformation(stack, worldIn, tooltip, flagIn);
        }
        else
        {
            tooltip.add(TextFormatting.RED + "Broken Focus :(");
        }
    }
    
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex)
    {
        AhFicusItemHelper itemHelper = getItemHelper(stack);
        
        if(itemHelper != null)
        {
            return itemHelper.getItemColor(stack, tintIndex);
        }
        
        return -1;
    }
}
