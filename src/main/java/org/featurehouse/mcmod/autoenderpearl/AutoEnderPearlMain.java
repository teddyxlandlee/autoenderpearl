package org.featurehouse.mcmod.autoenderpearl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AutoEnderPearlMain implements ModInitializer {
    public static final RecipeType<FakeCraftBenchRecipe> RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return "autoenderpearl:fake_craft_bench";
        }
    };
    public static final RecipeSerializer<FakeCraftBenchRecipe> RECIPE_SERIALIZER = new FakeCraftBenchRecipe.Serializer();

    @Override
    public void onInitialize() {
        Registry.register(Registries.RECIPE_TYPE, new Identifier("autoenderpearl:fake_craft_bench"), RECIPE_TYPE);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("autoenderpearl:fake_craft_bench"), RECIPE_SERIALIZER);
    }
}
