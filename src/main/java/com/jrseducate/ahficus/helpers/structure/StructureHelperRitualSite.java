package com.jrseducate.ahficus.helpers.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.networking.Message;

import net.minecraft.block.state.IBlockState;
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
        map.put('1', new StructurePiece(Blocks.DIRT));
        map.put('2', new StructurePiece(Blocks.STONE));
        map.put('3', new StructurePiece(Blocks.GRAVEL));
        map.put('4', new StructurePiece(Blocks.SAND));
        
        this.addLayer(new String[] {
            "1rlr2",
            "rlblr",
            "lbfbl",
            "rlblr",
            "4rlr3",
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
        List<Message> messages = new ArrayList<Message>();
        
        for(BlockPos blockPos : blocks)
        {
            IBlockState blockState = world.getBlockState(blockPos);
            
            if(blockState.getBlock() == Blocks.STONEBRICK)
            {
                messages.add(Message.spawnLightning(AhFicus.getSide(world), world.provider.getDimension(), blockPos, true));
            }
            if(blockState.getBlock() == Blocks.REDSTONE_ORE)
            {
                blockState = Blocks.LIT_REDSTONE_ORE.getDefaultState();
                world.setBlockState(blockPos, blockState);
            }
        }
        
        if(messages.size() > 0)
        {
            AhFicus.NetworkingManager.Network.sendToAll(Message.bulkMessage(AhFicus.getSide(world), messages.toArray(new Message[0])));
        }
    }
}
