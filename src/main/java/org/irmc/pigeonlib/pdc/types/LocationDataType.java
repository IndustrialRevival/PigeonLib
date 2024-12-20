package org.irmc.pigeonlib.pdc.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.irmc.pigeonlib.world.WorldUtils;
import org.jetbrains.annotations.NotNull;

class LocationDataType implements PersistentDataType<String, Location> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<Location> getComplexType() {
        return Location.class;
    }

    @Override
    public @NotNull String toPrimitive(
            @NotNull Location location, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return WorldUtils.locationToStringExactly(location);
    }

    @Override
    public @NotNull Location fromPrimitive(
            @NotNull String string, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        String[] split = string.split(",");
        String worldName = split[0];
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
