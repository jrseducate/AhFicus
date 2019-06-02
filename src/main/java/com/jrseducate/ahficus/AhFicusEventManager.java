package com.jrseducate.ahficus;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class AhFicusEventManager
{
    AhFicus plugin = null;
    
    public AhFicusEventManager(AhFicus plugin)
    {
        this.plugin = plugin;
    }
    
    @SubscribeEvent
    public void onLoginEvent(PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        
        player.sendMessage(new TextComponentString("Ah Ficus"));
    }

    private long lastUnixTime = 0;
    
    @SubscribeEvent
    public void onWorldTickEvent(WorldTickEvent event)
    {
        if(event.phase == Phase.END && event.side == Side.SERVER)
        {
            World world   = event.world;
            long unixTime = System.currentTimeMillis() / 1000L;
            
            if(unixTime % 5 == 0 && lastUnixTime != unixTime)
            {
                for(Entity entity : world.loadedEntityList)
                {
                    if(entity instanceof EntityPlayer)
                    {
                        EntityPlayer player = (EntityPlayer)entity;

                        player.sendMessage(new TextComponentString(String.format("Ah Ficus: %d", unixTime)));
                        
                        BlockPos playerPos = player.getPosition();
                        int playerX        = playerPos.getX();
                        int playerY        = playerPos.getY();
                        int playerZ        = playerPos.getZ();
                        int radius         = 50;
                        
                        for(int x = playerX - radius; x < playerX + radius; x++)
                        {
                            for(int y = playerY - radius; y < playerY + radius; y++)
                            {
                                for(int z = playerZ - radius; z < playerZ + radius; z++)
                                {
                                    if(world.getBlockState(new BlockPos(x, y, z)).getBlock() == Block.getBlockById(45))
                                    {
                                        BlockPos fencePos = new BlockPos(x, y + 1, z);
                                        boolean valid     = false;
                                        int fenceI        = 0;
                                        int fenceH        = 10;
                                        
                                        while(world.getBlockState(fencePos).getBlock() == Block.getBlockById(85) && fenceI < fenceH)
                                        {
                                            valid    = true;
                                            fencePos = fencePos.add(0, 1, 0);
                                            fenceI++;
                                        }
                                        
                                        if(valid)
                                        {
                                            ArrayList<Message> messages = new ArrayList<Message>();
                                            
                                            for(int i = 0; i < 100; i++)
                                            {                                            
                                                float fenceX  = fencePos.getX() + 0.5f;
                                                float fenceY  = fencePos.getY() + 1.0f;
                                                float fenceZ  = fencePos.getZ() + 0.5f;
                                                float fenceVX = (float)((Math.random() * 2.0f) - 1.0f) * 5.0f;
                                                float fenceVY = -1.0f;
                                                float fenceVZ = (float)((Math.random() * 2.0f) - 1.0f) * 5.0f;
                                                
                                                messages.add(Message.spawnParticle(
                                                    event.side,
                                                    EnumParticleTypes.PORTAL,
                                                    fenceX,
                                                    fenceY,
                                                    fenceZ,
                                                    fenceVX,
                                                    fenceVY,
                                                    fenceVZ
                                                ));
                                            }
                                            
                                            AhFicus.NETWORK.sendToAll(Message.bulkMessage(event.side, messages.toArray(new Message[0])));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            lastUnixTime = unixTime;
        }
    }
}
