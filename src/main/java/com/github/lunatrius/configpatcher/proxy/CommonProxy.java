package com.github.lunatrius.configpatcher.proxy;

import com.github.lunatrius.configpatcher.handler.ConfigurationHandler;
import com.github.lunatrius.configpatcher.patch.Generate;
import com.github.lunatrius.configpatcher.patch.Patch;
import com.github.lunatrius.configpatcher.reference.Reference;
import com.github.lunatrius.configpatcher.util.FileHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy {
    private File directoryMain = null;
    private File directoryOverride = null;
    private File directoryBase = null;
    private File directoryDiff = null;

    public void construct(FMLConstructionEvent event) {
        this.directoryMain = Loader.instance().getConfigDir();

        final File configFile = new File(this.directoryMain, Reference.MODID + ".cfg");
        ConfigurationHandler.init(configFile);

        this.directoryOverride = FileHelper.getCanonicalFile(this.directoryMain.getParentFile(), this.directoryMain.getName() + "-override");

        if (ConfigurationHandler.shouldPatch()) {
            new Patch(this.directoryMain, this.directoryOverride).execute();
        } else if (ConfigurationHandler.generate) {
            this.directoryBase = FileHelper.getCanonicalFile(this.directoryMain.getParentFile(), this.directoryMain.getName() + "-base");
            this.directoryDiff = FileHelper.getCanonicalFile(this.directoryOverride, "auto-generated");
        }
    }

    public void preInit(final FMLPreInitializationEvent event) {
    }

    public void init(final FMLInitializationEvent event) {
        if (ConfigurationHandler.generate) {
            new Generate(this.directoryMain, this.directoryBase, this.directoryDiff).execute();
        }
    }

    public void postInit(final FMLPostInitializationEvent event) {
    }
}
