package org.irmc.pigeonlib.enums;

import org.bukkit.Bukkit;

/**
 * This class represents a Minecraft version. It contains a major and minor version number, and provides methods to compare
 *
 * @author balugaq
 */
@SuppressWarnings("unused")
public enum MCVersion {
    MINECRAFT_1_21(21, 0),
    MINECRAFT_1_21_1(21, 1),
    MINECRAFT_1_21_2(21, 2),
    MINECRAFT_1_21_3(21, 3),
    MINECRAFT_1_21_4(21, 4),
    MINECRAFT_1_21_5(21, 5),
    MINECRAFT_1_21_6(21, 6),
    MINECRAFT_1_22(22, 0),
    MINECRAFT_1_22_1(22, 1),
    MINECRAFT_1_22_2(22, 2),
    MINECRAFT_1_22_3(22, 3),
    MINECRAFT_1_22_4(22, 4),
    MINECRAFT_1_22_5(22, 5),
    MINECRAFT_1_22_6(22, 6),
    UNKNOWN(-1, -1);

    private final int major;
    private final int minor;

    MCVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public static MCVersion of(int major, int minor) {
        for (MCVersion version : values()) {
            if (version.major == major && version.minor == minor) {
                return version;
            }
        }
        return UNKNOWN;
    }

    public static MCVersion getCurrentVersion() {
        String[] v = Bukkit.getMinecraftVersion().split("\\.");
        int major = Integer.parseInt(v[1]);
        int minor = Integer.parseInt(v[2]);
        return of(major, minor);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public boolean isAtLeast(MCVersion version) {
        return this.major > version.major || (this.major == version.major && this.minor >= version.minor);
    }

    public boolean isBelow(MCVersion version) {
        return this.major < version.major || (this.major == version.major && this.minor < version.minor);
    }

    public boolean isAtLeast(int major, int minor) {
        return this.major > major || (this.major == major && this.minor >= minor);
    }

    public boolean isBelow(int major, int minor) {
        return this.major < major || (this.major == major && this.minor < minor);
    }

    public boolean equals(int major, int minor) {
        return this.major == major && this.minor == minor;
    }

    public boolean equals(MCVersion version) {
        return this.major == version.major && this.minor == version.minor;
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}
