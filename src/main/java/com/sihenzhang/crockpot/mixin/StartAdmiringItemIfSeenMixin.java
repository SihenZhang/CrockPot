package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.StartAdmiringItemIfSeen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(StartAdmiringItemIfSeen.class)
public abstract class StartAdmiringItemIfSeenMixin {
    /**
     * Inject {@link StartAdmiringItemIfSeen#checkExtraStartConditions(ServerLevel, Piglin)} so that Piglin will walk to
     * items that can be used for Special Piglin Bartering.
     *
     * @param level      the ServerLevel where the Piglin is
     * @param owner      the Piglin which will walk to items
     * @param cir        Mixin CallbackInfoReturnable which is used to cancel the original method
     * @param itemEntity the ItemEntity that Piglin will walk to
     */
    @Inject(
            method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/piglin/Piglin;)Z",
            at = @At("TAIL"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void checkExtraStartConditionsHandler(ServerLevel level, Piglin owner, CallbackInfoReturnable<Boolean> cir, ItemEntity itemEntity) {
        if (!itemEntity.getItem().is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinAiMixin.callIsFood(itemEntity.getItem()) && PiglinBarteringRecipe.getRecipeFor(itemEntity.getItem(), level.getRecipeManager()) != null) {
            cir.setReturnValue(true);
        }
    }
}
