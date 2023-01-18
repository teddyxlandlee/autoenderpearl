package org.featurehouse.mcmod.autoenderpearl.mixin;

import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ShapedRecipe.class, ShapelessRecipe.class})
abstract class ClearGreenBookMixin {
    @Inject(cancellable = true, at = @At("HEAD"), method = "fits(II)Z")
    private void cancelFits(int width, int height, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
