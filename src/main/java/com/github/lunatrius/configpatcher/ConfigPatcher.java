package com.github.lunatrius.configpatcher;

import com.github.lunatrius.configpatcher.patch.Generate;
import com.github.lunatrius.configpatcher.patch.Patch;
import com.github.lunatrius.configpatcher.reference.Reference;
import com.github.lunatrius.configpatcher.util.FileHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

import java.io.File;
import java.util.Map;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-before:*")
public class ConfigPatcher {
    private File directoryMain = null;
    private File directoryOverride = null;
    private File directoryBase = null;
    private File directoryDiff = null;
    private boolean generatePatches = false;

    @NetworkCheckHandler
    public boolean checkModList(final Map<String, String> versions, final Side side) {
        return true;
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        this.directoryMain = FileHelper.getCanonicalFile(event.getModConfigurationDirectory());
        this.directoryOverride = FileHelper.getCanonicalFile(this.directoryMain.getParentFile(), this.directoryMain.getName() + "-override");
        this.generatePatches = new File(this.directoryMain.getParentFile(), "generate_patches.txt").exists();
        if (this.generatePatches) {
            this.directoryBase = FileHelper.getCanonicalFile(this.directoryMain.getParentFile(), this.directoryMain.getName() + "-base");
            this.directoryDiff = FileHelper.getCanonicalFile(this.directoryOverride, "auto-generated");
        } else {
            new Patch(this.directoryMain, this.directoryOverride).execute();
        }
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        if (this.generatePatches) {
            new Generate(this.directoryMain, this.directoryBase, this.directoryDiff).execute();
        }
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
}
