package com.github.lunatrius.configpatcher.handler;

import com.github.lunatrius.configpatcher.reference.Names;
import com.github.lunatrius.configpatcher.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigurationHandler {
    public static final ConfigurationHandler INSTANCE = new ConfigurationHandler();

    public static final String VERSION = "1";

    public static Configuration configuration;

    public static final boolean PATCH_DEFAULT = true;
    public static final boolean PATCH_ONCE_DEFAULT = false;
    public static final boolean GENERATE_DEFAULT = false;

    public static boolean patch = PATCH_DEFAULT;
    public static boolean patchOnce = PATCH_ONCE_DEFAULT;
    public static boolean generate = GENERATE_DEFAULT;

    public static Property propPatch = null;
    public static Property propPatchOnce = null;
    public static Property propGenerate = null;

    public static void init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile, VERSION);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        propPatch = configuration.get(Names.Config.Category.PATCHER, Names.Config.PATCH, PATCH_DEFAULT, Names.Config.PATCH_DESC);
        propPatch.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.PATCH);
        propPatch.setRequiresMcRestart(true);
        patch = propPatch.getBoolean(PATCH_DEFAULT);

        propPatchOnce = configuration.get(Names.Config.Category.PATCHER, Names.Config.PATCH_ONCE, PATCH_ONCE_DEFAULT, Names.Config.PATCH_ONCE_DESC);
        propPatchOnce.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.PATCH_ONCE);
        propPatchOnce.setRequiresMcRestart(true);
        patchOnce = propPatchOnce.getBoolean(PATCH_ONCE_DEFAULT);

        propGenerate = configuration.get(Names.Config.Category.PATCHER, Names.Config.GENERATE, GENERATE_DEFAULT, Names.Config.GENERATE_DESC);
        propGenerate.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.GENERATE);
        propGenerate.setRequiresMcRestart(true);
        generate = propGenerate.getBoolean(GENERATE_DEFAULT);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private ConfigurationHandler() {}

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MODID)) {
            loadConfiguration();
        }
    }

    public static boolean shouldPatch() {
        final boolean ret = patch || patchOnce;

        if (patchOnce) {
            propPatchOnce.set(false);
            loadConfiguration();
        }

        return ret && !generate;
    }
}
