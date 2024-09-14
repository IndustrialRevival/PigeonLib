package org.irmc.pigeonlib.items;

import org.bukkit.inventory.ItemStack;
import org.irmc.pigeonlib.dict.Dictionary;
import org.irmc.pigeonlib.dict.DictionaryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DictionaryItem {
    @Nonnull Dictionary getDictionary();
    @Nonnull String getKeyName();
    @Nullable
    static DictionaryItem fromItemStack(@Nonnull ItemStack itemStack) {
        return DictionaryUtil.getDictionaryItem(itemStack);
    }
}
