package com.jrseducate.ahficus.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.networking.Message;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ItemFoodEnderCookie extends AhFicusItemFood
{
    public static final String RegistryName = "ender_cookie";
    
    public ItemFoodEnderCookie()
    {
        super(RegistryName, 2, false);
        
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(16);
        setAlwaysEdible();
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        
        if(stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            
            if(nbt.hasKey("player_display"))
            {
                tooltip.add(TextFormatting.GREEN + "Player (" + nbt.getString("player_display") + ")");
                return;
            }
            else if(nbt.hasKey("entity_display"))
            {
                tooltip.add(TextFormatting.GREEN + "Entity (" + nbt.getString("entity_display") + ")");
                return;
            }
        }

        tooltip.add(TextFormatting.RED + "Broken Cookie :(");
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {        
        ItemStack stack = playerIn.getHeldItem(handIn);
        NBTTagCompound nbt = stack.getTagCompound();
        long unixTime = AhFicus.getUnix();
        
        if(!stack.hasTagCompound() || !nbt.hasKey("last_eaten") || (nbt.getLong("last_eaten") < unixTime - 2))
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }
    
    private void teleportToTarget(MinecraftServer server, final World finalTargetWorld, EntityPlayer player, final int dimensionFinal, final Vec3d newPosFinal, final String finalTargetName)
    {
        finalTargetWorld.playSound(null, new BlockPos(newPosFinal.x,  newPosFinal.y,  newPosFinal.z), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.MASTER, 0.25f, 1.0f);
        player.world.playSound(null, new BlockPos(player.posX,  player.posY,  player.posZ), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.MASTER, 0.25f, 1.0f);
        
        ArrayList<Message> messages = new ArrayList<Message>();
      
        for(int i = 0; i < 50; i++)
        {                                            
            float x  = (float)newPosFinal.x + 0.5f;
            float y  = (float)newPosFinal.y + 1.0f;
            float z  = (float)newPosFinal.z + 0.5f;
            float vX = (float)((Math.random() * 2.0f) - 1.0f) * 1.5f;
            float vY = (float)((Math.random() * 2.0f) - 1.0f) * 5.0f;
            float vZ = (float)((Math.random() * 2.0f) - 1.0f) * 1.5f;
          
            messages.add(Message.spawnParticle(
                AhFicus.getSide(player.world),
                dimensionFinal,
                EnumParticleTypes.PORTAL,
                x,
                y,
                z,
                vX,
                vY,
                vZ
            ));
        }
      
        for(int i = 0; i < 50; i++)
        {                                            
            float x  = (float)player.posX + 0.5f;
            float y  = (float)player.posY + 1.0f;
            float z  = (float)player.posZ + 0.5f;
            float vX = (float)((Math.random() * 2.0f) - 1.0f) * 1.5f;
            float vY = (float)((Math.random() * 2.0f) - 1.0f) * 5.0f;
            float vZ = (float)((Math.random() * 2.0f) - 1.0f) * 1.5f;
          
            messages.add(Message.spawnParticle(
                AhFicus.getSide(player.world),
                player.dimension,
                EnumParticleTypes.PORTAL,
                x,
                y,
                z,
                vX,
                vY,
                vZ
            ));
        }
        
        server.getCommandManager().executeCommand(server, "/tp " + player.getName() + " " + newPosFinal.x + " " + newPosFinal.y + " " + newPosFinal.z);
        player.sendStatusMessage(new TextComponentString( TextFormatting.RED + "#AhFicus:" + TextFormatting.WHITE + " Telporting to " + finalTargetName), true);
      
        AhFicus.NetworkingManager.Network.sendToAll(Message.bulkMessage(AhFicus.getSide(player.world), messages.toArray(new Message[0])));
    }
    
    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        super.onFoodEaten(stack, worldIn, player);
        
        if(AhFicus.isServer(worldIn) && stack.hasTagCompound())
        {            
            NBTTagCompound nbt = stack.getTagCompound();
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            
            long unixTime = AhFicus.getUnix();
            
            nbt.setLong("last_eaten", unixTime);
            
            stack.setTagCompound(nbt);
            
            if(server != null)
            {
                if(nbt.hasKey("player"))
                {
                    EntityPlayer targetPlayer = server.getPlayerList().getPlayerByUsername(nbt.getString("player"));
                    
                    if(targetPlayer instanceof EntityPlayerMP)
                    {
                        int dimension = targetPlayer.dimension;
                        World targetWorld = AhFicus.getWorld(dimension);
                        String targetName = targetPlayer.getDisplayNameString();
                        Vec3d newPos = new Vec3d(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ);
                        
                        if(player == targetPlayer)
                        {
                            BlockPos spawn = player.getBedLocation(0);
                            
                            spawn = spawn != null ? spawn : player.world.getSpawnPoint();
                            
                            while(player.world.getBlockState(spawn).causesSuffocation() || player.world.getBlockState(spawn.add(0, 1, 0)).causesSuffocation())
                            {
                                spawn = spawn.add(0, 1, 0);
                            }

                            dimension = 0;
                            targetWorld = AhFicus.getWorld(dimension);
                            targetName = "Spawn";
                            newPos = new Vec3d(spawn.getX() + 0.5f, spawn.getY(), spawn.getZ() + 0.5f);
                        }

                        final int dimensionFinal = dimension;
                        final Vec3d newPosFinal = newPos;
                        final String finalTargetName = targetName;
                        final World finalTargetWorld = targetWorld;
                        
                        ITeleporter teleporter = new ITeleporter() {
                            
                            @Override
                            public void placeEntity(World world, Entity entity, float yaw)
                            {
                                teleportToTarget(server, finalTargetWorld, player, dimensionFinal, newPosFinal, finalTargetName);
                            }
                        };
                        
                        if(player.dimension != dimension)
                        {
                            server.getPlayerList().transferPlayerToDimension((EntityPlayerMP) player, dimension, teleporter);
                        }
                        else
                        {
                            teleporter.placeEntity(null, null, 0);
                        }
                    }
                }
                else if(nbt.hasKey("entity") || nbt.hasKey("entity_custom"))
                {
                    List<Entity> entities = player.world.getLoadedEntityList();
                    Entity targetEntity = null;
                    
                    Collections.shuffle(entities);
                    
                    for(Entity entity : entities)
                    {
                        if((nbt.hasKey("entity_custom") && entity.getCustomNameTag() == nbt.getString("entity_custom")) || (nbt.hasKey("entity") && entity.getName() == nbt.getString("entity")))
                        {
                            targetEntity = entity;
                        }
                        
                        if(targetEntity != null)
                        {
                            break;
                        }
                    }
                    
                    if(targetEntity != null)
                    {
                        Vec3d newPos = new Vec3d(targetEntity.posX, targetEntity.posY, targetEntity.posZ);

                        teleportToTarget(server, player.world, player, player.dimension, newPos, nbt.getString("entity_display"));
                    }
                }
            }
        }
    }

}
