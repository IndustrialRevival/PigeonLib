package org.irmc.pigeonlib.pdc.types;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PersistentDataTypes {
    public static final PersistentDataType<String, Location> LOCATION = new LocationDataType();
    public static final PersistentDataType<String, UUID> UUID = new UUIDDataType();

    private PersistentDataTypes() {}
}
