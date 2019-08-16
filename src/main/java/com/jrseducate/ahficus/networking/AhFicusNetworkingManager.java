package com.jrseducate.ahficus.networking;

import com.jrseducate.ahficus.reference.Reference;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class AhFicusNetworkingManager
{
    public SimpleNetworkWrapper Network;
    
    public void init()
    {
        Network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
    }
}
