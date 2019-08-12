package com.jrseducate.ahficus.items;

import java.util.List;
import java.util.Objects;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.reference.Reference;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemWandFocus extends Item implements IItemColor
{
    public static final String RegistryName = "wand_focus";
    
    public ItemWandFocus()
    {
        super();
        
        setRegistryName(Reference.MOD_ID, RegistryName);
        final ResourceLocation registryName = Objects.requireNonNull(getRegistryName());
        setUnlocalizedName(registryName.toString());
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(64);
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        if(stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            
            if(nbt.hasKey("focus_type"))
            {
                tooltip.add(TextFormatting.GREEN + AhFicus.ItemManager.getFocusTypeName(nbt.getString("focus_type")));
                return;
            }
        }

        tooltip.add(TextFormatting.RED + "Broken Focus :(");
    }
    
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex)
    {
        return AhFicus.ItemManager.getItemColor(RegistryName, stack, tintIndex);
    }
}
