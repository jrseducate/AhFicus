package com.jrseducate.ahficus.entity.render;

import java.util.HashMap;
import java.util.Map;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.entity.EntityLightning;
import com.jrseducate.ahficus.entity.render.EntityRenderLightning;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class AhFicusEntityRenderManager
{
    Map<Class<? extends Entity>, Class<? extends Render<? extends Entity>>> entityRenderMap;
    
    public AhFicusEntityRenderManager()
    {
        this.entityRenderMap = new HashMap<Class<? extends Entity>, Class<? extends Render<? extends Entity>>>();
    }
    
    public void init()
    {        
        addEntityRenderers();
        registerEntityRenderers();
    }
    
    public void addEntityRenderers()
    {
        this.entityRenderMap.put(EntityLightning.class, EntityRenderLightning.class);
    }
    
    public void registerEntityRenderers()
    {
        for(Map.Entry<Class<? extends Entity>, Class<? extends Render<? extends Entity>>> entry : entityRenderMap.entrySet())
        {
            final Class<? extends Render<? extends Entity>> renderClass = entry.getValue();
            RenderingRegistry.registerEntityRenderingHandler(entry.getKey(), new IRenderFactory<Entity>() {

                @SuppressWarnings("unchecked")
                @Override
                public Render<Entity> createRenderFor(RenderManager manager)
                {
                    try
                    {
                        return (Render<Entity>) renderClass.getConstructor(RenderManager.class).newInstance(manager);
                    } 
                    catch (Exception ex)
                    {
                        AhFicus.logger.info(ex.getMessage());
                        return null;
                    }
                }
                
            });
        }
    }
}
