package com.jrseducate.ahficus.networking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Var
{    
    
    enum VarType
    {
        INT,
        FLOAT,
        STRING,
        VARLIST,
    }
    
    VarType type;
    int intV;
    float floatV;
    String stringV;
    VarList varlistV;
    
    public Var()
    {
        type = null;
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
    
    public void writeToBuffer(ByteBuf buf)
    {
        buf.writeInt(type.ordinal());
        
        switch(type)
        {
            case INT:
                
                buf.writeInt(intV);
                
                break;

            case FLOAT:
                
                buf.writeFloat(floatV);
                
                break;

            case STRING:
                
                byte[] bytes = stringV.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
                
                break;
            
            case VARLIST:
                
                varlistV.writeToBuffer(buf);
                
                break;
        }
    }
    
    public void readFromBuffer(ByteBuf buf)
    {
        type = VarType.values()[buf.readInt()];

        switch(type)
        {
            case INT:
                
                intV = buf.readInt();
                
                break;
                
            case FLOAT:
                
                floatV = buf.readFloat();
                
                break;

            case STRING:
                
                int length         = buf.readInt();
                ByteBuf byteBuffer = Unpooled.buffer(length);
                
                buf.readBytes(byteBuffer, length);
                
                byte[] bytes = byteBuffer.array();
                stringV      = new String(bytes);
                
                break;
                
            case VARLIST:
                
                varlistV = new VarList();
                
                varlistV.readFromBuffer(buf);
                
                break;
        }
    }
}
