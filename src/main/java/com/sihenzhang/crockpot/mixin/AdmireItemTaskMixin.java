package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.AdmireItemTask;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AdmireItemTask.class)
public abstract class AdmireItemTaskMixin {
    @Inject(
            method = "checkExtraStartConditions(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/entity/monster/piglin/PiglinEntity;)Z",
            at = @At("TAIL"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void checkExtraStartConditionsHandler(ServerWorld arg0, PiglinEntity p_212832_2_, CallbackInfoReturnable<Boolean> cir, ItemEntity itementity) {
        if (CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.matches(itementity.getItem()) != null) {
            cir.setReturnValue(true);
        }
    }
}
