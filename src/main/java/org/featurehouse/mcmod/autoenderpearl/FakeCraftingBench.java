package org.featurehouse.mcmod.autoenderpearl;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public final class FakeCraftingBench {
    private final RecipeManager recipeManager;
    private final PlayerInventory playerInventory;

    public FakeCraftingBench(RecipeManager recipeManager, PlayerInventory playerInventory) {
        this.recipeManager = recipeManager;
        this.playerInventory = playerInventory;
    }

    public boolean craft() {
        final World world = playerInventory.player.world;
        if (world.isClient()) return true;

        final Optional<FakeCraftBenchRecipe> recipe = recipeManager.listAllOfType(AutoEnderPearlMain.RECIPE_TYPE).stream()
                .sorted(Comparator.comparing(FakeCraftBenchRecipe::getId))
                .filter(r -> r.matches(playerInventory, world))
                .findFirst();
        if (recipe.isPresent()) {   // It is matched
            FakeCraftBenchRecipe fakeCraftBench = recipe.get();
            ItemStack output = fakeCraftBench.craft(playerInventory);
            if (!playerInventory.insertStack(output.copy()))
                playerInventory.player.dropItem(output, true);
            playerInventory.markDirty();
            return true;
        }
        return false;
    }

    public RecipeManager recipeManager() {
        return recipeManager;
    }

    public PlayerInventory playerInventory() {
        return playerInventory;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        FakeCraftingBench that = (FakeCraftingBench) obj;
        return Objects.equals(this.recipeManager, that.recipeManager) &&
                Objects.equals(this.playerInventory, that.playerInventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeManager, playerInventory);
    }

    @Override
    public String toString() {
        return "FakeCraftingBench[" +
                "recipeManager=" + recipeManager + ", " +
                "playerInventory=" + playerInventory + ']';
    }

}
