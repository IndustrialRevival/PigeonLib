package org.irmc.pigeonlib.world;


import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.irmc.pigeonlib.java.ReflectionUtil;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public class WorldUtils {
    protected static Class<?> craftBlockStateClass;
    protected static Field interfaceBlockDataField;
    protected static Field blockPositionField;
    protected static Field worldField;
    protected static Field weakWorldField;
    protected static boolean success = false;

    static {
        try {
            World sampleWorld = Bukkit.getWorlds().getFirst();
            BlockState blockstate = sampleWorld.getBlockAt(0, 0, 0).getState();
            var result = ReflectionUtil.getDeclaredFieldsRecursively(blockstate.getClass(), "data");
            interfaceBlockDataField = result.getFirstValue();
            interfaceBlockDataField.setAccessible(true);
            craftBlockStateClass = result.getSecondValue();
            blockPositionField = ReflectionUtil.getDeclaredFieldsRecursively(craftBlockStateClass, "position").getFirstValue();
            blockPositionField.setAccessible(true);
            worldField = ReflectionUtil.getDeclaredFieldsRecursively(craftBlockStateClass, "world").getFirstValue();
            worldField.setAccessible(true);
            weakWorldField = ReflectionUtil.getDeclaredFieldsRecursively(craftBlockStateClass, "weakWorld").getFirstValue();
            weakWorldField.setAccessible(true);
            success = true;
        } catch (Throwable ignored) {

        }
    }

    @CanIgnoreReturnValue
    public static boolean copyBlockState(@Nonnull BlockState fromBlockState, @Nonnull Block toBlock) {
        if (!success) {
            return false;
        }

        BlockState toState = toBlock.getState();
        if (!craftBlockStateClass.isInstance(toState) || !craftBlockStateClass.isInstance(fromBlockState)) {
            return false;
        }

        try {
            blockPositionField.set(fromBlockState, blockPositionField.get(toState));
            worldField.set(fromBlockState, worldField.get(toState));
            weakWorldField.set(fromBlockState, weakWorldField.get(toState));
            fromBlockState.update(true, false);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static String locationToString(@Nonnull Location l) {
        if (l.getWorld() == null) {
            return "Unknown Location";
        }
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public static String locationToStringExactly(@Nonnull Location l) {
        if (l.getWorld() == null) {
            return "Unknown Location";
        }

        return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
    }

    public static void doWorldEdit(@Nonnull Location pos1, @Nonnull Location pos2, @Nonnull Consumer<Location> consumer) {
        final int downX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        final int upX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        final int downY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        final int upY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        final int downZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        final int upZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    consumer.accept(new Location(pos1.getWorld(), x, y, z));
                }
            }
        }
    }

    public static long getRange(@Nonnull Location pos1, @Nonnull Location pos2) {
        final int downX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        final int upX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        final int downY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        final int upY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        final int downZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        final int upZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        return (long) (Math.abs(upX - downX) + 1) * (Math.abs(upY - downY) + 1) * (Math.abs(upZ - downZ) + 1);
    }
}