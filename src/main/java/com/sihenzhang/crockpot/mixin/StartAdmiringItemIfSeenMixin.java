package com.sihenzhang.crockpot.mixin;

import com.mojang.datafixers.kinds.Const;
import com.mojang.datafixers.kinds.IdF;
import com.mojang.datafixers.util.Unit;
import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.StartAdmiringItemIfSeen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(StartAdmiringItemIfSeen.class)
public abstract class StartAdmiringItemIfSeenMixin {
    /**
     * Inject {@link StartAdmiringItemIfSeen} so that Piglin will walk to
     * items that can be used for Special Piglin Bartering.
     *
     * @param level      the ServerLevel where the Piglin is
     * @param owner      the Piglin which will walk to items
     * @param cir        Mixin CallbackInfoReturnable which is used to cancel the original method
     * @param itemEntity the ItemEntity that Piglin will walk to
     */
    @Inject(
            method = "lambda$create$0(Lnet/minecraft/world/entity/ai/behavior/declarative/BehaviorBuilder$Instance;Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryAccessor;Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryAccessor;ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)Z",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/ai/behavior/declarative/BehaviorBuilder$Instance;get(Lnet/minecraft/world/entity/ai/behavior/declarative/MemoryAccessor;)Ljava/lang/Object;"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void checkExtraStartConditionsHandler(BehaviorBuilder.Instance<?> builder, MemoryAccessor<IdF.Mu, ItemEntity> accessor1, MemoryAccessor<Const.Mu<Unit>, Boolean> accessor2, int what, ServerLevel level, LivingEntity owner, long seed, CallbackInfoReturnable<Boolean> cir, ItemEntity itemEntity) {
        if (!itemEntity.getItem().is(ItemTags.PIGLIN_REPELLENTS) && !IPiglinAiMixin.callIsFood(itemEntity.getItem()) && PiglinBarteringRecipe.getRecipeFor(itemEntity.getItem(), level.getRecipeManager()) != null) {
            cir.setReturnValue(true);
        }
    }
}
