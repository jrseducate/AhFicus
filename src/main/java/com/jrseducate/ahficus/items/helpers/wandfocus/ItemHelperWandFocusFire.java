package com.jrseducate.ahficus.items.helpers.wandfocus;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemHelperWandFocusFire extends ItemHelperWandFocus
{
    int ticksPerTick;
    
    public ItemHelperWandFocusFire()
    {        
        super("fire", "Fire", 250, 0x00FF0000);
    }
    
    @Override
    public void onItemRightClick(EntityPlayer player, ItemStack stack, NBTTagCompound nbt)
    {
        if(AhFicus.isServer(player.world))
        {
            Vec3d lookDir = player.getLookVec();
            Vec3d eyePos = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
            Vec3d targetPos = eyePos.subtract(0, 0.5f, 0).add(lookDir.scale(2));
            
            EntitySmallFireball fireball = new EntitySmallFireball(player.world, targetPos.x, targetPos.y, targetPos.z, lookDir.x, lookDir.y, lookDir.z);
            player.world.spawnEntity(fireball);
            
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

    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, ItemStack stack, NBTTagCompound nbt, EnumFacing facing,
            float hitX, float hitY, float hitZ)
    {
        if(AhFicus.isServer(player.world))
        {
            BlockPos targetPos = pos.add(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ());
            IBlockState targetBlockState = player.world.getBlockState(targetPos);
            Block targetBlock = targetBlockState.getBlock();
            
            if(player.world.isAirBlock(targetPos) || targetBlock instanceof BlockLiquid)
            {
                IBlockState blockState = Blocks.LAVA.getDefaultState();
                worldIn.setBlockState(targetPos, blockState);
                ((BlockStaticLiquid)blockState.getBlock()).neighborChanged(blockState, player.world, targetPos, Blocks.LAVA, targetPos);
                
                damageItem(stack, player);
            }
        }
        
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public int getItemColor(ItemStack stack, int tintIndex)
    {
        return super.getItemColor(stack, tintIndex, true);
    }

}
