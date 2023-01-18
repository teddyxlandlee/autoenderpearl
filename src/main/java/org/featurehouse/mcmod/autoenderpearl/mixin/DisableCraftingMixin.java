package org.featurehouse.mcmod.autoenderpearl.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
abstract class DisableShapedMixin {
    @Inject(cancellable = true, at = @At("HEAD"), method = "fits")
    private void disableFits(int width, int height, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(cancellable = true, at = @At("HEAD"), method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z")
    private void disableMatches(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}

@Mixin(ShapelessRecipe.class)
abstract class DisableShapelessMixin {
    @Inject(cancellable = true, at = @At("HEAD"), method = "fits")
    private void disableFits(int width, int height, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(cancellable = true, at = @At("HEAD"), method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z")
    private void disableMatches(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
