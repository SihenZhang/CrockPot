package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.base.CrockPotCriteriaTriggers;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mixin(PiglinTasks.class)
public abstract class PiglinTasksMixin {
    @Shadow
    private static void holdInOffhand(PiglinEntity p_241427_0_, ItemStack p_241427_1_) {
    }

    @Shadow
    private static void throwItems(PiglinEntity p_234475_0_, List<ItemStack> p_234475_1_) {
    }

    @Shadow
    protected static boolean isLovedItem(Item p_234480_0_) {
        return true;
    }

    @Shadow
    private static void admireGoldItem(LivingEntity p_234501_0_) {
    }

    @Shadow
    private static boolean isAdmiringItem(PiglinEntity p_234451_0_) {
        return true;
    }

    @Shadow
    private static boolean isFood(Item p_234499_0_) {
        return true;
    }

    @Shadow
    private static boolean isAdmiringDisabled(PiglinEntity p_234453_0_) {
        return true;
    }

    @Shadow
    private static boolean isNotHoldingLovedItemInOffHand(PiglinEntity p_234455_0_) {
        return true;
    }

    @Inject(
            method = "pickUpItem(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Lnet/minecraft/entity/item/ItemEntity;)V",
            at = @At("HEAD")
    )
    private static void pickUpItemHandler(PiglinEntity piglinEntity, ItemEntity itemEntity, CallbackInfo ci) {
        PlayerEntity player = itemEntity.getThrower() != null ? piglinEntity.level.getPlayerByUUID(itemEntity.getThrower()) : null;
        if (player != null) {
            piglinEntity.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, player);
        }
    }

    /**
     * Inject {@link PiglinTasks#pickUpItem(PiglinEntity, ItemEntity)} so that Piglin can start bartering when picking up
     * items that can be used for Special Piglin Bartering.Vanilla behavior has the highest priority, so these vanilla
     * behaviors will be kept:
     * <ul>
     *     <li>Items that are Piglin loved will not be used for Special Piglin Bartering.</li>
     *     <li>Food that has not been eaten recently will be eaten. Food that has been eaten recently will not be used for Special Piglin Bartering.</li>
     *     <li>Items that can be equipped will be equipped directly.</li>
     *     <li>Gold Nugget will be put into the inventory directly.</li>
     * </ul>
     *
     * @param piglinEntity  the PiglinEntity which is picking up items
     * @param itemEntity    the ItemEntity which will be picked up, we don't use that
     * @param ci            Mixin CallbackInfo which is used to cancel the original method
     * @param pickedUpStack the ItemStack which has been picked up
     * @param pickedUpItem  the Item which has been picked up
     * @param canBeEquipped the Item can be equipped to the Piglin or not
     */
    @Inject(
            method = "pickUpItem(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Lnet/minecraft/entity/item/ItemEntity;)V",
            at = @At(
                    value = "JUMP",
                    ordinal = 1,
                    opcode = 154
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void pickUpItemHandler(PiglinEntity piglinEntity, ItemEntity itemEntity, CallbackInfo ci, ItemStack pickedUpStack, Item pickedUpItem, boolean canBeEquipped) {
        // Gold Nugget will be put into the inventory, vanilla behavior should be prioritised above our own behavior
        if (!canBeEquipped && !isFood(pickedUpItem) && pickedUpItem != Items.GOLD_NUGGET && !pickedUpItem.is(ItemTags.PIGLIN_REPELLENTS) && PiglinBarteringRecipe.getRecipeFor(pickedUpStack, piglinEntity.level.getRecipeManager()) != null) {
            piglinEntity.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(piglinEntity, pickedUpStack);
            admireGoldItem(piglinEntity);
            ci.cancel();
        }
    }

    /**
     * Inject {@link PiglinTasks#stopHoldingOffHandItem(PiglinEntity, boolean)} so that Piglin will throw output ItemStack
     * of Special Piglin Bartering. Vanilla behavior has the highest priority, so these vanilla behaviors will be kept:
     * <ul>
     *     <li>Only adult Piglin can throw output ItemStack of Special Piglin Bartering.</li>
     *     <li>If Piglin was hurt, it will not throw output ItemStack of Special Piglin Bartering.</li>
     *     <li>Gold Ingot will be used for Vanilla Piglin Bartering.</li>
     *     <li>Offhand Items that can be equipped will be equipped directly.</li>
     *     <li>Otherwise, items will be put into inventory.</li>
     * </ul>
     *
     * @param piglinEntity     the PiglinEntity which will stop holding offhand item
     * @param isNotHurt        true if the Piglin is bartering, false if the Piglin is hurt
     * @param ci               Mixin CallbackInfo which is used to cancel the original method
     * @param offhandStack     the ItemStack which is in the offhand
     * @param isPiglinCurrency true if the offhand Item is Gold Ingot, we don't use that
     * @param canBeEquipped    the Item can be equipped to the Piglin or not
     */
    @Inject(
            method = "stopHoldingOffHandItem(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Z)V",
            at = @At(
                    value = "JUMP",
                    ordinal = 1,
                    opcode = 154
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void stopHoldingOffHandItemHandler(PiglinEntity piglinEntity, boolean isNotHurt, CallbackInfo ci, ItemStack offhandStack, boolean isPiglinCurrency, boolean canBeEquipped) {
        PiglinBarteringRecipe recipe;
        if (isNotHurt && !canBeEquipped && !offhandStack.getItem().is(ItemTags.PIGLIN_REPELLENTS) && !isFood(offhandStack.getItem()) && (recipe = PiglinBarteringRecipe.getRecipeFor(offhandStack, piglinEntity.level.getRecipeManager())) != null) {
            throwItems(piglinEntity, Collections.singletonList(recipe.assemble(piglinEntity.getRandom())));
            ci.cancel();
        }
    }

    @Inject(
            method = "throwItemsTowardRandomPos(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Ljava/util/List;)V",
            at = @At("HEAD")
    )
    private static void throwItemsTowardRandomPosHandler(PiglinEntity piglinEntity, List<ItemStack> stacks, CallbackInfo ci) {
        piglinEntity.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).ifPresent(livingEntity -> {
            if (livingEntity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) livingEntity;
                stacks.forEach(stack -> CrockPotCriteriaTriggers.PIGLIN_BARTERING_TRIGGER.trigger(serverPlayer, stack));
            }
        });
    }

    @Inject(
            method = "throwItemsTowardPlayer(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Lnet/minecraft/entity/player/PlayerEntity;Ljava/util/List;)V",
            at = @At("HEAD")
    )
    private static void throwItemsTowardPlayerHandler(PiglinEntity piglinEntity, PlayerEntity playerEntity, List<ItemStack> stacks, CallbackInfo ci) {
        if (playerEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerEntity;
            stacks.forEach(stack -> CrockPotCriteriaTriggers.PIGLIN_BARTERING_TRIGGER.trigger(serverPlayer, stack));
        }
    }

    /**
     * Inject {@link PiglinTasks#wantsToPickup(PiglinEntity, ItemStack)} so that Piglin wants to pick up items that can be
     * used for Special Piglin Bartering. Vanilla behavior has the highest priority, so these vanilla behaviors will be kept:
     * <ul>
     *     <li>Items that have tag {@code minecraft:piglin_repellents} will not be picked up.</li>
     *     <li>Piglin that is attacking or cannot admire item will not want to pick up items.</li>
     *     <li>Gold Ingot will be picked up when Piglin is not holding loved item in offhand.</li>
     *     <li>Gold Nugget will be picked up when Piglin has inventory.</li>
     *     <li>Food will be picked up when Piglin has not eaten recently and has inventory.</li>
     *     <li>Items that are not loved items will be picked up when they can replace current item.</li>
     *     <li>Items that can be used for Special Piglin Bartering will be picked up when Piglin is not holding loved item in offhand.</li>
     *     <li>Otherwise, items will be picked up when Piglin is not holding loved item in offhand and Piglin has inventory.</li>
     * </ul>
     *
     * @param piglinEntity       the PiglinEntity which wants to pick up item
     * @param wantsToPickupStack the ItemStack which will be picked up
     * @param cir                Mixin CallbackInfoReturnable which is used to cancel the original method
     * @param wantsToPickupItem  the Item which will be picked up
     */
    @Inject(
            method = "wantsToPickup(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Lnet/minecraft/item/ItemStack;)Z",
            at = @At(
                    value = "JUMP",
                    ordinal = 0,
                    opcode = 166
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void wantsToPickupHandler(PiglinEntity piglinEntity, ItemStack wantsToPickupStack, CallbackInfoReturnable<Boolean> cir, Item wantsToPickupItem) {
        // Gold Nugget, Food and not loved item that can be equipped have their own behavior, so it will be skipped
        if (wantsToPickupItem != Items.GOLD_NUGGET && !isFood(wantsToPickupItem) && !(!isLovedItem(wantsToPickupItem) && ((IPiglinEntityMixin) piglinEntity).callCanReplaceCurrentItem(wantsToPickupStack)) && PiglinBarteringRecipe.getRecipeFor(wantsToPickupStack, piglinEntity.level.getRecipeManager()) != null) {
            cir.setReturnValue(isNotHoldingLovedItemInOffHand(piglinEntity));
        }
    }

    @Inject(
            method = "mobInteract(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResultType;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;",
                    ordinal = 0
            )
    )
    private static void modInteractHandler(PiglinEntity piglinEntity, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<ActionResultType> cir) {
        piglinEntity.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, playerEntity);
    }

    /**
     * Overwrite(Inject at head of the method and cancel) {@link PiglinTasks#canAdmire(PiglinEntity, ItemStack)} so that
     * Piglin will admire items that can be used for Special Piglin Bartering.
     *
     * @param piglinEntity the PiglinEntity which will admire items
     * @param itemStack    the ItemStack which will be admired
     * @param cir          Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "canAdmire(Lnet/minecraft/entity/monster/piglin/PiglinEntity;Lnet/minecraft/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void canAdmireHandler(PiglinEntity piglinEntity, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        Predicate<ItemStack> isSpecialBarteringStack = stack -> !stack.getItem().is(ItemTags.PIGLIN_REPELLENTS) && !isFood(stack.getItem()) && PiglinBarteringRecipe.getRecipeFor(stack, piglinEntity.level.getRecipeManager()) != null;
        cir.setReturnValue(!isAdmiringDisabled(piglinEntity) && !isAdmiringItem(piglinEntity) && piglinEntity.isAdult() && (itemStack.isPiglinCurrency() || isSpecialBarteringStack.test(itemStack)));
    }

    /**
     * Overwrite(Inject at head of the method and cancel) {@link PiglinTasks#isPlayerHoldingLovedItem(LivingEntity)} so that
     * Piglin will look at players that holding items that can be used for Special Piglin Bartering.
     *
     * @param livingEntity the LivingEntity holding items
     * @param cir          Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "isPlayerHoldingLovedItem(Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void isPlayerHoldingLovedItemHandler(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        Predicate<Item> isSpecialBarteringItem = item -> !item.is(ItemTags.PIGLIN_REPELLENTS) && !isFood(item) && PiglinBarteringRecipe.getRecipeFor(item, livingEntity.level.getRecipeManager()) != null;
        cir.setReturnValue(livingEntity.getType() == EntityType.PLAYER && (livingEntity.isHolding(PiglinTasksMixin::isLovedItem) || livingEntity.isHolding(isSpecialBarteringItem)));
    }

    /**
     * Overwrite(Inject at head of the method and cancel) {@link PiglinTasks#isNotHoldingLovedItemInOffHand(PiglinEntity)}
     * so that the method will return true when Piglin is holding items that can be used for Special Piglin Bartering in offhand.
     *
     * @param piglinEntity the PiglinEntity which is holding items in offhand
     * @param cir          Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "isNotHoldingLovedItemInOffHand(Lnet/minecraft/entity/monster/piglin/PiglinEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void isNotHoldingLovedItemInOffHandHandler(PiglinEntity piglinEntity, CallbackInfoReturnable<Boolean> cir) {
        // To avoid Piglin picking up items during bartering, cancel this method so that it can determine if the item in off hand can be used for PiglinBarteringRecipe
        ItemStack offhandStack = piglinEntity.getOffhandItem();
        Predicate<ItemStack> isSpecialBarteringStack = stack -> !stack.getItem().is(ItemTags.PIGLIN_REPELLENTS) && !isFood(stack.getItem()) && PiglinBarteringRecipe.getRecipeFor(stack, piglinEntity.level.getRecipeManager()) != null;
        cir.setReturnValue(offhandStack.isEmpty() || (!isLovedItem(offhandStack.getItem()) && !isSpecialBarteringStack.test(offhandStack)));
    }
}
