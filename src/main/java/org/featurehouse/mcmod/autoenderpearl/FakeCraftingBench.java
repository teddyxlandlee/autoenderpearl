package org.featurehouse.mcmod.autoenderpearl;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.featurehouse.mcmod.autoenderpearl.config.CraftBenchConfig;

import java.util.Optional;

public record FakeCraftingBench(RecipeManager recipeManager, PlayerInventory playerInventory) {
    public boolean craft() {
        var identifiers = CraftBenchConfig.getInstance().getAllowedCrafts();
        for (Identifier id : identifiers) {
            final Optional<? extends Recipe<?>> recipe = recipeManager.get(id);
            if (recipe.isPresent()) {
                var r0 = recipe.get();
                if (r0.getType() == AutoEnderPearlMain.RECIPE_TYPE) {
                    final FakeCraftBenchRecipe fakeCraftingBench = (FakeCraftBenchRecipe) r0;
                    if (fakeCraftingBench.matches(playerInventory, playerInventory.player.world)) {
                        var output = fakeCraftingBench.craft(playerInventory);
                        if (!playerInventory.insertStack(output))
                            playerInventory.player.dropItem(output, true);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
