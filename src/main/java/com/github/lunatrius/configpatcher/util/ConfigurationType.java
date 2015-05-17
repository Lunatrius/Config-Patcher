package com.github.lunatrius.configpatcher.util;

import com.github.lunatrius.configpatcher.reference.Reference;
import com.google.common.base.Strings;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public enum ConfigurationType {
    FORGE {
        @Override
        public void patchFile(final File fileTarget, final File fileSource) {
            Reference.logger.trace("Patching {}", FileHelper.getCanonicalFile(fileTarget));
            final Configuration configurationTarget = new Configuration(fileTarget);
            final Configuration configurationSource = new Configuration(fileSource);

            final Set<String> categoryNames = configurationSource.getCategoryNames();
            for (final String categoryName : categoryNames) {
                final ConfigCategory category = configurationSource.getCategory(categoryName);
                for (final Map.Entry<String, Property> entry : category.entrySet()) {
                    final String key = entry.getKey();
                    final Property propertySource = entry.getValue();
                    final Property propertyTarget = ConfigurationHelper.getPropertyFor(configurationTarget, categoryName, key, propertySource);

                    ConfigurationHelper.copyProperty(propertyTarget, propertySource);
                }
            }

            configurationTarget.save();
        }

        @Override
        public void generatePatch(final File fileMain, final File fileBase, final File fileDiff) {
            final Configuration configurationMain = new Configuration(fileMain);
            final Configuration configurationBase = new Configuration(fileBase);
            Configuration configurationDiff = null;

            final Set<String> categoryNames = configurationMain.getCategoryNames();
            for (final String categoryName : categoryNames) {
                final ConfigCategory categoryMain = configurationMain.getCategory(categoryName);
                for (final Map.Entry<String, Property> entry : categoryMain.entrySet()) {
                    final String key = entry.getKey();
                    final Property propertyMain = entry.getValue();
                    final Property propertyBase = ConfigurationHelper.getPropertyFor(configurationBase, categoryName, key, propertyMain);

                    if (!ConfigurationHelper.arePropertiesEqual(propertyMain, propertyBase)) {
                        if (configurationDiff == null) {
                            configurationDiff = new Configuration(fileDiff);
                        }

                        final String comment = !Strings.isNullOrEmpty(propertyBase.getString()) ? propertyBase.getString() : Arrays.toString(propertyBase.getStringList());
                        final Property propertyDiff = ConfigurationHelper.getPropertyFor(configurationDiff, categoryName, key, "Original: " + comment, propertyMain);
                        ConfigurationHelper.copyProperty(propertyDiff, propertyMain);
                    }
                }
            }

            if (configurationDiff != null && configurationDiff.hasChanged()) {
                Reference.logger.trace("Generated patch {}", fileDiff);
                configurationDiff.save();
            }
        }
    },
    UNKNOWN;

    public void patchFile(final File fileTarget, final File fileSource) {
    }

    public void generatePatch(final File fileMain, final File fileBase, final File fileDiff) {
    }
}
