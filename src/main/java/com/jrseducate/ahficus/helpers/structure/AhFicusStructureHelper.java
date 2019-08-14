package com.jrseducate.ahficus.helpers.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

abstract public class AhFicusStructureHelper
{
    public class StructurePiece
    {
        boolean any = false;
        Block block = null;
        List<Block> blocks = null;
        
        public StructurePiece()
        {
            this.any = true;
        }
        
        public StructurePiece(Block block)
        {
            this.block = block;
        }
        
        public StructurePiece(Block ...blocks)
        {
            this.blocks = new ArrayList<Block>();
            
            for(Block block : blocks)
            {
                this.blocks.add(block);
            }
        }
        
        public boolean matches(World world, BlockPos blockPos)
        {
            if(this.any)
            {
                return true;
            }
            if(this.block != null)
            {
                Block block = world.getBlockState(blockPos).getBlock();
                
                return block.equals(this.block);
            }
            if(this.blocks != null)
            {
                boolean result = false;
                Block block = world.getBlockState(blockPos).getBlock();
                
                for(Block blockIter : this.blocks)
                {
                    result |= block.equals(blockIter);
                    
                    if(result)
                    {
                        break;
                    }
                }
                
                return result;
            }
            
            return false;
        }
    }
    
    String name = null;
    Map<BlockPos, StructurePiece> structure;
    int y = 0;
    
    public AhFicusStructureHelper(String name)
    {
        this.name = name;
        structure = new HashMap<BlockPos, StructurePiece>();
    }
    
    public String getName()
    {
        return name;
    }
    
    public void addLayer(String[] layer, Map<Character, StructurePiece> map)
    {
        int x = 0;
        int z = 0;
        
        for(String row : layer)
        {
            for(char cell : row.toCharArray())
            {
                BlockPos blockPos = new BlockPos(x, y, z);
                
                if(cell == '*')
                {
                    structure.put(blockPos, new StructurePiece());
                }
                else
                {
                    StructurePiece piece = map.get(cell);
                    
                    structure.put(blockPos, piece);
                }
                
                x++;
            }

            x = 0;
            z++;
        }
        
        y++;
    }
    
    public List<BlockPos> getStructure(World world, BlockPos blockPos)
    {
        boolean exists = true;
        List<BlockPos> blocks = new ArrayList<BlockPos>();
        
        for(Map.Entry<BlockPos, StructurePiece> entry : structure.entrySet())
        {
            BlockPos entryBlockPos = entry.getKey();
            BlockPos targetBlockPos = blockPos.add(entryBlockPos);
            StructurePiece entryPiece = entry.getValue();
            
            exists &= entryPiece.matches(world, targetBlockPos);
            
            if(!exists)
            {
                break;
            }
            
            if(!entryPiece.any)
            {
                blocks.add(targetBlockPos);
            }
        }
        
        return exists ? blocks : null;
    }
    
    abstract public void activateStructure(World world, List<BlockPos> blocks);
}
