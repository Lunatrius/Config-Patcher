package com.github.lunatrius.configpatcher.patch;

import com.github.lunatrius.configpatcher.reference.Reference;
import com.github.lunatrius.configpatcher.util.FileHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class Patch {
    private final File directoryMain;
    private final File directoryOverride;

    private Patcher patcher;

    public Patch(final File directoryMain, final File directoryOverride) {
        this.directoryMain = directoryMain;
        this.directoryOverride = directoryOverride;

        this.patcher = new Patcher();
    }

    public void execute() {
        if (!this.directoryOverride.exists()) {
            FileHelper.mkdirs(this.directoryOverride);
        } else {
            final File[] directories = this.directoryOverride.listFiles(new FileFilter() {
                @Override
                public boolean accept(final File file) {
                    return file.isDirectory();
                }
            });

            if (directories != null) {
                Arrays.sort(directories);
                for (final File directory : directories) {
                    processDirectory(directory, ".");
                }
            }
        }
    }

    private void processDirectory(final File root, final String path) {
        Reference.logger.trace("Scanning {}", root);
        final File[] files = root.listFiles();
        if (files == null) {
            return;
        }

        Arrays.sort(files);

        for (final File file : files) {
            if (file.isFile()) {
                processFile(file, new File(this.directoryMain, path));
            }
        }

        for (final File file : files) {
            if (file.isDirectory()) {
                processDirectory(file, path + "/" + file.getName());
            }
        }
    }

    private void processFile(final File fileOverride, final File directoryMain) {
        if (!directoryMain.exists()) {
            FileHelper.mkdirs(directoryMain);
        }

        final File fileMain = new File(directoryMain, fileOverride.getName());
        this.patcher.patchFile(fileMain, fileOverride);
    }
}
