package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
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
     * Cancels {@link BubbleColumnBlock#entityInside} for {@link LivingEntity} instances with
     * {@link ModEffects#OCEAN_AFFINITY} so bubble columns no longer affect them.
     */
    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void cancelBubbleColumnForOceanAffinity(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.OCEAN_AFFINITY.getDelegate())) {
            ci.cancel();
        }
    }
}
