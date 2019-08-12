package com.jrseducate.ahficus.proxy;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @SubscribeEvent
    public static void registerItemColourHandlers(final ColorHandlerEvent.Item event)
    {
        final ItemColors itemColors = event.getItemColors();
        
        for(Item item : AhFicus.ItemManager.getColoredItems())
        {
            itemColors.registerItemColorHandler((IItemColor) item, item);
        }
    }
}
