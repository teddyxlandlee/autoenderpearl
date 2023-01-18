package org.featurehouse.mcmod.autoenderpearl;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeManager;

import java.util.Comparator;
import java.util.Optional;

public record FakeCraftingBench(RecipeManager recipeManager, PlayerInventory playerInventory) {
    public boolean craft() {
        final Optional<FakeCraftBenchRecipe> recipe = recipeManager.listAllOfType(AutoEnderPearlMain.RECIPE_TYPE).stream()
                .sorted(Comparator.comparing(FakeCraftBenchRecipe::getId))
                .filter(r -> r.matches(playerInventory, playerInventory.player.world))
                .findFirst();
        if (recipe.isPresent()) {   // It is matched
            var fakeCraftBench = recipe.get();
            var output = fakeCraftBench.craft(playerInventory);
            if (!playerInventory.insertStack(output))
                playerInventory.player.dropItem(output, true);
            return true;
        }
        return false;
    }
}
