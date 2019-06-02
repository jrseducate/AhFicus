package com.jrseducate.ahficus;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageManager implements IMessageHandler<Message, IMessage>
{
    public static int messageIndex = 0;
    
    public MessageManager()
    {
        Message.manager = this;
    }
    
    @Override
    public IMessage onMessage(Message message, MessageContext ctx)
    {
        AhFicus.logger.info("Ah Ficus: Message Recieved (" + ctx.side.name() + ") (" + message.type.name() + ") (" + messageIndex + ")");
        
        if(message.type != Message.MessageType.Null)
        {
            message.handle(ctx);
            
            messageIndex++;
        }
        
        return null;
    }
}
