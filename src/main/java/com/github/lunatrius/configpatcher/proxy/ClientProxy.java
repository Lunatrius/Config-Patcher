package com.github.lunatrius.configpatcher.proxy;

import com.github.lunatrius.configpatcher.handler.ConfigurationHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        FMLCommonHandler.instance().bus().register(ConfigurationHandler.INSTANCE);
    }
}
