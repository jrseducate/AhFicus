package com.jrseducate.ahficus.items.helpers.wandfocus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

import org.lwjgl.opengl.GL11;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHelperWandFocusFinder extends ItemHelperWandFocus
{
    int ticksPerTick;
    
    public ItemHelperWandFocusFinder()
    {        
        super("finder", "Finder", 250, 0x00FAAFFA);
    }
    
    @Override
    public void onItemRightClick(EntityPlayer player, ItemStack stack, NBTTagCompound nbt)
    {
        if(AhFicus.isServer(player.world))
        {
            nbt.setInteger("ticks_active", 40);
            
            damageItem(stack, player);
        }
    }

    @Override
    public void onLeftClickEntity(EntityPlayer player, Entity entity, ItemStack stack, NBTTagCompound nbt)
    {
        
    }

    @Override
    public void onUpdate(Entity entity, ItemStack stack, NBTTagCompound nbt, boolean isSelected)
    {
        if(AhFicus.isServer(entity.world))
        {
            if(nbt.hasKey("ticks_active"))
            {
                int ticksActive = nbt.getInteger("ticks_active");
                
                if(ticksActive > 0)
                {
                    nbt.setInteger("ticks_active", ticksActive - 1);
                }
                else
                {
                    nbt.removeTag("ticks_active");
                }
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, ItemStack stack, NBTTagCompound nbt, EnumFacing facing,
            float hitX, float hitY, float hitZ)
    {
        if(AhFicus.isServer(player.world))
        {
            IBlockState targetBlockState = player.world.getBlockState(pos);
            Block targetBlock = targetBlockState.getBlock();
            
            nbt.setInteger("target_block", Block.getIdFromBlock(targetBlock));
            nbt.setString("target_block_display", targetBlock.getLocalizedName());
        }
        
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public int getItemColor(ItemStack stack, int tintIndex)
    {
        return super.getItemColor(stack, tintIndex, true);
    }

    @Override
    public void customRender(EntityPlayer player, ItemStack stack, NBTTagCompound nbt)
    {
        if(nbt.hasKey("ticks_active"))
        {
            int ticksActive = nbt.getInteger("ticks_active");
            
            if(ticksActive > 0)
            {
                Block block = Block.getBlockById(nbt.getInteger("target_block"));

                if(AhFicus.isClient(player.world) && block != Blocks.AIR)
                {                   
                    GlStateManager.pushMatrix();
                    GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                    int radius = 32;
                    ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
                    
                    for(int x = -radius; x <= radius; x++)
                    {
                        for(int y = -radius; y <= radius; y++)
                        {
                            for(int z = -radius; z <= radius; z++)
                            {
                                BlockPos pos = player.getPosition().add(x, y, z);
                                IBlockState state = player.world.getBlockState(pos);
                                
                                if(state.getBlock() == block)
                                {
                                    blocks.add(pos);
                                }
                            }
                        }
                    }
                    
                    Collections.sort(blocks, new Comparator<BlockPos>()
                    {

                        @Override
                        public int compare(BlockPos arg0, BlockPos arg1)
                        {
                            if (arg0 == arg1)
                            {
                                return 0;
                            }
                            else if (arg0.distanceSq(player.posX, player.posY, player.posZ) <
                                     arg1.distanceSq(player.posX, player.posY, player.posZ))
                            {
                                return -1;
                            }
                            
                            return 1;
                        }
                        
                    });
                    blocks = new ArrayList<BlockPos>(blocks.subList(0, Math.min(50, blocks.size())));
                    
                    ListIterator<BlockPos> listIterator = blocks.listIterator(blocks.size());
                    while (listIterator.hasPrevious())
                    {
                        BlockPos pos = listIterator.previous();
                        IBlockState state = player.world.getBlockState(pos);
                        
                        double renderPosX = Minecraft.getMinecraft().getRenderManager().viewerPosX;
                        double renderPosY = Minecraft.getMinecraft().getRenderManager().viewerPosY;
                        double renderPosZ = Minecraft.getMinecraft().getRenderManager().viewerPosZ;

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
                        GlStateManager.disableDepth();
                        
                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
                        GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
                        GlStateManager.color(1, 1, 1, 1);
                        brd.renderBlockBrightness(state, 1.0F);

                        GlStateManager.color(1F, 1F, 1F, 1F);
                        GlStateManager.enableDepth();
                        GlStateManager.popMatrix();
                        GlStateManager.popMatrix();
                    }
                    
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
}
