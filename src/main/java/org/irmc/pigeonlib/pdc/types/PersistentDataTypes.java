package org.irmc.pigeonlib.pdc.types;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PersistentDataTypes {
    public static final PersistentDataType<String, Location> LOCATION = new LocationDataType();
    public static final PersistentDataType<String, UUID> UUID = new UUIDDataType();
    public static final PersistentDataType<byte[], ItemStack> ITEM_STACK = new ItemStackDataType();
    public static final PersistentDataType<String, NamespacedKey> NAMESPACED_KEY = new NamespacedKeyDataType();

    private PersistentDataTypes() {}
}
