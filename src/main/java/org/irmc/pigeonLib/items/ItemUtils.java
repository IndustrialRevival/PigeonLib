package org.irmc.pigeonLib.items;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Contract;

public final class ItemUtils {
    private ItemUtils() {}

    @Contract("null,_ -> null")
    public static ItemStack getCleanedItem(@Nullable ItemStack item, @Nullable Consumer<ItemMeta> additionalSettings) {
        if (item == null) {
            return null;
        }

        ItemStack baseItem = new ItemStack(item.getType(), item.getAmount());
        ItemMeta meta = baseItem.getItemMeta();

        ItemMeta originalMeta = item.getItemMeta();

        if (originalMeta.hasCustomModelData()) {
            meta.setCustomModelData(originalMeta.getCustomModelData());
        }

        if (originalMeta.hasDisplayName()) {
            meta.displayName(originalMeta.displayName());
        }
        if (originalMeta.hasLore()) {
            meta.lore(originalMeta.lore());
        }

        originalMeta.getItemFlags().forEach(meta::addItemFlags);

        meta.setUnbreakable(originalMeta.isUnbreakable());
        item.getEnchantments().forEach(baseItem::addEnchantment);

        if (additionalSettings != null) {
            additionalSettings.accept(meta);
        }

        baseItem.setItemMeta(meta);

        return item;
    }

    /**
     * This method compares two instances of {@link ItemStack} and checks
     * whether their {@link Material} and {@link ItemMeta} match.
     *
     * @param a
     *            {@link ItemStack} One
     * @param b
     *            {@link ItemStack} Two
     * @return Whether the two instances of {@link ItemStack} are similar and can be stacked.
     */
    public static boolean isItemSimilar(@Nullable ItemStack a, @Nullable ItemStack b) {
        return isItemSimilar(a, b, true, true);
    }

    /**
     * This method compares two instances of {@link ItemStack} and checks
     * @param item1 item one
     * @param item2 item two
     * @param checkLore Whether to check lore
     * @return Whether the two instances of {@link ItemStack} are similar and can be stacked.
     */
    public static boolean isItemSimilar(ItemStack item1, ItemStack item2, boolean checkLore) {
        return isItemSimilar(item1, item2, checkLore, true);
    }

    /**
     * This method compares two instances of {@link ItemStack} and checks
     * @param item1 item one
     * @param item2 item two
     * @param checkLore Whether to check lore
     * @param checkAmount Whether to check amount
     * @return Whether the two instances of {@link ItemStack} are similar and can be stacked.
     */
    public static boolean isItemSimilar(ItemStack item1, ItemStack item2, boolean checkLore, boolean checkAmount) {
        if (item1 == null || item2 == null) {
            return item1 == item2;
        }

        if (item1.getType() != item2.getType()) {
            return false;
        }

        if (checkAmount && item1.getAmount() < item2.getAmount()) {
            return false;
        }

        if (item1.hasItemMeta() != item2.hasItemMeta()) {
            return false;
        }

        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();

        if (meta1.hasDisplayName()
                && meta2.hasDisplayName()
                && !Objects.equals(meta1.displayName(), meta2.displayName())) {
            return false;
        }

        if (meta1.hasCustomModelData() != meta2.hasCustomModelData()) {
            return false;
        } else {
            if (meta1.getCustomModelData() != meta2.getCustomModelData()) {
                return false;
            }
        }

        if (!item1.getEnchantments().equals(item2.getEnchantments())) {
            return false;
        }

        if (metaNotEquals(meta1, meta2)) {
            return false;
        }

        PersistentDataContainer pdc1 = meta1.getPersistentDataContainer();
        PersistentDataContainer pdc2 = meta2.getPersistentDataContainer();

        if (pdc1.getKeys().size() != pdc2.getKeys().size()) {
            return false;
        }

        if (!pdc1.equals(pdc2)) {
            return false;
        }

        if (checkLore) {
            List<Component> lore1 = meta1.lore();
            List<Component> lore2 = meta2.lore();

            if (lore1 == null || lore2 == null) {
                return lore1 == lore2;
            }

            if (lore1.size() != lore2.size()) {
                return false;
            }

            for (int i = 0; i < lore1.size(); i++) {
                if (!Objects.equals(lore1.get(i), lore2.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    private static boolean metaNotEquals(ItemMeta meta1, ItemMeta meta2) {
        if (meta1 == null || meta2 == null) {
            return meta1 != meta2;
        }

        if (meta1 instanceof Damageable instanceOne && meta2 instanceof Damageable instanceTwo) {
            if (instanceOne.getDamage() != instanceTwo.getDamage()) {
                return true;
            }
        }

        // Axolotl
        if (meta1 instanceof AxolotlBucketMeta instanceOne && meta2 instanceof AxolotlBucketMeta instanceTwo) {
            if (instanceOne.hasVariant() != instanceTwo.hasVariant()) {
                return true;
            }

            if (!instanceOne.hasVariant() || !instanceTwo.hasVariant()) return true;

            if (instanceOne.getVariant() != instanceTwo.getVariant()) {
                return true;
            }
        }

        // Banner
        if (meta1 instanceof BannerMeta instanceOne && meta2 instanceof BannerMeta instanceTwo) {
            if (!instanceOne.getPatterns().equals(instanceTwo.getPatterns())) {
                return true;
            }
        }

        // Books
        if (meta1 instanceof BookMeta instanceOne && meta2 instanceof BookMeta instanceTwo) {
            if (instanceOne.getPageCount() != instanceTwo.getPageCount()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getAuthor(), instanceTwo.getAuthor())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getTitle(), instanceTwo.getTitle())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getGeneration(), instanceTwo.getGeneration())) {
                return true;
            }
        }

        // Compass
        if (meta1 instanceof CompassMeta instanceOne && meta2 instanceof CompassMeta instanceTwo) {
            if (instanceOne.isLodestoneTracked() != instanceTwo.isLodestoneTracked()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getLodestone(), instanceTwo.getLodestone())) {
                return true;
            }
        }

        // Crossbow
        if (meta1 instanceof CrossbowMeta instanceOne && meta2 instanceof CrossbowMeta instanceTwo) {
            if (instanceOne.hasChargedProjectiles() != instanceTwo.hasChargedProjectiles()) {
                return true;
            }
            if (!instanceOne.getChargedProjectiles().equals(instanceTwo.getChargedProjectiles())) {
                return true;
            }
        }

        // Enchantment Storage
        if (meta1 instanceof EnchantmentStorageMeta instanceOne
                && meta2 instanceof EnchantmentStorageMeta instanceTwo) {
            if (instanceOne.hasStoredEnchants() != instanceTwo.hasStoredEnchants()) {
                return true;
            }
            if (!instanceOne.getStoredEnchants().equals(instanceTwo.getStoredEnchants())) {
                return true;
            }
        }

        // Firework Star
        if (meta1 instanceof FireworkEffectMeta instanceOne && meta2 instanceof FireworkEffectMeta instanceTwo) {
            if (!Objects.equals(instanceOne.getEffect(), instanceTwo.getEffect())) {
                return true;
            }
        }

        // Firework
        if (meta1 instanceof FireworkMeta instanceOne && meta2 instanceof FireworkMeta instanceTwo) {
            if (instanceOne.getPower() != instanceTwo.getPower()) {
                return true;
            }
            if (!instanceOne.getEffects().equals(instanceTwo.getEffects())) {
                return true;
            }
        }

        // Leather Armor
        if (meta1 instanceof LeatherArmorMeta instanceOne && meta2 instanceof LeatherArmorMeta instanceTwo) {
            if (!instanceOne.getColor().equals(instanceTwo.getColor())) {
                return true;
            }
        }

        // Maps
        if (meta1 instanceof MapMeta instanceOne && meta2 instanceof MapMeta instanceTwo) {
            if (instanceOne.hasMapView() != instanceTwo.hasMapView()) {
                return true;
            }
            if (instanceOne.hasLocationName() != instanceTwo.hasLocationName()) {
                return true;
            }
            if (instanceOne.hasColor() != instanceTwo.hasColor()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getMapView(), instanceTwo.getMapView())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getLocationName(), instanceTwo.getLocationName())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getColor(), instanceTwo.getColor())) {
                return true;
            }
        }

        // Potion
        if (meta1 instanceof PotionMeta instanceOne && meta2 instanceof PotionMeta instanceTwo) {
            if (!instanceOne.getBasePotionData().equals(instanceTwo.getBasePotionData())) {
                return true;
            }
            if (instanceOne.hasCustomEffects() != instanceTwo.hasCustomEffects()) {
                return true;
            }
            if (instanceOne.hasColor() != instanceTwo.hasColor()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getColor(), instanceTwo.getColor())) {
                return true;
            }
            if (!instanceOne.getCustomEffects().equals(instanceTwo.getCustomEffects())) {
                return true;
            }
        }

        // Skull
        if (meta1 instanceof SkullMeta instanceOne && meta2 instanceof SkullMeta instanceTwo) {
            if (instanceOne.hasOwner() != instanceTwo.hasOwner()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getOwningPlayer(), instanceTwo.getOwningPlayer())) {
                return true;
            }
        }

        // Stew
        if (meta1 instanceof SuspiciousStewMeta instanceOne && meta2 instanceof SuspiciousStewMeta instanceTwo) {
            if (!Objects.equals(instanceOne.getCustomEffects(), instanceTwo.getCustomEffects())) {
                return true;
            }
        }

        // Fish Bucket
        if (meta1 instanceof TropicalFishBucketMeta instanceOne
                && meta2 instanceof TropicalFishBucketMeta instanceTwo) {
            if (instanceOne.hasVariant() != instanceTwo.hasVariant()) {
                return true;
            }
            if (!instanceOne.getPattern().equals(instanceTwo.getPattern())) {
                return true;
            }
            if (!instanceOne.getBodyColor().equals(instanceTwo.getBodyColor())) {
                return true;
            }
            return !instanceOne.getPatternColor().equals(instanceTwo.getPatternColor());
        }

        // Bundle
        if (meta1 instanceof BundleMeta instanceOne && meta2 instanceof BundleMeta instanceTwo) {
            if (instanceOne.hasItems() != instanceTwo.hasItems()) {
                return true;
            }
            return !instanceOne.getItems().equals(instanceTwo.getItems());
        }

        return false;
    }

    /**
     * This method damages the specified Item by the given amount.
     * If ignoredEnchantments is set to false, it will factor in the "Unbreaking" Enchantment.
     *
     * @param item
     *            The Item to damage
     * @param damage
     *            The amount of damage to apply
     * @param ignoreEnchantments
     *            Whether the Unbreaking Enchantment should be ignored
     */
    public static void damageItem(@Nonnull ItemStack item, int damage, boolean ignoreEnchantments) {
        if (item.getType() != Material.AIR && item.getAmount() > 0) {
            int remove = damage;

            if (!ignoreEnchantments && item.getEnchantments().containsKey(Enchantment.DURABILITY)) {
                int level = item.getEnchantmentLevel(Enchantment.DURABILITY);

                for (int i = 0; i < damage; i++) {
                    if (Math.random() * 100 <= (60 + Math.floorDiv(40, (level + 1)))) {
                        remove--;
                    }
                }
            }

            ItemMeta meta = item.getItemMeta();
            Damageable damageable = (Damageable) meta;

            if (damageable.getDamage() + remove > item.getType().getMaxDurability()) {
                item.setAmount(0);
            } else {
                damageable.setDamage(damageable.getDamage() + remove);
                item.setItemMeta(meta);
            }
        }
    }

    /**
     * This Method will consume the Item in the specified slot.
     * See {@link ItemUtils#consumeItem(ItemStack, int, boolean)} for further details.
     *
     * @param item
     *            The Item to consume
     * @param amount
     *            The number of items to consume
     */
    public static void consumeItem(@Nonnull ItemStack item, int amount) {
        consumeItem(item, amount, true);
    }

    /**
     * This Method consumes a specified number of items from the
     * specified slot.
     * <p>
     * The items will be removed from the slot, if the slot does not hold enough items,
     * it will be replaced with null.
     * Note that this does not check whether there are enough Items present,
     * if you specify a bigger amount than present, it will simply set the Item to null.
     * <p>
     * If replaceConsumables is true, the following things will not be replaced with 'null':
     * {@code Buckets -> new ItemStack(Material.BUCKET)}
     * {@code Potions -> new ItemStack(Material.GLASS_BOTTLE)}
     *
     * @param item
     *            The Item to consume
     * @param amount
     *            The number of items to consume
     * @param replaceConsumables
     *            Whether Items should be replaced with their "empty" version
     */
    public static void consumeItem(@Nonnull ItemStack item, int amount, boolean replaceConsumables) {
        if (item.getType() != Material.AIR && item.getAmount() > 0) {
            if (replaceConsumables) {
                switch (item.getType()) {
                    case POTION:
                    case DRAGON_BREATH:
                    case HONEY_BOTTLE:
                        item.setType(Material.GLASS_BOTTLE);
                        item.setAmount(1);
                        return;
                    case WATER_BUCKET:
                    case LAVA_BUCKET:
                    case MILK_BUCKET:
                        item.setType(Material.BUCKET);
                        item.setAmount(1);
                        return;
                    default:
                        break;
                }
            }

            if (item.getAmount() <= amount) {
                item.setAmount(0);
            } else {
                item.setAmount(item.getAmount() - amount);
            }
        }
    }

    public static ItemStack cloneItem(ItemStack it, int amount) {
        ItemStack it1 = it.clone();
        it1.setAmount(amount);
        return it1;
    }

    /**
     * This method adds a line of lore to the specified item.
     * @param item the item
     * @param lore the line of lore to add
     * @param appendEmptyLine whether to append an empty line before the line of lore (won't append if the item has no lore)
     */
    public static void addLore(ItemStack item, Component lore, boolean appendEmptyLine) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<Component> loreList = meta.lore();

            if (appendEmptyLine) {
                loreList.add(Component.empty());
            }

            loreList.add(lore);
            meta.lore(loreList);
        } else {
            meta.lore(List.of(lore));
        }
    }

    /**
     * Get the display name of the item.
     * @param item the item
     * @return return translated display name of the item or head owner's name if the item is a player head
     */
    @SuppressWarnings({"deprecation"})
    public static Component getDisplayName(@Nullable ItemStack item) {
        if (item == null) {
            return Component.empty();
        }
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (meta.hasDisplayName()) {
                return Component.text(meta.getDisplayName());
            }

            if ((item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD)
                    && meta instanceof SkullMeta skull
                    && skull.hasOwner()) {
                return Component.text(skull.getOwningPlayer().getName());
            }

            return Component.translatable(getTranslationKey(item.getType()));
        }

        return Component.translatable(getTranslationKey(item.getType()));
    }

    public static boolean isActualBlock(Material material) {
        return material.isBlock() && !material.isAir();
    }

    private static String getTranslationKey(Material material) {
        return material.isItem() ? material.getItemTranslationKey() : material.getBlockTranslationKey();
    }
}