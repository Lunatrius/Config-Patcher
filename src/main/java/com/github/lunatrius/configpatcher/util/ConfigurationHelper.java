package com.github.lunatrius.configpatcher.util;

import com.github.lunatrius.configpatcher.reference.Reference;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

public final class ConfigurationHelper {
    private static final Field fieldFile = ReflectionHelper.findField(Configuration.class, "file");

    public static ConfigurationType getConfigurationType(final File file) {
        if (!file.exists()) {
            return ConfigurationType.NONEXISTENT;
        }

        if (isForgeConfiguration(file)) {
            return ConfigurationType.FORGE;
        }

        return ConfigurationType.UNKNOWN;
    }

    public static boolean isForgeConfiguration(final File file) {
        final Configuration configuration = newInstance(null);
        if (!setConfigurationFile(configuration, file)) {
            return false;
        }

        try {
            configuration.load();
        } catch (final Throwable t) {
            return false;
        }

        return true;
    }

    public static Configuration newInstance(final File file) {
        return newInstance(file, false);
    }

    public static Configuration newInstance(final File file, final boolean empty) {
        if (file == null || !file.exists()) {
            final Configuration configuration = new Configuration();
            setConfigurationFile(configuration, file);
            return configuration;
        }

        final Configuration configuration = new Configuration(file, true);
        if (empty) {
            for (String category : configuration.getCategoryNames()) {
                configuration.removeCategory(configuration.getCategory(category));
            }
        }

        return configuration;
    }

    public static boolean setConfigurationFile(final Configuration configuration, final File file) {
        try {
            fieldFile.set(configuration, file);
        } catch (final Exception e) {
            Reference.logger.error("Could not set the 'file' field!", e);
        }

        return true;
    }

    public static Property getPropertyFor(final Configuration configuration, final String categoryName, final String key, final Property prop) {
        return getPropertyFor(configuration, categoryName, key, prop.comment, prop);
    }

    public static Property getPropertyFor(final Configuration configuration, final String categoryName, final String key, final String comment, final Property prop) {
        if (prop.isList()) {
            return configuration.get(categoryName, key, new String[] { "<config patcher dummy value>" }, comment, prop.getType());
        }

        return configuration.get(categoryName, key, "<config patcher dummy value>", comment, prop.getType());
    }

    public static boolean arePropertiesEqual(final Property propertyA, final Property propertyB) {
        if (propertyA.isIntValue() && propertyB.isIntValue()) {
            return propertyA.getInt() == propertyB.getInt();
        } else if (propertyA.isIntList() && propertyB.isIntList()) {
            return Arrays.equals(propertyA.getIntList(), propertyB.getIntList());
        } else if (propertyA.isDoubleValue() && propertyB.isDoubleValue()) {
            return propertyA.getDouble() == propertyB.getDouble();
        } else if (propertyA.isDoubleList() && propertyB.isDoubleList()) {
            return Arrays.equals(propertyA.getDoubleList(), propertyB.getDoubleList());
        } else if (propertyA.isBooleanValue() && propertyB.isBooleanValue()) {
            return propertyA.getBoolean() == propertyB.getBoolean();
        } else if (propertyA.isBooleanList() && propertyB.isBooleanList()) {
            return Arrays.equals(propertyA.getBooleanList(), propertyB.getBooleanList());
        } else if (!propertyA.isList() && !propertyB.isList()) {
            return propertyA.getString().equals(propertyB.getString());
        } else if (propertyA.isList() && propertyB.isList()) {
            return Arrays.equals(propertyA.getStringList(), propertyB.getStringList());
        }

        return false;
    }

    public static void copyProperty(final Property propertyTarget, final Property propertySource) {
        if (propertySource.isIntValue()) {
            propertyTarget.set(propertySource.getInt());
        } else if (propertySource.isIntList()) {
            propertyTarget.set(propertySource.getIntList());
        } else if (propertySource.isDoubleValue()) {
            propertyTarget.set(propertySource.getDouble());
        } else if (propertySource.isDoubleList()) {
            propertyTarget.set(propertySource.getDoubleList());
        } else if (propertySource.isBooleanValue()) {
            propertyTarget.set(propertySource.getBoolean());
        } else if (propertySource.isBooleanList()) {
            propertyTarget.set(propertySource.getBooleanList());
        } else if (!propertySource.isList()) {
            propertyTarget.set(propertySource.getString());
        } else {
            propertyTarget.set(propertySource.getStringList());
        }
    }
}
