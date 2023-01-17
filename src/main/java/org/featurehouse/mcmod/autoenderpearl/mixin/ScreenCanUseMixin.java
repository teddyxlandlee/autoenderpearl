package org.featurehouse.mcmod.autoenderpearl.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(PlayerEntity.class)
abstract class ScreenCanUseMixin {
    @Accessor("playerScreenHandler") public abstract PlayerScreenHandler playerScreenHandler();

    @Redirect(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/ScreenHandler;canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"
            )
    )
    private boolean editTick(ScreenHandler instance, PlayerEntity playerEntity) {
        return instance == this.playerScreenHandler();
    }

    @Inject(method = "openHandledScreen", at = @At("HEAD"), cancellable = true)
    private void refuseOpenHandledScreen(NamedScreenHandlerFactory factory, CallbackInfoReturnable<OptionalInt> cir) {
        cir.setReturnValue(OptionalInt.empty());
    }
}
