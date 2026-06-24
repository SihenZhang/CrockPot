package com.sihenzhang.crockpot.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.sihenzhang.crockpot.advancement.ModCriterionTriggers;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.util.RecipeUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {
    @Shadow
    private static void throwItems(Piglin body, List<ItemStack> itemStacks) {
        throw new AssertionError();
    }

    /**
     * Remembers the player who threw a custom bartering item so later random reward throws can
     * award {@link ModCriterionTriggers#PIGLIN_BARTERING_TRIGGER} to the correct player.
     */
    @Inject(method = "pickUpItem", at = @At("HEAD"))
    private static void rememberPickedUpItemOwnerForCustomBarteringRewards(ServerLevel level, Piglin body, ItemEntity itemEntity, CallbackInfo ci) {
        if (itemEntity.getOwner() instanceof Player player) {
            body.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, player);
        }
    }

    /**
     * Lets piglins pick up items that match {@link ModRecipes#PIGLIN_BARTERING_RECIPE_TYPE}
     * as if they were vanilla loved items.
     */
    @WrapOperation(
            method = "pickUpItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isLovedItem(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private static boolean treatCustomBarteringItemAsLovedOnPickup(ItemStack itemStack, Operation<Boolean> original, ServerLevel level) {
        return original.call(itemStack) || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), level).isPresent();
    }

    /**
     * Handles a completed custom barter before vanilla tries to equip the input item as gear:
     * throws the recipe result when bartering is enabled and skips the vanilla fallback branch.
     */
    @Inject(
            method = "stopHoldingOffHandItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/piglin/Piglin;equipItemIfPossible(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            ),
            cancellable = true
    )
    private static void throwCustomBarteringResultBeforeEquipmentFallback(ServerLevel level, Piglin body, boolean barteringEnabled, CallbackInfo ci, @Local(name = "itemStack") ItemStack itemStack) {
        var input = new SingleRecipeInput(itemStack);
        var recipe = RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), input, level);
        if (recipe.isPresent()) {
            if (barteringEnabled) {
                throwItems(body, List.of(recipe.get().value().assemble(input)));
            }
            ci.cancel();
        }
    }

    /**
     * Keeps custom bartering inputs on the same {@link PiglinAi#stopHoldingOffHandItem} path as
     * vanilla loved items while the piglin finishes admiring them.
     */
    @WrapOperation(
            method = "stopHoldingOffHandItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isLovedItem(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private static boolean treatCustomBarteringItemAsLovedWhenStoppingOffHand(ItemStack itemStack, Operation<Boolean> original, ServerLevel level) {
        return original.call(itemStack) || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), level).isPresent();
    }

    /**
     * Awards the custom bartering advancement for reward items thrown to a random position, using
     * the interaction target remembered when the item was picked up or handed over.
     */
    @Inject(method = "throwItemsTowardRandomPos", at = @At("HEAD"))
    private static void triggerCustomBarteringAdvancementForRandomThrow(Piglin body, List<ItemStack> itemStacks, CallbackInfo ci) {
        body.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).ifPresent(livingEntity -> {
            if (livingEntity instanceof ServerPlayer player) {
                itemStacks.forEach(stack -> ModCriterionTriggers.PIGLIN_BARTERING_TRIGGER.get().trigger(player, stack));
            }
        });
    }

    /**
     * Awards the custom bartering advancement when vanilla throws reward items directly toward a
     * visible player.
     */
    @Inject(method = "throwItemsTowardPlayer", at = @At("HEAD"))
    private static void triggerCustomBarteringAdvancementForPlayerThrow(Piglin body, Player player, List<ItemStack> itemStacks, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            itemStacks.forEach(stack -> ModCriterionTriggers.PIGLIN_BARTERING_TRIGGER.get().trigger(serverPlayer, stack));
        }
    }

    /**
     * Remembers the player who right-clicked a piglin with a custom bartering input so the later
     * reward throw can award advancements to that player.
     */
    @Inject(
            method = "mobInteract",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consumeAndReturn(ILnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;")
    )
    private static void rememberInteractingPlayerForCustomBarteringRewards(ServerLevel level, Piglin body, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        body.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, player);
    }

    /**
     * Allows direct interaction with custom bartering inputs to start the piglin admiration flow.
     */
    @ModifyExpressionValue(
            method = "canAdmire",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isPiglinCurrency()Z")
    )
    private static boolean canAdmireCustomBarteringItem(boolean original, Piglin body, ItemStack playerHeldItemStack) {
        return original || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(playerHeldItemStack), body.level()).isPresent();
    }

    /**
     * Makes piglin attention checks treat players holding custom bartering inputs like players
     * holding vanilla loved items.
     */
    @WrapOperation(
            method = "isPlayerHoldingLovedItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isHolding(Ljava/util/function/Predicate;)Z")
    )
    private static boolean treatHeldCustomBarteringItemsAsLoved(LivingEntity instance, Predicate<ItemStack> itemPredicate, Operation<Boolean> original) {
        return original.call(instance, (Predicate<ItemStack>) itemStack ->
                itemPredicate.test(itemStack) || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), instance.level()).isPresent()
        );
    }

    /**
     * Prevents piglins from treating custom bartering inputs in their offhand as non-loved items.
     */
    @WrapOperation(
            method = "isNotHoldingLovedItemInOffHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isLovedItem(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private static boolean treatCustomBarteringOffhandItemAsLoved(ItemStack itemStack, Operation<Boolean> original, Piglin body) {
        return original.call(itemStack) || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), body.level()).isPresent();
    }

    /**
     * Lets piglins decide to pick up custom bartering inputs even when they are not vanilla
     * piglin currency.
     */
    @ModifyExpressionValue(
            method = "wantsToPickup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isPiglinCurrency()Z")
    )
    private static boolean wantsToPickupCustomBarteringItem(boolean original, Piglin body, ItemStack itemStack) {
        return original || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), body.level()).isPresent();
    }
}
