package com.github.lunatrius.configpatcher.patch;

import com.github.lunatrius.configpatcher.reference.Blacklist;
import com.github.lunatrius.configpatcher.reference.Reference;
import com.github.lunatrius.configpatcher.util.ConfigurationHelper;
import com.github.lunatrius.configpatcher.util.ConfigurationType;
import com.github.lunatrius.configpatcher.util.FileHelper;

import java.io.File;
import java.util.Arrays;

public class Generate {
    private final File directoryMain;
    private final File directoryBase;
    private final File directoryDiff;

    private final Patcher patcher;

    public Generate(final File directoryMain, final File directoryBase, final File directoryDiff) {
        this.directoryMain = directoryMain;
        this.directoryBase = directoryBase;
        this.directoryDiff = directoryDiff;

        this.patcher = new Patcher();
    }

    public void execute() {
        if (!this.directoryBase.exists()) {
            FileHelper.mkdirs(this.directoryBase);

            processDirectoryCopy(this.directoryMain, ".");
        } else {
            if (this.directoryDiff.exists()) {
                if (!this.directoryDiff.delete()) {
                    Reference.logger.error("Could not delete {}", this.directoryDiff);
                }
            }

            FileHelper.mkdirs(this.directoryDiff);

            processDirectoryGenerate(this.directoryMain, ".");
        }
    }

    private void processDirectoryCopy(final File directory, final String path) {
        final File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        Arrays.sort(files);

        for (final File file : files) {
            if (file.isFile()) {
                if (Blacklist.isBlacklisted(path, file.getName())) {
                    continue;
                }

                processFileCopy(file, new File(this.directoryBase, path));
            }
        }

        for (final File file : files) {
            if (file.isDirectory()) {
                if (Blacklist.isBlacklisted(path, file.getName())) {
                    continue;
                }

                processDirectoryCopy(file, path + "/" + file.getName());
            }
        }
    }

    private void processFileCopy(final File fileOverride, final File directoryBase) {
        if (!directoryBase.exists()) {
            FileHelper.mkdirs(directoryBase);
        }

        final File fileBase = new File(directoryBase, fileOverride.getName());
        this.patcher.patchFile(fileBase, fileOverride);
    }

    private void processDirectoryGenerate(final File directoryMain, final String path) {
        final File[] files = directoryMain.listFiles();
        if (files == null) {
            return;
        }

        Arrays.sort(files);

        for (final File file : files) {
            if (file.isFile()) {
                if (Blacklist.isBlacklisted(path, file.getName())) {
                    continue;
                }

                processFileGenerate(file, new File(this.directoryBase, path), new File(this.directoryDiff, path));
            }
        }

        for (final File file : files) {
            if (file.isDirectory()) {
                if (Blacklist.isBlacklisted(path, file.getName())) {
                    continue;
                }

                processDirectoryGenerate(file, path + "/" + file.getName());
            }
        }
    }

    private void processFileGenerate(final File fileMain, final File directoryBase, final File directoryDiff) {
        final File fileBase = new File(directoryBase, fileMain.getName());
        final File fileDiff = new File(directoryDiff, fileMain.getName());

        final ConfigurationType typeMain = ConfigurationHelper.getConfigurationType(fileMain);
        final ConfigurationType typeBase = ConfigurationHelper.getConfigurationType(fileBase);
        if (typeMain != typeBase) {
            return;
        }

        typeMain.generatePatch(fileMain, fileBase, fileDiff);
    }
}
