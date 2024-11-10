package org.irmc.pigeonlib.enums;

import io.papermc.lib.PaperLib;
import lombok.Getter;

@Getter
public enum MCVersion {
    MC1_21_3(21, 3),
    MC1_21_4(21, 4),
    UNKNOWN(-1, -1);

    public static final MCVersion CURRENT;

    static {
        int major = PaperLib.getMinecraftVersion();
        int minor = PaperLib.getMinecraftPatchVersion();
        CURRENT = MCVersion.getByInt(major, minor);
    }

    private final int major;
    private final int minor;

    MCVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public MCVersion getPrevious() {
        int index = ordinal() - 1;
        if (index < 0) {
            return UNKNOWN;
        }
        return values()[index];
    }

    public MCVersion getNext() {
        int index = ordinal() + 1;
        if (index >= values().length) {
            return UNKNOWN;
        }
        return values()[index];
    }

    public static MCVersion getByInt(int major, int minor) {
        for (MCVersion version : values()) {
            if (version.getMajor() == major && version.getMinor() == minor) {
                return version;
            }
        }

        return UNKNOWN;
    }

    public boolean atLeast(MCVersion version) {
        if (this.major > version.major) {
            return true;
        }
        return this.major == version.major && this.minor >= version.minor;
    }
}