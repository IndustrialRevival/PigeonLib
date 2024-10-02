package org.irmc.pigeonlib.dict;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.irmc.pigeonlib.items.ItemUtils;
import org.irmc.pigeonlib.pdc.PersistentDataAPI;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * A dictionary system
 * @author balugaq
 */
@SuppressWarnings("unused")
public class Dictionary {
    private static final String PDC_KEY = "dictionary";
    private static final String DISABLED_MSG = "Dictionary is disabled";
    private static final String LOCKED_MSG = "Dictionary is locked";
    @Getter
    private final NamespacedKey key;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private boolean enabled;
    @Getter
    @Setter
    private boolean locked;
    public Dictionary(JavaPlugin plugin) {
        this.key = new NamespacedKey(plugin, PDC_KEY);
        this.name = key.getKey();
        this.enabled = true;
        DictionaryRegistry.registerDictionary(this);
    }
    public Dictionary(NamespacedKey key) {
        this.key = key;
        this.name = key.getKey();
        this.enabled = true;
        DictionaryRegistry.registerDictionary(this);
    }

    /**
     * Set of all keys in the dictionary
     */
    private final Set<String> keys = new HashSet<>();

    /**
     * Map Industry Revival ID -> Key Name
     * silver_ore -> ore
     * gold_ore -> ore
     */
    private final Map<String, String> dictionary = new HashMap<>();

    public void addDictMeta(@Nonnull ItemStack itemStack, @Nonnull String keyName) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        Validate.notEmpty(keyName, "keyName cannot be empty");
        checkEnabled();
        if (!keys.contains(keyName)) {
            throw new UnsupportedOperationException("Key " + keyName + " does not exist in dictionary");
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        PersistentDataAPI.set(meta, getKey(), PersistentDataType.STRING, keyName);
        itemStack.setItemMeta(meta);
    }

    public void setDictMeta(@Nonnull ItemStack itemStack, String keyName) {
        addDictMeta(itemStack, keyName);
    }

    public String getDictMeta(@Nonnull ItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        checkEnabled();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        return PersistentDataAPI.get(meta, getKey(), PersistentDataType.STRING);
    }

    public boolean hasDictMeta(@Nonnull ItemStack itemStack) {
        return getDictMeta(itemStack) != null;
    }

    public void removeDictMeta(@Nonnull ItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        checkEnabled();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        PersistentDataAPI.remove(meta, getKey());
        itemStack.setItemMeta(meta);
    }

    public void registerKey(String key) {
        checkEnabled();
        keys.add(key);
    }

    public void unregisterKey(String key) {
        checkEnabled();
        checkLocked();
        keys.remove(key);
        cleanInvalidKeys();
    }

    public boolean hasKey(String key) {
        checkEnabled();
        return keys.contains(key);
    }

    public void cleanInvalidKeys() {
        checkEnabled();
        checkLocked();
        keys.forEach(k -> {
            if (k == null || k.isEmpty()) {
                keys.remove(k);
            }
        });
        dictionary.forEach((irid, k) -> {
            if (k == null || k.isEmpty()) {
                dictionary.remove(irid);
            }
            else if (!keys.contains(k)) {
                dictionary.remove(irid);
            }
        });
    }

    public void checkEnabled() {
        if (!isEnabled()) {
            throw new UnsupportedOperationException(DISABLED_MSG);
        }
    }
    public void checkLocked() {
        if (isLocked()) {
            throw new UnsupportedOperationException(LOCKED_MSG);
        }
    }
}
