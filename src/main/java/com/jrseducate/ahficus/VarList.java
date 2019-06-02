package com.jrseducate.ahficus;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;

public class VarList extends ArrayList<Var>
{
    private static final long serialVersionUID = 1L;

    public static VarList fromArray(Var ...vars)
    {
        VarList varList = new VarList();
        
        for(int i = 0; i < vars.length; i++)
        {
            varList.add(vars[i]);
        }
        
        return varList;
    }

    public void writeToBuffer(ByteBuf buf)
    {
        buf.writeInt(this.size());
        
        for(Var var : this)
        {
            var.writeToBuffer(buf);
        }
    }

    public void readFromBuffer(ByteBuf buf)
    {
        this.clear();
        
        int size = buf.readInt();
        
        for(int i = 0; i < size; i++)
        {
            Var var = new Var();
            
            var.readFromBuffer(buf);
            
            this.add(var);
        }
    }
    
}
