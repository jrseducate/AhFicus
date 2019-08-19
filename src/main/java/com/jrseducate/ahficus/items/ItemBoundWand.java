package com.jrseducate.ahficus.items;

import java.util.List;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.items.helpers.AhFicusItemHelper;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBoundWand extends AhFicusItem implements ItemPreventDefaultRightClick, ItemCustomRendering, IItemColor
{
    public static final String RegistryName = "bound_wand";
    
    public ItemBoundWand()
    {
        super(RegistryName);
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
        setMaxDamage(9999);
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
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
            tooltip.add(TextFormatting.RED + "Broken Bound Wand :(");
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
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        AhFicusItemHelper itemHelper = getItemHelper(stack);
        
        if(itemHelper != null)
        {
            itemHelper.onLeftClickEntity(player, entity, stack, stack.getTagCompound());
        }
        
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        AhFicusItemHelper itemHelper = getItemHelper(stack);
        
        if(itemHelper != null)
        {
            itemHelper.onItemRightClick(playerIn, stack, stack.getTagCompound());
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
            EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        AhFicusItemHelper itemHelper = getItemHelper(stack);
        
        if(itemHelper != null)
        {
            return itemHelper.onItemUse(player, worldIn, pos, stack, stack.getTagCompound(), facing, hitX, hitY, hitZ);
        }
        
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {        
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        AhFicusItemHelper itemHelper = getItemHelper(stack);
        
        if(itemHelper != null)
        {
            itemHelper.onUpdate(entityIn, stack, stack.getTagCompound(), isSelected);
        }
    }

    @Override
    public void customRender(EntityPlayer player, ItemStack itemStack)
    {
        AhFicusItemHelper itemHelper = getItemHelper(itemStack);
        
        if(itemHelper != null)
        {
            itemHelper.customRender(player, itemStack, itemStack.getTagCompound());
        }
    }
}
