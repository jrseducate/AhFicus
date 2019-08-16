package com.jrseducate.ahficus.networking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class Var
{    
    
    enum VarType
    {
        BOOLEAN,
        INT,
        FLOAT,
        STRING,
        VARLIST,
        NBT,
        CLASS,
        BLOCK_POS,
    }
    
    // TODO: Optimize storage of different types (possibly using subclasses?)
    VarType type;
    boolean booleanV;
    int intV;
    float floatV;
    String stringV;
    VarList varlistV;
    NBTTagCompound nbtV;
    Class<?> classV;
    BlockPos blockPosV;
    
    public Var()
    {
        type = null;
    }
    
    public Var(boolean val)
    {
        type = VarType.BOOLEAN;
        booleanV = val;
    }
    
    public Var(int val)
    {
        type = VarType.INT;
        intV = val;
    }
    
    public Var(float val)
    {
        type   = VarType.FLOAT;
        floatV = val;
    }
    
    public Var(double val)
    {
        type   = VarType.FLOAT;
        floatV = (float)val;
    }
    
    public Var(String val)
    {
        type    = VarType.STRING;
        stringV = val;
    }
    
    public Var(VarList val)
    {
        type     = VarType.VARLIST;
        varlistV = val;
    }
    
    public Var(NBTTagCompound val)
    {
        type = VarType.NBT;
        nbtV = val;
    }
    
    public Var(Class<?> val)
    {
        type   = VarType.CLASS;
        classV = val;
    }
    
    public Var(BlockPos blockPos)
    {
        type      = VarType.BLOCK_POS;
        blockPosV = blockPos;
    }
    
    public boolean getBoolean() throws Exception
    {
        if(type != VarType.BOOLEAN)
        {
            throw new Exception("Var is not of type " + VarType.BOOLEAN.name());
        }
        
        return this.booleanV;
    }
    
    public int getInt() throws Exception
    {
        if(type == VarType.FLOAT)
        {
            return (int)this.floatV;
        }
        if(type != VarType.INT)
        {
            throw new Exception("Var is not of type " + VarType.INT.name());
        }
        
        return this.intV;
    }
    
    public float getFloat() throws Exception
    {
        if(type == VarType.INT)
        {
            return (float)this.intV;
        }
        if(type != VarType.FLOAT)
        {
            throw new Exception("Var is not of type " + VarType.FLOAT.name());
        }
        
        return this.floatV;
    }
    
    public String getString() throws Exception
    {
        if(type != VarType.STRING)
        {
            throw new Exception("Var is not of type " + VarType.STRING.name());
        }
        
        return this.stringV;
    }
    
    public VarList getVarList() throws Exception
    {
        if(type != VarType.VARLIST)
        {
            throw new Exception("Var is not of type " + VarType.VARLIST.name());
        }
        
        return this.varlistV;
    }
    
    public NBTTagCompound getNBT() throws Exception
    {
        if(type != VarType.NBT)
        {
            throw new Exception("Var is not of type " + VarType.NBT.name());
        }
        
        return this.nbtV;
    }
    
    public Class<?> getVarClass() throws Exception
    {
        if(type != VarType.CLASS)
        {
            throw new Exception("Var is not of type " + VarType.CLASS.name());
        }
        
        return this.classV;
    }
    
    public BlockPos getBlockPos() throws Exception
    {
        if(type != VarType.BLOCK_POS)
        {
            throw new Exception("Var is not of type " + VarType.BLOCK_POS.name());
        }
        
        return blockPosV;
    }
    
    public void writeToBuffer(ByteBuf buf)
    {
        buf.writeInt(type.ordinal());
        
        switch(type)
        {
            case BOOLEAN:
            {
                buf.writeBoolean(booleanV);
            } break;
            
            case INT:
            {
                buf.writeInt(intV);
            } break;

            case FLOAT:
            {
                buf.writeFloat(floatV);
            } break;

            case STRING:
            {
                byte[] bytes = stringV.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            } break;
            
            case VARLIST:
            {
                varlistV.writeToBuffer(buf);
            } break;
                
            case NBT:
            {
                ByteBufUtils.writeTag(buf, nbtV);
            } break;
                
            case CLASS:
            {
                String string = classV.getName();
                byte[] bytes = string.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            } break;
                
            case BLOCK_POS:
            {
                buf.writeInt(blockPosV.getX());
                buf.writeInt(blockPosV.getY());
                buf.writeInt(blockPosV.getZ());
            } break;
        }
    }
    
    public void readFromBuffer(ByteBuf buf)
    {
        type = VarType.values()[buf.readInt()];

        try
        {
            switch(type)
            {
                case BOOLEAN:
                {
                    booleanV = buf.readBoolean();
                } break;
                
                case INT:
                {
                    intV = buf.readInt();
                } break;
                    
                case FLOAT:
                {
                    floatV = buf.readFloat();
                } break;

                case STRING:  
                {
                    int length         = buf.readInt();
                    ByteBuf byteBuffer = Unpooled.buffer(length);
                    
                    buf.readBytes(byteBuffer, length);
                    
                    byte[] bytes = byteBuffer.array();
                    stringV      = new String(bytes);
                } break;
                    
                case VARLIST:
                {
                    varlistV = new VarList();
                    
                    varlistV.readFromBuffer(buf);
                } break;
                    
                case NBT:
                {
                    nbtV = ByteBufUtils.readTag(buf);
                } break;
                
                case CLASS:
                {
                    int length         = buf.readInt();
                    ByteBuf byteBuffer = Unpooled.buffer(length);
                    
                    buf.readBytes(byteBuffer, length);
                    
                    byte[] bytes  = byteBuffer.array();
                    String string = new String(bytes);
                    classV        = Class.forName(string);
                } break;
                    
                case BLOCK_POS:
                {
                    int x     = buf.readInt();
                    int y     = buf.readInt();
                    int z     = buf.readInt();
                    blockPosV = new BlockPos(x, y, z);
                } break;
            }
        }
        catch(Exception ex) {};
    }
}
