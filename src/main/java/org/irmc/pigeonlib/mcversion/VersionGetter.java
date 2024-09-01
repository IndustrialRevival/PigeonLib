package org.irmc.pigeonlib.mcversion;

import io.papermc.lib.PaperLib;

public class VersionGetter {
    private static MCVersion mcVersion;

    static {
        int major = PaperLib.getMinecraftVersion();
        int minor = PaperLib.getMinecraftPatchVersion();
        mcVersion = mcVersion.getByInt(major, minor);
    }

    public static MCVersion getVersion() {
        return mcVersion;
    }
}
