package com.jrseducate.ahficus.items;

import java.util.List;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.helpers.structure.AhFicusStructureHelper;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRitualStone extends AhFicusItem implements IItemColor
{    
    public static final String RegistryName = "ritual_stone";
    
    public ItemRitualStone()
    {
        super(RegistryName);
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(16);
    }
    
    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem)
    {
        ItemStack itemStack = entityItem.getItem();
        NBTTagCompound nbt = itemStack.getTagCompound();
        
        if(AhFicus.isServer(entityItem.world) && entityItem.isBurning() && itemStack.hasTagCompound() && nbt.hasKey("active") && nbt.getBoolean("active"))
        {
            entityItem.setEntityInvulnerable(true);
            
            BlockPos blockPos = entityItem.getPosition();
            
            AhFicusStructureHelper structureManager = AhFicus.StructureManager.getStructureHelper("ritual_site");
            List<BlockPos> structureBlocks = structureManager.getStructure(entityItem.world, blockPos);
            
            if(structureBlocks != null)
            {
                entityItem.setDead();
                
                structureManager.activateStructure(entityItem.world, structureBlocks);
            }
        }
        
        return super.onEntityItemUpdate(entityItem);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(AhFicus.isServer(entityIn.world))
        {
            NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            
            boolean isDay = entityIn.world.isDaytime();
            boolean isRaining = entityIn.world.isRaining();
            
            if(!isDay && !isRaining && !nbt.hasKey("active"))
            {
                nbt.setBoolean("active", true);
                
                stack.setTagCompound(nbt);
            }
            else if((isDay || isRaining) && nbt.hasKey("active"))
            {
                nbt.removeTag("active");
                
                stack.setTagCompound(nbt);
            }
        }
        
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }
    
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        
        if(tintIndex == 0)
        {
            int color = 0xFF22F082;
            
            if(!(stack.hasTagCompound() && nbt.hasKey("active") && nbt.getBoolean("active")))
            {
                color = color & 0xFF444444;
            }
//            if(stack)
            
            return color;
        }
        
        return -1;
    }
}
