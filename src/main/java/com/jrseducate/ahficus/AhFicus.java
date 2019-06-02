package com.jrseducate.ahficus;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import org.apache.logging.log4j.Logger;

@Mod(modid = AhFicus.MODID, name = AhFicus.NAME, version = AhFicus.VERSION)
public class AhFicus
{
    public static final String MODID = "ahficus";
    public static final String NAME = "Ah Ficus";
    public static final String VERSION = "1.0";
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(AhFicus.MODID);

    public static Logger logger;
    public static AhFicusEventManager eventManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger       = event.getModLog();
        eventManager = new AhFicusEventManager(this);
        
        MinecraftForge.EVENT_BUS.register(eventManager);
        NETWORK.registerMessage(MessageManager.class, Message.class, 0, event.getSide());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        
    }
}
