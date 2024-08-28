package org.irmc.pigeonLib.pdc;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.irmc.pigeonLib.pdc.types.PersistentDataTypes;
import org.jetbrains.annotations.Nullable;

/**
 * This class provides a simple API for accessing persistent data containers on Bukkit objects.
 * For more persistent data types, see {@link PersistentDataTypes}
 */
public class PersistentDataAPI {
    private PersistentDataAPI() {}

    public static int getInt(PersistentDataHolder holder, NamespacedKey key) {
        return getInt(holder, key, 0);
    }

    public static int getInt(PersistentDataHolder holder, NamespacedKey key, int def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, def);
    }

    public static void setInt(PersistentDataHolder holder, NamespacedKey key, int value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
    }

    public static boolean getBoolean(PersistentDataHolder holder, NamespacedKey key) {
        return getBoolean(holder, key, false);
    }

    public static boolean getBoolean(PersistentDataHolder holder, NamespacedKey key, boolean def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, def);
    }

    public static void setBoolean(PersistentDataHolder holder, NamespacedKey key, boolean value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, value);
    }

    public static String getString(PersistentDataHolder holder, NamespacedKey key) {
        return getString(holder, key, "");
    }

    public static String getString(PersistentDataHolder holder, NamespacedKey key, String def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, def);
    }

    public static void setString(PersistentDataHolder holder, NamespacedKey key, String value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    public static double getDouble(PersistentDataHolder holder, NamespacedKey key) {
        return getDouble(holder, key, 0.0);
    }

    public static double getDouble(PersistentDataHolder holder, NamespacedKey key, double def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, def);
    }

    public static void setDouble(PersistentDataHolder holder, NamespacedKey key, double value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public static long getLong(PersistentDataHolder holder, NamespacedKey key) {
        return getLong(holder, key, 0L);
    }

    public static long getLong(PersistentDataHolder holder, NamespacedKey key, long def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LONG, def);
    }

    public static void setLong(PersistentDataHolder holder, NamespacedKey key, long value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.LONG, value);
    }

    public static short getShort(PersistentDataHolder holder, NamespacedKey key) {
        return getShort(holder, key, (short) 0);
    }

    public static short getShort(PersistentDataHolder holder, NamespacedKey key, short def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.SHORT, def);
    }

    public static void setShort(PersistentDataHolder holder, NamespacedKey key, short value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.SHORT, value);
    }

    public static byte getByte(PersistentDataHolder holder, NamespacedKey key) {
        return getByte(holder, key, (byte) 0);
    }

    public static byte getByte(PersistentDataHolder holder, NamespacedKey key, byte def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE, def);
    }

    public static void setByte(PersistentDataHolder holder, NamespacedKey key, byte value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.BYTE, value);
    }

    public static float getFloat(PersistentDataHolder holder, NamespacedKey key) {
        return getFloat(holder, key, 0.0f);
    }

    public static float getFloat(PersistentDataHolder holder, NamespacedKey key, float def) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.FLOAT, def);
    }

    public static void setFloat(PersistentDataHolder holder, NamespacedKey key, float value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
    }

    @Nullable public static Location getLocation(PersistentDataHolder holder, NamespacedKey key) {
        return getLocation(holder, key, null);
    }

    @Nullable public static Location getLocation(PersistentDataHolder holder, NamespacedKey key, @Nullable Location def) {
        if (def == null) {
            return holder.getPersistentDataContainer().get(key, PersistentDataTypes.LOCATION);
        }
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataTypes.LOCATION, def);
    }

    public static void setLocation(PersistentDataHolder holder, NamespacedKey key, Location value) {
        holder.getPersistentDataContainer().set(key, PersistentDataTypes.LOCATION, value);
    }

    public static <T, Z> void set(PersistentDataHolder holder, NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        holder.getPersistentDataContainer().set(key, type, value);
    }

    public static <T, Z> Z get(PersistentDataHolder holder, NamespacedKey key, PersistentDataType<T, Z> type) {
        return holder.getPersistentDataContainer().get(key, type);
    }

    public static <T, Z> Z getOrDefault(PersistentDataHolder holder, NamespacedKey key, PersistentDataType<T, Z> type, Z def) {
        return holder.getPersistentDataContainer().getOrDefault(key, type, def);
    }

    public static <T, Z> boolean has(PersistentDataHolder holder, NamespacedKey key, PersistentDataType<T, Z> type) {
        return holder.getPersistentDataContainer().has(key, type);
    }

    public static <T, Z> void remove(PersistentDataHolder holder, NamespacedKey key) {
        holder.getPersistentDataContainer().remove(key);
    }
}