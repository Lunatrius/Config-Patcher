package com.github.lunatrius.configpatcher.reference;

import java.util.ArrayList;
import java.util.List;

public class Blacklist {
    private static final List<String> BLACKLIST = new ArrayList<String>();

    public static boolean isBlacklisted(final String path, final String filename) {
        final boolean blacklisted = BLACKLIST.contains(path) || BLACKLIST.contains(filename) || BLACKLIST.contains(path + "/" + filename);
        if (blacklisted) {
            Reference.logger.trace("{}/{} is on the blacklist, skipping", path, filename);
        }

        return blacklisted;
    }

    static {
        BLACKLIST.add("./ConfigPatcher.cfg");
    }
}
