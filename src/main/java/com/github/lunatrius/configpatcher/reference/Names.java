package com.github.lunatrius.configpatcher.reference;

public final class Names {
    public static final class Config {
        public static final class Category {
            public static final String PATCHER = "patcher";
        }

        public static final String PATCH = "patch";
        public static final String PATCH_DESC = "Apply patch files on startup.";
        public static final String PATCH_ONCE = "patchOnce";
        public static final String PATCH_ONCE_DESC = "Apply patch files on startup once.";
        public static final String GENERATE = "generate";
        public static final String GENERATE_DESC = "Generate patch files on startup.";

        public static final String LANG_PREFIX = Reference.MODID.toLowerCase() + ".config";
    }
}
