package com.github.lunatrius.configpatcher.util;

import com.github.lunatrius.configpatcher.reference.Reference;

import java.io.File;
import java.io.IOException;

public class FileHelper {
    public static File getCanonicalFile(final File file) {
        try {
            return file.getCanonicalFile();
        } catch (final IOException ioe) {
        }
        return file;
    }

    public static File getCanonicalFile(final File root, final String filename) {
        return getCanonicalFile(new File(root, filename));
    }

    public static void mkdirs(final File file) {
        if (!file.mkdirs()) {
            Reference.logger.error("Could not create directory: {}", getCanonicalFile(file));
        }
    }
}
