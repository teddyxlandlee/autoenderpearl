package org.featurehouse.mcmod.autoenderpearl;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class FakeCraftBenchRecipe implements Recipe<PlayerInventory> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Identifier id;
    private final List<ItemStack> input;
    private final ItemStack output;

    public FakeCraftBenchRecipe(Identifier id, List<ItemStack> input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(PlayerInventory inventory, World world) {
        // Have a copy
        SimpleInventory copy = new SimpleInventory(32);
        inventory.main.forEach(copy::addStack);

        for (ItemStack input0 : input) {
            final int count = input0.getCount();
            final int remove = Inventories.remove(copy, predicate(input0), count, false);
            if (remove < count) return false;
        }
        return true;
    }

    private static Predicate<ItemStack> predicate(final ItemStack input) {
        return itemStack -> {
            if (!ItemStack.areItemsEqual(itemStack, input) || itemStack.getCount() < input.getCount())
                return false;
            if (input.hasTag()) {
                NbtPredicate nbtPredicate = new NbtPredicate(input.getTag());
                return nbtPredicate.test(itemStack);
            }
            return true;
        };
    }

    private static void remove(DefaultedList<ItemStack> inventory, Predicate<ItemStack> shouldRemove, int maxCount) {
        int i = 0;
        for (int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.get(j);
            int k = Inventories.remove(itemStack, shouldRemove, maxCount - i, false);
            if (k > 0 && itemStack.isEmpty()) {
                inventory.set(j, ItemStack.EMPTY);
            }
            i += k;
        }
    }

    @Override
    public ItemStack craft(PlayerInventory inventory) {
        for (ItemStack input0 : input) {
            remove(inventory.main, predicate(input0), input0.getCount());
        }
        return getOutput();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AutoEnderPearlMain.RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return AutoEnderPearlMain.RECIPE_TYPE;
    }

    public Identifier id() {
        return id;
    }

    public List<ItemStack> input() {
        return input;
    }

    public ItemStack output() {
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        FakeCraftBenchRecipe that = (FakeCraftBenchRecipe) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.input, that.input) &&
                Objects.equals(this.output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, input, output);
    }

    @Override
    public String toString() {
        return "FakeCraftBenchRecipe[" +
                "id=" + id + ", " +
                "input=" + input + ", " +
                "output=" + output + ']';
    }


    public static final class Serializer implements RecipeSerializer<FakeCraftBenchRecipe> {

        @Override
        public FakeCraftBenchRecipe read(Identifier id, JsonObject json) {
            final JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            List<ItemStack> list = Lists.newArrayList();
            for (JsonElement e : ingredients) {
                JsonObject o = JsonHelper.asObject(e, "ingredient");
                ItemStack itemStack = readItemStack(id, o);
                list.add(itemStack);
            }
            ItemStack output = readItemStack(id, JsonHelper.getObject(json, "result"));
            return new FakeCraftBenchRecipe(id, list, output);
        }

        private static ItemStack readItemStack(Object id, JsonObject o) {
            final Item item = JsonHelper.getItem(o, "item");
            final int count = JsonHelper.getInt(o, "count", 1);
            final String nbt = JsonHelper.getString(o, "nbt", null);
            final ItemStack itemStack = new ItemStack(item, count);
            if (nbt != null) {
                try {
                    itemStack.setTag(new StringNbtReader(new StringReader(nbt)).parseCompound());
                } catch (CommandSyntaxException ex) {
                    LOGGER.error("An error occurred while parsing recipe {}", id, ex);
                }
            }
            return itemStack;
        }

        @Override
        public FakeCraftBenchRecipe read(Identifier id, PacketByteBuf buf) {
            final ItemStack output = buf.readItemStack();
            final int inputSize = buf.readVarInt();
            final List<ItemStack> input = Lists.newArrayListWithCapacity(inputSize);
            for (int i = 0; i < inputSize; i++) {
                input.add(buf.readItemStack());
            }
            return new FakeCraftBenchRecipe(id, input, output);
        }

        @Override
        public void write(PacketByteBuf buf, FakeCraftBenchRecipe recipe) {
            buf.writeItemStack(recipe.output);
            buf.writeVarInt(recipe.input.size());
            recipe.input.forEach(buf::writeItemStack);
        }
    }
}
