package com.jrseducate.ahficus.events;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.items.ItemPreventDefaultRightClick;
import com.jrseducate.ahficus.networking.AhFicusNetworkingManager;
import com.jrseducate.ahficus.networking.Message;
import com.jrseducate.ahficus.networking.MessageManager;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;

public class AhFicusEventManager
{
    AhFicusNetworkingManager NetworkingManager;
    
    public AhFicusEventManager(AhFicusNetworkingManager NetworkingManager)
    {
        this.NetworkingManager = NetworkingManager;
    }
    
    public void init(Side side)
    {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkingManager.Network.registerMessage(MessageManager.class, Message.class, 0, side);
    }
    
    public void registerRender(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(AhFicus.ItemManager.getItems());
    }
    
    @SubscribeEvent
    public void registerRenders(ModelRegistryEvent event)
    {
        for(Item item : AhFicus.ItemManager.getItems())
        {
            registerRender(item);
        }
    }
    
    @SubscribeEvent
    public void onLoginEvent(PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        
        player.sendMessage(new TextComponentString("Ah Ficus"));
    }

    @SubscribeEvent
    public void onInteractEvent(PlayerInteractEvent.EntityInteract event)
    {
        ItemStack itemStack = event.getItemStack();
        
        if(itemStack.getItem() instanceof ItemPreventDefaultRightClick)
        {
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.FAIL);
            event.setResult(Result.DENY);
        }
    }
    
    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
//        event.getRegistry().register(new EntityEntry(EntityLightning.class, EntityLightning.RegistryName));
    }
}
