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
    ArrayList<Map<BlockPos, StructurePiece>> structure;
    int y = 0;
    
    public AhFicusStructureHelper(String name)
    {
        this.name = name;
        structure = new ArrayList<Map<BlockPos, StructurePiece>>();
        
        for(int i = 0; i < 4; i++)
        {
            structure.add(new HashMap<BlockPos, StructurePiece>());
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public void addLayer(String[] layer, Map<Character, StructurePiece> map)
    {
        int x = 0;
        int z = 0;
        int height = layer.length;
        
        for(String row : layer)
        {
            int width = row.length();
            
            for(char cell : row.toCharArray())
            {
                int x2 = z;
                int z2 = width - x - 1;
                int x3 = z2;
                int z3 = height - x2 - 1;
                int x4 = z3;
                int z4 = width - x3 - 1;
                
                BlockPos blockPosList[] = {
                        new BlockPos(x, y, z),
                        new BlockPos(x2, y, z2),
                        new BlockPos(x3, y, z3),
                        new BlockPos(x4, y, z4),
                };
                
                for(int i = 0; i < blockPosList.length; i++)
                {
                    BlockPos blockPos = blockPosList[i];
                    
                    if(cell == '*')
                    {
                        structure.get(i).put(blockPos, new StructurePiece());
                    }
                    else
                    {
                        StructurePiece piece = map.get(cell);
                        
                        structure.get(i).put(blockPos, piece);
                    }
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
        for(int i = 0; i < structure.size(); i++)
        {
            boolean exists = true;
            List<BlockPos> blocks = new ArrayList<BlockPos>();
            
            for(Map.Entry<BlockPos, StructurePiece> entry : structure.get(i).entrySet())
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
            
            if(exists)
            {
                return blocks;
            }
        }
        
        return null;
    }
    
    abstract public void activateStructure(World world, List<BlockPos> blocks);
}
