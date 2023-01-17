package org.featurehouse.mcmod.autoenderpearl;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.featurehouse.mcmod.autoenderpearl.config.CraftBenchConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AutoEnderPearlMain implements ModInitializer {
    private static final Identifier RELOAD_ID = new Identifier("autoenderpearl:reload");
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("autoenderpearl.txt");
    public static final RecipeType<FakeCraftBenchRecipe> RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, new Identifier("autoenderpearl:fake_craft_bench"), new RecipeType<FakeCraftBenchRecipe>() {
        @Override
        public String toString() {
            return "autoenderpearl:fake_craft_bench";
        }
    });
    public static final RecipeSerializer<FakeCraftBenchRecipe> RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier("autoenderpearl:fake_craft_bench"), new FakeCraftBenchRecipe.Serializer());

    @Override
    public void onInitialize() {
        try {
            loadConfig();
        } catch (IOException e) {
            LOGGER.error("[AutoEnderPearl] Failed to load config. Using default config temporarily.", e);
        }
    }

    private static void loadConfig() throws IOException {
        if (Files.notExists(CONFIG_FILE)) {
            CraftBenchConfig.getInstance().loadFromDefault();
            try (var w = Files.newBufferedWriter(CONFIG_FILE)) {
                CraftBenchConfig.getInstance().serialize(w);
            }
        } else {
            try (var r = Files.newBufferedReader(CONFIG_FILE)) {
                CraftBenchConfig.getInstance().deserialize(r);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        ClientCommandRegistrationCallback.EVENT.register(RELOAD_ID, (dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("niganma")
                    .executes(context -> {
                        try {
                            loadConfig();
                            return 1;
                        } catch (IOException e) {
                            context.getSource().sendError(Text.translatable("error.autoenderpearl.reload", e));
                            return 0;
                        }
                    })
            );
        });
    }
}
