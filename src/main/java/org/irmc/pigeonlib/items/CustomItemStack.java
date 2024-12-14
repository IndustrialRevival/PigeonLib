package org.irmc.pigeonlib.items;

import com.google.common.collect.Multimap;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings("deprecation")
public class CustomItemStack implements Cloneable {
    private ItemStack bukkit;

    public CustomItemStack(Material material) {
        this(material, 1);
    }

    public CustomItemStack(Material material, int amount) {
        bukkit = ItemStack.of(material, amount);
    }

    public CustomItemStack(Material material, String displayName, String... lore) {
        bukkit = ItemStack.of(material);
        bukkit.editMeta(meta -> {
            if (displayName != null && !displayName.isBlank()) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            }

            if (lore != null && lore.length > 0) {
                List<String> loreList = new ArrayList<>(List.of(lore));
                loreList.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
                meta.setLore(loreList);
            }
        });
    }

    public CustomItemStack(Material material, Component displayName, Component... lore) {
        bukkit = ItemStack.of(material);
        bukkit.editMeta(meta -> {
            if (displayName != null) {
                meta.displayName(displayName);
            }

            if (lore != null && lore.length > 0) {
                meta.lore(new ArrayList<>(List.of(lore)));
            }
        });
    }

    public CustomItemStack(Material material, int amount, String displayName, String... lore) {
        bukkit = ItemStack.of(material, amount);
        bukkit.editMeta(meta -> {
            if (displayName != null && !displayName.isBlank()) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            }

            if (lore != null && lore.length > 0) {
                List<String> loreList = new ArrayList<>(List.of(lore));
                loreList.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
                meta.setLore(loreList);
            }
        });
    }

    public CustomItemStack(ItemStack itemStack) {
        bukkit = itemStack.clone();
    }

    public CustomItemStack(ItemStack itemStack, int amount) {
        bukkit = itemStack.clone();
        bukkit.setAmount(amount);
    }

    public CustomItemStack(ItemStack itemStack, String displayName, String... lore) {
        bukkit = itemStack.clone();
        bukkit.editMeta(meta -> {
            if (displayName != null && !displayName.isBlank()) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            }

            if (lore != null && lore.length > 0) {
                List<String> loreList = new ArrayList<>(List.of(lore));
                loreList.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
                meta.setLore(loreList);
            }
        });
    }

    public CustomItemStack(ItemStack itemStack, int amount, String displayName, String... lore) {
        bukkit = itemStack.clone();
        bukkit.setAmount(amount);
        bukkit.editMeta(meta -> {
            if (displayName != null && !displayName.isBlank()) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            }

            if (lore != null && lore.length > 0) {
                List<String> loreList = new ArrayList<>(List.of(lore));
                loreList.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
                meta.setLore(loreList);
            }
        });
    }

    public CustomItemStack(ItemStack itemStack, int amount, Component displayName, Component... lore) {
        bukkit = itemStack.clone();
        bukkit.setAmount(amount);
        bukkit.editMeta(meta -> {
            if (displayName != null) {
                meta.displayName(displayName);
            }

            if (lore != null && lore.length > 0) {
                meta.lore(new ArrayList<>(List.of(lore)));
            }
        });
    }

    public CustomItemStack setAmount(int amount) {
        bukkit.setAmount(amount);
        return this;
    }

    public CustomItemStack setDisplayName(String displayName) {
        bukkit.editMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName)));
        return this;
    }

    public CustomItemStack setLore(String... lore) {
        bukkit.editMeta(meta -> {
            List<String> loreList = new ArrayList<>(List.of(lore));
            loreList.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
            meta.setLore(loreList);
        });
        return this;
    }

    public CustomItemStack setLore(List<String> lore) {
        bukkit.editMeta(meta -> {
            List<String> loreList = new ArrayList<>(lore);
            loreList.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
            meta.setLore(loreList);
        });
        return this;
    }

    public CustomItemStack displayName(Component displayName) {
        bukkit.editMeta(meta -> meta.displayName(displayName));
        return this;
    }

    public CustomItemStack lore(Component... lore) {
        bukkit.editMeta(meta -> {
            List<Component> loreList = new ArrayList<>(List.of(lore));
            meta.lore(loreList);
        });
        return this;
    }

    public CustomItemStack lore(List<Component> lore) {
        bukkit.editMeta(meta -> {
            List<Component> loreList = new ArrayList<>(lore);
            meta.lore(loreList);
        });
        return this;
    }

    public CustomItemStack setRenderColor(Color color) {
        Material originalMaterial = bukkit.getType();
        bukkit = ItemStack.of(Material.FIREWORK_STAR, 1);
        bukkit.editMeta(FireworkEffectMeta.class, meta -> {
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            meta.setEffect(FireworkEffect.builder().withTrail().withColor(color).build());
            meta.setItemModel(originalMaterial.getKey());
        });
        return this;
    }

    public CustomItemStack addLore(String... lore) {
        bukkit.editMeta(meta -> {
            List<String> loreList = meta.getLore();
            if (loreList == null) {
                loreList = new ArrayList<>();
            }

            for (String s : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', s));
            }

            meta.setLore(loreList);
        });
        return this;
    }

    public CustomItemStack addItemFlags(ItemFlag... itemFlags) {
        bukkit.editMeta(meta -> meta.addItemFlags(itemFlags));
        return this;
    }

    public CustomItemStack removeItemFlags(ItemFlag... itemFlags) {
        bukkit.editMeta(meta -> meta.removeItemFlags(itemFlags));
        return this;
    }

    public CustomItemStack setItemModel(NamespacedKey key) {
        bukkit.editMeta(meta -> meta.setItemModel(key));
        return this;
    }

    public CustomItemStack setUnbreakable(boolean unbreakable) {
        bukkit.editMeta(meta -> meta.setUnbreakable(unbreakable));
        return this;
    }

    public CustomItemStack setCustomModel(int data) {
        bukkit.editMeta(meta -> meta.setCustomModelData(data));
        return this;
    }

    public CustomItemStack addEnchantment(Enchantment enchantment, int level) {
        bukkit.addEnchantment(enchantment, level);
        return this;
    }

    public CustomItemStack removeEnchantment(Enchantment enchantment) {
        bukkit.removeEnchantment(enchantment);
        return this;
    }

    public CustomItemStack setDurability(short durability) {
        bukkit.setDurability(durability);
        return this;
    }

    public CustomItemStack add() {
        bukkit.add();
        return this;
    }

    public CustomItemStack subtract() {
        bukkit.subtract();
        return this;
    }

    public CustomItemStack add(int amount) {
        bukkit.add(amount);
        return this;
    }

    public CustomItemStack subtract(int amount) {
        bukkit.subtract(amount);
        return this;
    }

    public CustomItemStack setFoodComponent(FoodComponent foodComponent) {
        bukkit.editMeta(meta -> meta.setFood(foodComponent));
        return this;
    }

    public CustomItemStack setCooldownComponent(UseCooldownComponent cooldownComponent) {
        bukkit.editMeta(meta -> meta.setUseCooldown(cooldownComponent));
        return this;
    }

    public CustomItemStack setEquippableComponent(EquippableComponent equippableComponent) {
        bukkit.editMeta(meta -> meta.setEquippable(equippableComponent));
        return this;
    }

    public CustomItemStack setToolComponent(ToolComponent toolComponent) {
        bukkit.editMeta(meta -> meta.setTool(toolComponent));
        return this;
    }

    public CustomItemStack setJukeboxPlayableComponent(JukeboxPlayableComponent component) {
        bukkit.editMeta(meta -> meta.setJukeboxPlayable(component));
        return this;
    }

    public CustomItemStack setCustomModelDataComponent(CustomModelDataComponent component) {
        bukkit.editMeta(meta -> meta.setCustomModelDataComponent(component));
        return this;
    }

    public CustomItemStack setUseRemainder(ItemStack remainder) {
        bukkit.editMeta(meta -> meta.setUseRemainder(remainder));
        return this;
    }

    public CustomItemStack setToolTipStyle(NamespacedKey key) {
        bukkit.editMeta(meta -> meta.setTooltipStyle(key));
        return this;
    }

    public CustomItemStack setHideTooltip(boolean hide) {
        bukkit.editMeta(meta -> meta.setHideTooltip(hide));
        return this;
    }

    public CustomItemStack setGlider(boolean glider) {
        bukkit.editMeta(meta -> meta.setGlider(glider));
        return this;
    }

    /**
     * Sets the damage resistant of the item.
     * @param tags the damage-resistant tags
     * @return the custom item stack itself
     *
     * @see org.bukkit.tag.DamageTypeTags
     */
    public CustomItemStack setDamageResistant(Tag<DamageType> tags) {
        bukkit.editMeta(meta -> meta.setDamageResistant(tags));
        return this;
    }

    public CustomItemStack setMaxStackSize(int size) {
        bukkit.editMeta(meta -> meta.setMaxStackSize(size));
        return this;
    }

    public CustomItemStack setRarity(ItemRarity rarity) {
        bukkit.editMeta(meta -> meta.setRarity(rarity));
        return this;
    }

    public CustomItemStack setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> map) {
        bukkit.editMeta(meta -> meta.setAttributeModifiers(map));
        return this;
    }

    public CustomItemStack addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        bukkit.editMeta(meta -> meta.addAttributeModifier(attribute, modifier));
        return this;
    }

    @Override
    public CustomItemStack clone() {
        return new CustomItemStack(bukkit.clone());
    }
}
