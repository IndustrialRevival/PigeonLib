package org.irmc.pigeonlib.dict;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.irmc.pigeonlib.items.DictionaryItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@UtilityClass
public class DictionaryUtil {
    public static boolean isDictionaryItem(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof DictionaryItem;
    }

    public static boolean isDictionary(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Dictionary;
    }

    @Nullable
    public static DictionaryItem getDictionaryItem(@Nonnull ItemStack itemStack) {
        if (!isDictionaryItem(itemStack)) {
            return null;
        }
        return (DictionaryItem) itemStack;
    }

    @Nullable
    public static Dictionary getDictionary(@Nonnull ItemStack itemStack) {
        if (!isDictionaryItem(itemStack)) {
            return null;
        }
        return ((DictionaryItem) itemStack).getDictionary();
    }

    @Nullable
    public static String getKey(@Nonnull ItemStack itemStack) {
        if (!isDictionaryItem(itemStack)) {
            return null;
        }
        return ((DictionaryItem) itemStack).getKeyName();
    }

    public static void setKey(@Nonnull ItemStack itemStack, String key) {
        setKey(itemStack, key, true);
    }
    public static void setKey(@Nonnull ItemStack itemStack, String key, boolean checkExists) {
        if (!isDictionaryItem(itemStack)) {
            return;
        }

        Dictionary dictionary = ((DictionaryItem) itemStack).getDictionary();
        if (dictionary == null || !dictionary.isEnabled() || dictionary.isLocked()) {
            return;
        }

        if (checkExists && !dictionary.hasKey(key)) {
            dictionary.registerKey(key);
        }

        dictionary.setDictMeta(itemStack, key);
    }

    public static void removeKey(@Nonnull ItemStack itemStack, boolean cleanDictionary) {
        if (!isDictionaryItem(itemStack)) {
            return;
        }
        Dictionary dictionary = ((DictionaryItem) itemStack).getDictionary();
        if (dictionary == null || !dictionary.isEnabled() || dictionary.isLocked()) {
            return;
        }
        dictionary.removeDictMeta(itemStack);
        if (cleanDictionary) {
            dictionary.cleanInvalidKeys();
        }
    }

    public static boolean hasKey(@Nonnull ItemStack itemStack) {
        if (!isDictionaryItem(itemStack)) {
            return false;
        }
        Dictionary dictionary = ((DictionaryItem) itemStack).getDictionary();
        if (dictionary == null || !dictionary.isEnabled()) {
            return false;
        }
        return dictionary.hasDictMeta(itemStack);
    }
}
