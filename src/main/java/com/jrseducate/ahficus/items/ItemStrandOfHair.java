package com.jrseducate.ahficus.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemStrandOfHair extends AhFicusItem
{
    public static final String RegistryName = "strand_of_hair";
    
    public ItemStrandOfHair()
    {
        super(RegistryName);
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(16);
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        if(stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            
            if(nbt.hasKey("player_display"))
            {
                tooltip.add(TextFormatting.GREEN + "Player (" + nbt.getString("player_display") + ")");
                return;
            }
            else if(nbt.hasKey("entity_display"))
            {
                tooltip.add(TextFormatting.GREEN + "Entity (" + nbt.getString("entity_display") + ")");
                return;
            }
        }

        tooltip.add(TextFormatting.RED + "Broken Strand Of Hair :(");
    }
}
