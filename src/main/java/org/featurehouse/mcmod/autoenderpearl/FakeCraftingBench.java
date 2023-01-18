package org.featurehouse.mcmod.autoenderpearl;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.Optional;

public record FakeCraftingBench(RecipeManager recipeManager, PlayerInventory playerInventory) {
    public boolean craft() {
        final World world = playerInventory.player.world;
        if (world.isClient()) return true;

        final Optional<FakeCraftBenchRecipe> recipe = recipeManager.listAllOfType(AutoEnderPearlMain.RECIPE_TYPE).stream()
                .sorted(Comparator.comparing(FakeCraftBenchRecipe::getId))
                .filter(r -> r.matches(playerInventory, world))
                .findFirst();
        if (recipe.isPresent()) {   // It is matched
            var fakeCraftBench = recipe.get();
            var output = fakeCraftBench.craft(playerInventory);
            if (!playerInventory.insertStack(output.copy()))
                playerInventory.player.dropItem(output, true);
            playerInventory.markDirty();
            return true;
        }
        return false;
    }
}
