package com.jrseducate.ahficus.helpers.structure;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureHelperRitualSite extends AhFicusStructureHelper
{
    public StructureHelperRitualSite()
    {
        super("ritual_site");
        
        HashMap<Character, StructurePiece> map = new HashMap<Character, StructurePiece>();
        
        map.put('l', new StructurePiece(Blocks.LAPIS_BLOCK));
        map.put('r', new StructurePiece(Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE));
        map.put('b', new StructurePiece(Blocks.STONEBRICK));
        map.put('f', new StructurePiece(Blocks.FIRE));
        
        this.addLayer(new String[] {
            "lrlrl",
            "rlblr",
            "lbfbl",
            "rlblr",
            "lrlrl",
        }, map);
    }
    
    @Override
    public List<BlockPos> getStructure(World world, BlockPos blockPos)
    {
        blockPos = blockPos.add(-2, 0, -2);
        
        return super.getStructure(world, blockPos);
    }

    @Override
    public void activateStructure(World world, List<BlockPos> blocks)
    {
        for(BlockPos blockPos : blocks)
        {
            IBlockState blockState = world.getBlockState(blockPos);
            
            if(blockState.getBlock() == Blocks.STONEBRICK)
            {
                world.addWeatherEffect(new EntityLightningBolt(world, (double)blockPos.getX() + 0.5f, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5f, true));
            }
            if(blockState.getBlock() == Blocks.REDSTONE_ORE)
            {
                blockState = Blocks.LIT_REDSTONE_ORE.getDefaultState();
                world.setBlockState(blockPos, blockState);
            }
        }
    }
}
