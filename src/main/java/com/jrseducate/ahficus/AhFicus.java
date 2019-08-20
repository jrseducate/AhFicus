package com.jrseducate.ahficus;

import org.apache.logging.log4j.Logger;

import com.jrseducate.ahficus.entity.AhFicusEntityManager;
import com.jrseducate.ahficus.entity.render.AhFicusEntityRenderManager;
import com.jrseducate.ahficus.events.AhFicusEventManager;
import com.jrseducate.ahficus.helpers.structure.AhFicusStructureManager;
import com.jrseducate.ahficus.items.AhFicusItemManager;
import com.jrseducate.ahficus.items.helpers.AhFicusItemHelperManager;
import com.jrseducate.ahficus.networking.AhFicusNetworkingManager;
import com.jrseducate.ahficus.proxy.CommonProxy;
import com.jrseducate.ahficus.reference.Reference;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)

// TODO: Comment the code
public class AhFicus
{
    public static Logger logger;
    public static AhFicusNetworkingManager NetworkingManager;
    public static AhFicusEventManager EventManager;
    public static AhFicusItemManager ItemManager;
    public static AhFicusItemHelperManager ItemHelperManager;
    public static AhFicusStructureManager StructureManager;
    public static AhFicusEntityManager EntityManager;
    public static AhFicusEntityRenderManager EntityRenderManager;

    @SidedProxy(clientSide = Reference.PROXY_CLASS_CLIENT, serverSide = Reference.PROXY_CLASS_SERVER)
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        
        NetworkingManager = new AhFicusNetworkingManager();
        NetworkingManager.init();
        
        EventManager = new AhFicusEventManager();
        EventManager.init(Side.CLIENT);
        EventManager.init(Side.SERVER);
        
        ItemManager = new AhFicusItemManager();
        ItemManager.init();
        
        ItemHelperManager = new AhFicusItemHelperManager();
        ItemHelperManager.init();
        
        StructureManager = new AhFicusStructureManager();
        StructureManager.init();
        
        EntityManager = new AhFicusEntityManager();
        EntityManager.init();
        
        EntityRenderManager = new AhFicusEntityRenderManager();
        EntityRenderManager.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
    
    public static Side getSide(World world)
    {
        return world.isRemote ? Side.CLIENT : Side.SERVER;
    }
    
    public static boolean isClient(World world)
    {
        return getSide(world) == Side.CLIENT;
    }
    
    public static boolean isServer(World world)
    {
        return getSide(world) == Side.SERVER;
    }
    
    public static World getWorld(int dimension)
    {
        World targetWorld = DimensionManager.getWorld(dimension);
        
        if(targetWorld == null)
        {
            DimensionManager.initDimension(dimension);
            targetWorld = DimensionManager.getWorld(dimension);
        }
        
        return targetWorld;
    }

    public static long getUnix()
    {
        return System.currentTimeMillis() / 1000L;
    }
}
