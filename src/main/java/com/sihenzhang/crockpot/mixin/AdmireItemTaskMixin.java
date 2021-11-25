package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.AdmireItemTask;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AdmireItemTask.class)
public abstract class AdmireItemTaskMixin {
    /**
     * Inject {@link AdmireItemTask#checkExtraStartConditions(ServerWorld, PiglinEntity)} so that Piglin will walk to
     * items that can be used for Special Piglin Bartering.
     *
     * @param serverWorld  the ServerWorld where the PiglinEntity is
     * @param piglinEntity the PiglinEntity which will walk to items
     * @param cir          Mixin CallbackInfoReturnable which is used to cancel the original method
     * @param itemEntity   the ItemEntity that Piglin will walk to
     */
    @Inject(
            method = "checkExtraStartConditions(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/entity/monster/piglin/PiglinEntity;)Z",
            at = @At("TAIL"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void checkExtraStartConditionsHandler(ServerWorld serverWorld, PiglinEntity piglinEntity, CallbackInfoReturnable<Boolean> cir, ItemEntity itemEntity) {
        if (!itemEntity.getItem().getItem().is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinTasksMixin.callIsFood(itemEntity.getItem().getItem()) && PiglinBarteringRecipe.getRecipeFor(itemEntity.getItem(), serverWorld.getRecipeManager()) != null) {
            cir.setReturnValue(true);
        }
    }
}
