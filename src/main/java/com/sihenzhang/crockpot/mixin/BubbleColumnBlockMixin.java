package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnBlock.class)
public abstract class BubbleColumnBlockMixin {
    /**
     * Inject {@link BubbleColumnBlock#entityInside(BlockState, Level, BlockPos, Entity)} so that Entity with
     * Ocean Affinity effect will not be affected by Bubble Column.
     *
     * @param state  the BlockState of the Bubble Column Block
     * @param level  the Level which the Bubble Column Block exists
     * @param pos    the BlockPos where the Bubble Column Block is located
     * @param entity the Entity which is in the Bubble Column Block
     * @param ci     Mixin CallbackInfo which is used to cancel the original method
     */
    @Inject(
            method = "entityInside(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void entityInsideHandler(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(CrockPotEffects.OCEAN_AFFINITY.get())) {
            ci.cancel();
        }
    }
}
