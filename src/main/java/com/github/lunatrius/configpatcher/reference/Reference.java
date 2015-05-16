package com.github.lunatrius.configpatcher.reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {
    public static final String MODID = "ConfigPatcher";
    public static final String NAME = "Config Patcher";
    public static final String VERSION = "${version}";
    public static final String FORGE = "${forgeversion}";
    public static final String MINECRAFT = "${mcversion}";
    public static final String PROXY_SERVER = "com.github.lunatrius.configpatcher.proxy.ServerProxy";
    public static final String PROXY_CLIENT = "com.github.lunatrius.configpatcher.proxy.ClientProxy";
    public static final String GUI_FACTORY = "com.github.lunatrius.configpatcher.client.gui.GuiFactory";

    public static Logger logger = LogManager.getLogger(Reference.MODID);
}
