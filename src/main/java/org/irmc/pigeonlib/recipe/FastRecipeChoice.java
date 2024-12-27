package org.irmc.pigeonlib.recipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.irmc.pigeonlib.items.ItemUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FastRecipeChoice extends RecipeChoice.ExactChoice {
    public FastRecipeChoice(@NotNull ItemStack stack) {
        this(List.of(stack));
    }

    public FastRecipeChoice(ItemStack... stacks) {
        this(Arrays.asList(stacks));
    }

    public FastRecipeChoice(@NotNull List<ItemStack> choices) {
        super(choices);
    }

    public boolean test(@NotNull ItemStack t) {
        Iterator<ItemStack> var2 = getChoices().iterator();

        ItemStack match;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            match = var2.next();
        } while (!ItemUtils.isItemSimilar(t, match, true, true));

        return true;
    }
}
