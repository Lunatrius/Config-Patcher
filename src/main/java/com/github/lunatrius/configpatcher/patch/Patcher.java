package com.github.lunatrius.configpatcher.patch;

import com.github.lunatrius.configpatcher.util.ConfigurationHelper;
import com.github.lunatrius.configpatcher.util.ConfigurationType;

import java.io.File;

public class Patcher {
    public void patchFile(final File fileMain, final File fileOverride) {
        final ConfigurationType typeOverride = ConfigurationHelper.getConfigurationType(fileOverride);
        final ConfigurationType typeMain = ConfigurationHelper.getConfigurationType(fileMain);

        if (!typeOverride.isCompatibleWith(typeMain)) {
            return;
        }

        typeOverride.patchFile(fileMain, fileOverride);
    }
}
