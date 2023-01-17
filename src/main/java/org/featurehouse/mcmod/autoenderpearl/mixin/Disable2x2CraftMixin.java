package org.featurehouse.mcmod.autoenderpearl.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerScreenHandler.class)
abstract class Disable2x2CraftMixin {
    @Inject(at = @At("HEAD"), method = "matches", cancellable = true)
    private void cancelMatch(Recipe<? super CraftingInventory> recipe, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
