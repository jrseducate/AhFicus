package com.jrseducate.ahficus.crafting;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.jrseducate.ahficus.items.ItemWandFocus;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ItemBoundWandRecipe extends ShapelessOreRecipe
{

    public ItemBoundWandRecipe(@Nullable final ResourceLocation group, final NonNullList<Ingredient> input, final ItemStack result)
    {
        super(group, result, input.toArray());
    }
    
    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1)
    {
        ItemStack focus = null;
        
        for(int i = 0; i < var1.getSizeInventory() && focus == null; i++)
        {
            ItemStack itemStack = var1.getStackInSlot(i);
            
            if(itemStack.getItem() instanceof ItemWandFocus)
            {
                focus = itemStack;
            }
        }
        
        if(focus != null && focus.hasTagCompound())
        {
            NBTTagCompound nbt = focus.getTagCompound();

            ItemStack result = output.copy();
            
            result.setTagCompound(nbt);
            
            return result;
        }
        
        return null;
    };

    public static class Factory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json)
        {
            final String group = JsonUtils.getString(json, "group", "");
            final NonNullList<Ingredient> ingredients = RecipeUtil.parseShapeless(context, json);
            final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

            return new ItemBoundWandRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, result);
        }
    }

}
