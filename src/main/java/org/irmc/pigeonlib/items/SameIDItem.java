package org.irmc.pigeonlib.items;

import org.bukkit.inventory.ItemStack;

public interface SameIDItem {
    boolean isSimilarItem(ItemStack itemStack1, ItemStack itemStack2);
}
