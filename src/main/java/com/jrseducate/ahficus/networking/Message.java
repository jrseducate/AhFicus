package com.jrseducate.ahficus.networking;

import com.jrseducate.ahficus.AhFicus;
import com.jrseducate.ahficus.entity.EntityLightning;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class Message implements IMessage
{
    public static MessageManager manager = null;
    
    public Message()
    {
        this.type = null;
        this.vals = new VarList();
    }

    enum MessageType {
        Null,
        Bulk,
        SpawnParticle,
        SpawnWeather,
    }
    
    public MessageType type;
    public VarList vals;
    public int index = 0;
    public static int messageIndex = 0;
    
    public Message(Side side, MessageType type)
    {
        this.type = type;
        this.vals = new VarList();
    }
    
    public Message(Side side, MessageType type, VarList vals)
    {
        this.type = type;
        this.vals = vals;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(type.ordinal());

        vals.writeToBuffer(buf);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        type = MessageType.values()[buf.readInt()];

        vals.readFromBuffer(buf);
    }
    
    public Var getVar() throws Exception
    {
        return getVar(index++);
    }
    
    public Var getVar(int index) throws Exception
    {
        Var var = vals.get(index);
        
        if(var == null)
        {
            throw new Exception("Failed to get Var in Message (" + type + ") at index (" + index + ")");
        }
        
        return var;
    }
    
    public boolean handle(MessageContext ctx)
    {
        boolean result = false;
        
        result |= handleBulkMessage(ctx);
        result |= handleSpawnParticle(ctx);
        result |= handleSpawnWeather(ctx);
        
        return result;
    }
    
    public static Message bulkMessage(Side side, Message ...messages)
    {
        VarList varList = new VarList();

        varList.add(new Var(messages.length));
        
        for(Message message : messages)
        {
            varList.add(new Var(message.type.ordinal()));
            varList.add(new Var(message.vals));
        }
        
        return new Message(side, MessageType.Bulk, varList);
    }
    
    public boolean handleBulkMessage(MessageContext ctx)
    {
        if(type == MessageType.Bulk)
        {
            try
            {
                int length = getVar().getInt();
                
                for(int i = 0; i < length; i++)
                {
                    MessageType messageType = MessageType.values()[getVar().getInt()];
                    VarList varList         = getVar().getVarList();
                    
                    Message message = new Message(ctx.side, messageType, varList);
                    
                    message.handle(ctx);
                }
                
                return true;
            } catch (Exception ex)
            {
                AhFicus.logger.info(ex.getMessage());
            }
        }
        
        return false;
    }
    
    public static Message spawnParticle(Side side, int dimension, EnumParticleTypes pt, double x, double y, double z, double vx, double vy, double vz)
    {
        if(side != Side.SERVER)
        {
            return new Message(side, MessageType.Null);
        }
        
        return new Message(side, MessageType.SpawnParticle, VarList.fromArray(
            new Var(dimension),
            new Var(pt.getParticleName()),
            new Var(x),
            new Var(y),
            new Var(z),
            new Var(vx),
            new Var(vy),
            new Var(vz)
        ));
    }
    
    public boolean handleSpawnParticle(MessageContext ctx)
    {
        if(type == MessageType.SpawnParticle && ctx.side == Side.CLIENT)
        {
            final Minecraft MINECRAFT = Minecraft.getMinecraft();
            
            try
            {
                int dimension       = getVar().getInt();
                String particleType = getVar().getString();
                float xCoord        = getVar().getFloat();
                float yCoord        = getVar().getFloat();
                float zCoord        = getVar().getFloat();
                float xSpeed        = getVar().getFloat();
                float ySpeed        = getVar().getFloat();
                float zSpeed        = getVar().getFloat();
                
                if(MINECRAFT.world.provider.getDimension() == dimension)
                {
                    MINECRAFT.world.spawnParticle(EnumParticleTypes.getByName(particleType), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
                }
                
                return true;
            }
            catch(Exception ex)
            {
                AhFicus.logger.info(ex.getMessage());
            }
        }
        
        return false;
    }
    
    public static Message spawnLightning(Side side, int dimension, BlockPos blockPos, boolean effectOnly)
    {
        if(side != Side.SERVER)
        {
            return new Message(side, MessageType.Null);
        }
        
        return new Message(side, MessageType.SpawnWeather, VarList.fromArray(
            new Var(dimension),
            new Var(blockPos),
            new Var(effectOnly)
        ));
    }
    
    public boolean handleSpawnWeather(MessageContext ctx)
    {
        if(type == MessageType.SpawnWeather && ctx.side == Side.CLIENT)
        {
            final Minecraft MINECRAFT = Minecraft.getMinecraft();
            
            try
            {
                int dimension      = getVar().getInt();
                BlockPos blockPos  = getVar().getBlockPos();
                boolean effectOnly = getVar().getBoolean();
                
                if(MINECRAFT.world.provider.getDimension() == dimension)
                {
                    EntityLightning weatherEffect = new EntityLightning(MINECRAFT.world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), effectOnly);
                    weatherEffect.setPlayer(MINECRAFT.player);
                    
                    MINECRAFT.world.addWeatherEffect(weatherEffect);
                }
                
                return true;
            }
            catch(Exception ex)
            {
                AhFicus.logger.info(ex.getMessage());
            }
        }
        
        return false;
    }
}
