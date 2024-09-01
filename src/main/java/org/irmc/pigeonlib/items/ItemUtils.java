package org.irmc.pigeonlib.items;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.irmc.pigeonlib.mcversion.MCVersion;
import org.irmc.pigeonlib.mcversion.VersionGetter;
import org.jetbrains.annotations.Contract;

@UtilityClass
public final class ItemUtils {
    MCVersion MC_VERSION = VersionGetter.getVersion();
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
        return isItemSimilar(a, b, false, true, false);
    }

    /**
     * This method compares two instances of {@link ItemStack} and checks
     * @param item1 item one
     * @param item2 item two
     * @param checkLore Whether to check lore
     * @return Whether the two instances of {@link ItemStack} are similar and can be stacked.
     */
    public static boolean isItemSimilar(ItemStack item1, ItemStack item2, boolean checkLore) {
        return isItemSimilar(item1, item2, checkLore, true, false);
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
        return isItemSimilar(item1, item2, checkLore, checkAmount, false);
    }

    /**
     * This method compares two instances of {@link ItemStack} and checks
     * @param item1 item one
     * @param item2 item two
     * @param checkLore Whether to check lore
     * @param checkAmount Whether to check amount
     * @param checkSameID Whether to check SameIDItem
     * @return Whether the two instances of {@link ItemStack} are similar and can be stacked.
     */
    public static boolean isItemSimilar(ItemStack item1, ItemStack item2, boolean checkLore, boolean checkAmount, boolean checkSameID) {
        // Null check
        if (item1 == null || item2 == null) {
            return item1 == null && item2 == null;
        }

        // If types do not match, then the items cannot possibly match
        if (item1.getType() != item2.getType()) {
            return false;
        }

        // If amounts do not match, then the items cannot possibly match
        if (checkAmount && item1.getAmount() > item2.getAmount()) {
            return false;
        }

        // If either item does not have a meta then either a mismatch or both without meta = vanilla
        if (!item1.hasItemMeta() || !item2.hasItemMeta()) {
            return item1.hasItemMeta() == item2.hasItemMeta();
        }

        // Now we need to compare meta's directly - cache is already out, but let's fetch the 2nd meta also
        final ItemMeta itemMeta = item1.getItemMeta();
        final ItemMeta cachedMeta = item2.getItemMeta();

        if (itemMeta == null || cachedMeta == null) {
            return itemMeta == cachedMeta;
        }

        // ItemMetas are different types and cannot match
        if (!itemMeta.getClass().equals(cachedMeta.getClass())) {
            return false;
        }

        // SameIDItem need make isSimilarItem by themselves
        if (checkSameID && item1 instanceof SameIDItem sii && item2 instanceof SameIDItem) {
            return sii.isSimilarItem(item1, item2);
        }

        // Quick meta-extension escapes
        if (metaNotEquals(itemMeta, cachedMeta)) {
            return false;
        }

        // Has a display name (checking the name occurs later)
        if (itemMeta.hasDisplayName() != cachedMeta.hasDisplayName()) {
            return false;
        }

        // Custom model data is different, no match
        final boolean hasCustomOne = itemMeta.hasCustomModelData();
        final boolean hasCustomTwo = cachedMeta.hasCustomModelData();
        if (hasCustomOne) {
            if (!hasCustomTwo || itemMeta.getCustomModelData() != cachedMeta.getCustomModelData()) {
                return false;
            }
        } else if (hasCustomTwo) {
            return false;
        }

        // PDCs don't match
        if (!itemMeta.getPersistentDataContainer().equals(cachedMeta.getPersistentDataContainer())) {
            return false;
        }

        // Make sure enchantments match
        if (!itemMeta.getEnchants().equals(cachedMeta.getEnchants())) {
            return false;
        }

        // Check item flags
        if (!itemMeta.getItemFlags().equals(cachedMeta.getItemFlags())) {
            return false;
        }

        // Check the display name
        if (itemMeta.hasDisplayName() && !Objects.equals(itemMeta.getDisplayName(), cachedMeta.getDisplayName())) {
            return false;
        }

        // Check the lore
        if (checkLore) {
            if (itemMeta.hasLore() && cachedMeta.hasLore()) {
                if (!Objects.equals(itemMeta.getLore(), cachedMeta.getLore())) {
                    return false;
                }
            } else if (itemMeta.hasLore() != cachedMeta.hasLore()) {
                return false;
            }
        }

        // Check the attribute modifiers
        final boolean hasAttributeOne = itemMeta.hasAttributeModifiers();
        final boolean hasAttributeTwo = cachedMeta.hasAttributeModifiers();
        if (hasAttributeOne) {
            if (!hasAttributeTwo ||!Objects.equals(itemMeta.getAttributeModifiers(), cachedMeta.getAttributeModifiers())) {
                return false;
            }
        } else if (hasAttributeTwo) {
            return false;
        }

        // Check if unbreakable
        if (itemMeta.isUnbreakable() != cachedMeta.isUnbreakable()) {
            return false;
        }

        /*
        if (MC_VERSION.atLeast(MCVersion.MC1_20_5)) {
            // Check if fire-resistant
            if (itemMeta.isFireResistant() != cachedMeta.isFireResistant()) {
                return false;
            }

            // Check if hide tooltip
            if (itemMeta.isHideTooltip() != cachedMeta.isHideTooltip()) {
                return false;
            }

            // Check rarity
            final boolean hasRarityOne = itemMeta.hasRarity();
            final boolean hasRarityTwo = cachedMeta.hasRarity();
            if (hasRarityOne) {
                if (!hasRarityTwo || itemMeta.getRarity() != cachedMeta.getRarity()) {
                    return false;
                }
            } else if (hasRarityTwo) {
                return false;
            }

            // Check food components
            if (itemMeta.hasFood() && cachedMeta.hasFood()) {
                if (!Objects.equals(itemMeta.getFood(), cachedMeta.getFood())) {
                    return false;
                }
            } else if (itemMeta.hasFood() != cachedMeta.hasFood()) {
                return false;
            }

            // Check tool components
            if (itemMeta.hasTool() && cachedMeta.hasTool()) {
                if (!Objects.equals(itemMeta.getTool(), cachedMeta.getTool())) {
                    return false;
                }
            } else if (itemMeta.hasTool() != cachedMeta.hasTool()) {
                return false;
            }

            // Check jukebox playable
            if (itemMeta.hasJukeboxPlayable() && cachedMeta.hasJukeboxPlayable()) {
                if (!Objects.equals(itemMeta.getJukeboxPlayable(), cachedMeta.getJukeboxPlayable())) {
                    return false;
                }
            } else if (itemMeta.hasJukeboxPlayable() != cachedMeta.hasJukeboxPlayable()) {
                return false;
            }
        }
         */

        return true;
    }

    @SuppressWarnings("deprecation")
    private static boolean metaNotEquals(ItemMeta meta1, ItemMeta meta2) {
        if (meta1 == null || meta2 == null) {
            return meta1 != meta2;
        }

        // Damageable (first as everything can be damageable apparently)
        if (meta1 instanceof Damageable instanceOne && meta2 instanceof Damageable instanceTwo) {
            if (instanceOne.hasDamage() != instanceTwo.hasDamage()) {
                return true;
            }

            if (instanceOne.getDamage() != instanceTwo.getDamage()) {
                return true;
            }
        }

        if (meta1 instanceof Repairable instanceOne && meta2 instanceof Repairable instanceTwo) {
            if (instanceOne.hasRepairCost() != instanceTwo.hasRepairCost()) {
                return true;
            }

            if (instanceOne.getRepairCost() != instanceTwo.getRepairCost()) {
                return true;
            }
        }

        // Axolotl
        if (meta1 instanceof AxolotlBucketMeta instanceOne && meta2 instanceof AxolotlBucketMeta instanceTwo) {
            if (instanceOne.hasVariant() != instanceTwo.hasVariant()) {
                return true;
            }

            if (!instanceOne.hasVariant() || !instanceTwo.hasVariant()) {
                return true;
            }

            if (instanceOne.getVariant() != instanceTwo.getVariant()) {
                return true;
            }
        }

        // Banner
        if (meta1 instanceof BannerMeta instanceOne && meta2 instanceof BannerMeta instanceTwo) {
            if (instanceOne.numberOfPatterns() != instanceTwo.numberOfPatterns()) {
                return true;
            }

            if (!instanceOne.getPatterns().equals(instanceTwo.getPatterns())) {
                return true;
            }
        }

        // BlockData
        if (meta1 instanceof BlockDataMeta instanceOne && meta2 instanceof BlockDataMeta instanceTwo) {
            if (instanceOne.hasBlockData() != instanceTwo.hasBlockData()) {
                return true;
            }
        }

        // BlockState
        if (meta1 instanceof BlockStateMeta instanceOne && meta2 instanceof BlockStateMeta instanceTwo) {
            if (instanceOne.hasBlockState() != instanceTwo.hasBlockState()) {
                return true;
            }

            if (!instanceOne.getBlockState().equals(instanceTwo.getBlockState())) {
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

        // Bundle
        if (meta1 instanceof BundleMeta instanceOne && meta2 instanceof BundleMeta instanceTwo) {
            if (instanceOne.hasItems() != instanceTwo.hasItems()) {
                return true;
            }
            if (!instanceOne.getItems().equals(instanceTwo.getItems())) {
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
        if (meta1 instanceof EnchantmentStorageMeta instanceOne && meta2 instanceof EnchantmentStorageMeta instanceTwo) {
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
            if (!MC_VERSION.atLeast(MCVersion.MC1_20_5)) {
                if (!Objects.equals(instanceOne.getBasePotionData(), instanceTwo.getBasePotionData())) {
                    return true;
                }
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
            if (instanceOne.hasCustomEffects() != instanceTwo.hasCustomEffects()) {
                return true;
            }

            if (!Objects.equals(instanceOne.getCustomEffects(), instanceTwo.getCustomEffects())) {
                return true;
            }
        }

        // Fish Bucket
        if (meta1 instanceof TropicalFishBucketMeta instanceOne && meta2 instanceof TropicalFishBucketMeta instanceTwo) {
            if (instanceOne.hasVariant() != instanceTwo.hasVariant()) {
                return true;
            }
            if (!instanceOne.getPattern().equals(instanceTwo.getPattern())) {
                return true;
            }
            if (!instanceOne.getBodyColor().equals(instanceTwo.getBodyColor())) {
                return true;
            }
            if (!instanceOne.getPatternColor().equals(instanceTwo.getPatternColor())) {
                return true;
            }
        }

        // Knowledge Book
        if (meta1 instanceof KnowledgeBookMeta instanceOne && meta2 instanceof KnowledgeBookMeta instanceTwo) {
            if (instanceOne.hasRecipes() != instanceTwo.hasRecipes()) {
                return true;
            }

            if (!Objects.equals(instanceOne.getRecipes(), instanceTwo.getRecipes())) {
                return true;
            }
        }

        // Music Instrument
        if (meta1 instanceof MusicInstrumentMeta instanceOne && meta2 instanceof MusicInstrumentMeta instanceTwo) {
            if (!Objects.equals(instanceOne.getInstrument(), instanceTwo.getInstrument())) {
                return true;
            }
        }

        // Spawn Egg
        if (meta1 instanceof SpawnEggMeta instanceOne && meta2 instanceof SpawnEggMeta instanceTwo) {
            if (!instanceOne.getSpawnedType().equals(instanceTwo.getSpawnedType())) {
                return true;
            }
            /*
            if (MC_VERSION.atLeast(MCVersion.MC1_21)) {
                if (!Objects.equals(instanceOne.getSpawnedEntity(), instanceTwo.getSpawnedEntity())) {
                    return true;
                }
            } else {
                if (!instanceOne.getSpawnedType().equals(instanceTwo.getSpawnedType())) {
                    return true;
                }
            }
             */
        }

        // Armor
        if (meta1 instanceof ArmorMeta instanceOne && meta2 instanceof ArmorMeta instanceTwo) {
            if (!Objects.equals(instanceOne.getTrim(), instanceTwo.getTrim())) {
                return true;
            }
        }

        /*
        if (MC_VERSION.atLeast(MCVersion.MC1_20_5)) {
            // Writable Book
            if (meta1 instanceof WritableBookMeta instanceOne && meta2 instanceof WritableBookMeta instanceTwo) {
                if (instanceOne.getPageCount() != instanceTwo.getPageCount()) {
                    return true;
                }
                if (!Objects.equals(instanceOne.getPages(), instanceTwo.getPages())) {
                    return true;
                }
            }
            if (MC_VERSION.atLeast(MCVersion.MC1_21)) {
                // Ominous Bottle
                if (meta1 instanceof OminousBottleMeta instanceOne && meta2 instanceof OminousBottleMeta instanceTwo) {
                    if (instanceOne.hasAmplifier() != instanceTwo.hasAmplifier()) {
                        return true;
                    }

                    if (instanceOne.getAmplifier() != instanceTwo.getAmplifier()) {
                        return true;
                    }
                }
                // Shield
                if (meta1 instanceof ShieldMeta instanceOne && meta2 instanceof ShieldMeta instanceTwo) {
                    if (Objects.equals(instanceOne.getBaseColor(), instanceTwo.getBaseColor())) {
                        return true;
                    }
                }
            }
        }
         */

        // Cannot escape via any meta extension check
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
        if (!item.getType().isAir() && item.getAmount() > 0) {
            if (replaceConsumables) {
                switch (item.getType()) {
                    case POTION, DRAGON_BREATH, HONEY_BOTTLE -> {
                        item.setType(Material.GLASS_BOTTLE);
                        item.setAmount(1);
                        return;
                    }
                    case WATER_BUCKET, LAVA_BUCKET, MILK_BUCKET -> {
                        item.setType(Material.BUCKET);
                        item.setAmount(1);
                        return;
                    }
                    default -> {
                    }
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
            if (loreList == null) {
                return;
            }

            if (appendEmptyLine) {
                loreList.add(Component.empty());
            }

            loreList.add(lore);
            meta.lore(loreList);
        } else {
            meta.lore(List.of(lore));
        }
    }

    public static void insertLore(ItemStack item, int index, Component lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<Component> loreList = meta.lore();
            if (loreList == null) {
                loreList = List.of(lore);
            } else {
                loreList.add(index, lore);
            }
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
