package org.featurehouse.mcmod.autoenderpearl.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.featurehouse.mcmod.autoenderpearl.FakeCraftingBench;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingTableBlock.class)
abstract class SimpleCraftPearlMixin {
    @Inject(
            method = "onUse",
            at = @At("RETURN")
    )
    private void postUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        FakeCraftingBench bench = new FakeCraftingBench(world.getRecipeManager(), player.inventory);
        if (!bench.craft())
            player.sendMessage(new TranslatableText("error.autoenderpearl.no_ingredient"), true);
    }
}
