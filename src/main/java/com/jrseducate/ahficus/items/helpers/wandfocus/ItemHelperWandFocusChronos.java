package com.jrseducate.ahficus.items.helpers.wandfocus;

import com.jrseducate.ahficus.AhFicus;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHelperWandFocusChronos extends ItemHelperWandFocus
{
    int ticksPerTick;
    
    public ItemHelperWandFocusChronos()
    {        
        super("chronos", "Chronos", 1000, 0x00FFFF00);
        
        ticksPerTick = 25;
    }
    
    public void clearNBT(ItemStack stack, NBTTagCompound nbt)
    {
        nbt.removeTag("dimension");
        nbt.removeTag("x");
        nbt.removeTag("y");
        nbt.removeTag("z");
    }

    @Override
    public void onItemRightClick(EntityPlayer player, ItemStack stack, NBTTagCompound nbt)
    {
        if(AhFicus.isServer(player.world))
        {
            if(nbt.getLong("last_use") < AhFicus.getUnix() - 0.25)
            {
                clearNBT(stack, nbt);
            }
            
            stack.setTagCompound(nbt);
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
            if(nbt.hasKey("dimension") && nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z") && isSelected)
            {
                int dimension = nbt.getInteger("dimension");
                int ticksUsed = nbt.hasKey("ticks_used") ? nbt.getInteger("ticks_used") : -1;
                
                if(entity.dimension == dimension)
                {
                    BlockPos blockPos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
                    
                    IBlockState blockState = entity.world.getBlockState(blockPos);
                    
                    TileEntity tileEntity = entity.world.getTileEntity(blockPos);
                    
                    for(int i = 0; i < ticksPerTick; i++)
                    {
                        if(tileEntity != null)
                        {
                            if(tileEntity instanceof ITickable)
                            {
                                ((ITickable) tileEntity).update();
                            }
                        }
                        
                        entity.world.immediateBlockTick(blockPos, blockState, entity.world.rand);
                    }
                    
                    if(ticksUsed > 20)
                    {
                        ticksUsed = -1;
                        
                        damageItem(stack, entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null);
                    }
                    
                    nbt.setInteger("ticks_used", ticksUsed + 1);
                    
                    stack.setTagCompound(nbt);
                    
                    return;
                }
            }

            clearNBT(stack, nbt);
            
            stack.setTagCompound(nbt);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, ItemStack stack, NBTTagCompound nbt, EnumFacing facing,
            float hitX, float hitY, float hitZ)
    {
        if(AhFicus.isServer(player.world))
        {
            if(nbt.hasKey("dimension") && nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z"))
            {
                BlockPos blockPos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
                
                if(blockPos.equals(pos))
                {
                    clearNBT(stack, nbt);
                    
                    stack.setTagCompound(nbt);
                    
                    return EnumActionResult.SUCCESS;
                }
            }
                
            nbt.setInteger("dimension", player.dimension);
            nbt.setInteger("x", pos.getX());
            nbt.setInteger("y", pos.getY());
            nbt.setInteger("z", pos.getZ());
            
            stack.setTagCompound(nbt);
        }
        
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public int getItemColor(ItemStack stack, int tintIndex)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        
        return super.getItemColor(stack, tintIndex, stack.hasTagCompound() && nbt.hasKey("dimension") && nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z"));
    }

}
