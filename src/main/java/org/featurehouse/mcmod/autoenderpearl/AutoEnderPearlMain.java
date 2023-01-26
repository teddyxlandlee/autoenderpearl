package org.featurehouse.mcmod.autoenderpearl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AutoEnderPearlMain implements ModInitializer {
    public static final RecipeType<FakeCraftBenchRecipe> RECIPE_TYPE = new RecipeType<FakeCraftBenchRecipe>() {
        @Override
        public String toString() {
            return "autoenderpearl:fake_craft_bench";
        }
    };
    public static final RecipeSerializer<FakeCraftBenchRecipe> RECIPE_SERIALIZER = new FakeCraftBenchRecipe.Serializer();

    @Override
    public void onInitialize() {
        Registry.register(Registry.RECIPE_TYPE, new Identifier("autoenderpearl:fake_craft_bench"), RECIPE_TYPE);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("autoenderpearl:fake_craft_bench"), RECIPE_SERIALIZER);
    }
}
