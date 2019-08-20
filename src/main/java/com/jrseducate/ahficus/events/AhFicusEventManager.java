package com.jrseducate.ahficus.events;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.items.ItemCustomRendering;
import com.jrseducate.ahficus.items.ItemCustomScrollWheel;
import com.jrseducate.ahficus.items.ItemPreventDefaultRightClick;
import com.jrseducate.ahficus.networking.AhFicusNetworkingManager;
import com.jrseducate.ahficus.networking.Message;
import com.jrseducate.ahficus.networking.MessageManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AhFicusEventManager
{
    public AhFicusEventManager()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public void init(Side side)
    {
        AhFicus.NetworkingManager.Network.registerMessage(MessageManager.class, Message.class, 0, side);
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
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderEvent(RenderWorldLastEvent event)
    {
        Minecraft MINECRAFT = Minecraft.getMinecraft();
        EntityPlayer player = MINECRAFT.player;
        
        if(player instanceof EntityPlayer)
        {
            ItemStack itemStack = player.getHeldItemMainhand();
            Item item = itemStack.getItem();
            
            if(item instanceof ItemCustomRendering)
            {
                ((ItemCustomRendering)item).customRender(player, itemStack);
            }
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void mouseEvent(MouseEvent event)
    {
        if(event.getDwheel() == 0)
        {
            return;
        }
        
        Minecraft MINECRAFT = Minecraft.getMinecraft();
        EntityPlayer player = MINECRAFT.player;
        
        if(player instanceof EntityPlayer)
        {
            ItemStack itemStack = player.getHeldItemMainhand();
            Item item = itemStack.getItem();
            
            if(item instanceof ItemCustomScrollWheel && ((ItemCustomScrollWheel) item).onScrollWheelValid(player, itemStack, itemStack.getTagCompound(), event.getDwheel()))
            {
                AhFicus.NetworkingManager.Network.sendToServer(Message.scrollEvent(Side.CLIENT, player, event.getDwheel()));
                event.setCanceled(true);
            }
        }
    }
}
