package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.advancement.CrockPotCriteriaTriggers;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Predicate;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {
    @Shadow
    private static void holdInOffhand(Piglin p_241427_0_, ItemStack p_241427_1_) {
    }

    @Shadow
    private static void throwItems(Piglin p_234475_0_, List<ItemStack> p_234475_1_) {
    }

    @Shadow
    protected static boolean isLovedItem(ItemStack p_149966_) {
        return true;
    }

    @Shadow
    private static void admireGoldItem(LivingEntity p_234501_0_) {
    }

    @Shadow
    private static boolean isAdmiringItem(Piglin p_234451_0_) {
        return true;
    }

    @Shadow
    private static boolean isFood(ItemStack p_149970_) {
        return true;
    }

    @Shadow
    private static boolean isAdmiringDisabled(Piglin p_234453_0_) {
        return true;
    }

    @Shadow
    private static boolean isNotHoldingLovedItemInOffHand(Piglin p_234455_0_) {
        return true;
    }

    /**
     * Inject {@link PiglinAi#pickUpItem(Piglin, ItemEntity)} so that Piglin will know who throws the Item
     * will be picked up. This will be used for PiglinBarteringTrigger.
     *
     * @param piglin     the Piglin which is picking up items
     * @param itemEntity the ItemEntity which will be picked up
     * @param ci         Mixin CallbackInfo which is used to cancel the original method
     */
    @Inject(
            method = "pickUpItem(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/entity/item/ItemEntity;)V",
            at = @At("HEAD")
    )
    private static void pickUpItemHandler(Piglin piglin, ItemEntity itemEntity, CallbackInfo ci) {
        if (itemEntity.getOwner() instanceof Player player) {
            piglin.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, player);
        }
    }

    /**
     * Inject {@link PiglinAi#pickUpItem(Piglin, ItemEntity)} so that Piglin can start bartering when picking up
     * items that can be used for Special Piglin Bartering.Vanilla behavior has the highest priority, so these createVanillaRL
     * behaviors will be kept:
     * <ul>
     *     <li>Items that are Piglin loved will not be used for Special Piglin Bartering.</li>
     *     <li>Food that has not been eaten recently will be eaten. Food that has been eaten recently will not be used for Special Piglin Bartering.</li>
     *     <li>Items that can be equipped will be equipped directly.</li>
     *     <li>Gold Nugget will be put into the inventory directly.</li>
     * </ul>
     *
     * @param piglin        the Piglin which is picking up items
     * @param itemEntity    the ItemEntity which will be picked up, we don't use that
     * @param ci            Mixin CallbackInfo which is used to cancel the original method
     * @param pickedUpStack the ItemStack which has been picked up
     */
    @Inject(
            method = "pickUpItem(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/entity/item/ItemEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;putInInventory(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 0
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void pickUpItemHandler(Piglin piglin, ItemEntity itemEntity, CallbackInfo ci, ItemStack pickedUpStack) {
        // Gold Nugget will be put into the inventory, createVanillaRL behavior should be prioritised above our own behavior
        if (!isFood(pickedUpStack) && pickedUpStack.getItem() != Items.GOLD_NUGGET && !pickedUpStack.is(ItemTags.PIGLIN_REPELLENTS) && piglin.level().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(pickedUpStack), piglin.level()).isPresent()) {
            piglin.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(piglin, pickedUpStack);
            admireGoldItem(piglin);
            ci.cancel();
        }
    }

    /**
     * Inject {@link PiglinAi#stopHoldingOffHandItem(Piglin, boolean)} so that Piglin will throw output ItemStack
     * of Special Piglin Bartering. Vanilla behavior has the highest priority, so these createVanillaRL behaviors will be kept:
     * <ul>
     *     <li>Only adult Piglin can throw output ItemStack of Special Piglin Bartering.</li>
     *     <li>If Piglin was hurt, it will not throw output ItemStack of Special Piglin Bartering.</li>
     *     <li>Gold Ingot will be used for Vanilla Piglin Bartering.</li>
     *     <li>Offhand Items that can be equipped will be equipped directly.</li>
     *     <li>Otherwise, items will be put into inventory.</li>
     * </ul>
     *
     * @param piglin       the Piglin which will stop holding offhand item
     * @param isNotHurt    true if the Piglin is bartering, false if the Piglin is hurt
     * @param ci           Mixin CallbackInfo which is used to cancel the original method
     * @param offhandStack the ItemStack which is in the offhand
     */
    @Inject(
            method = "stopHoldingOffHandItem(Lnet/minecraft/world/entity/monster/piglin/Piglin;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;putInInventory(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 0
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void stopHoldingOffHandItemHandler(Piglin piglin, boolean isNotHurt, CallbackInfo ci, ItemStack offhandStack) {
        var container = new SimpleContainer(offhandStack);
        var optionalRecipe = piglin.level().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), container, piglin.level());
        if (isNotHurt && !offhandStack.is(ItemTags.PIGLIN_REPELLENTS) && !isFood(offhandStack) && optionalRecipe.isPresent()) {
            throwItems(piglin, List.of(optionalRecipe.get().assemble(container, piglin.level().registryAccess())));
            ci.cancel();
        }
    }

    /**
     * Inject {@link PiglinAi#throwItemsTowardRandomPos(Piglin, List)} so that it will trigger
     * PiglinBarteringTrigger for the player interacted with the Piglin.
     *
     * @param piglin the Piglin which will throw items
     * @param stacks the ItemStacks that will be thrown
     * @param ci     Mixin CallbackInfo which is used to cancel the original method
     */
    @Inject(
            method = "throwItemsTowardRandomPos(Lnet/minecraft/world/entity/monster/piglin/Piglin;Ljava/util/List;)V",
            at = @At("HEAD")
    )
    private static void throwItemsTowardRandomPosHandler(Piglin piglin, List<ItemStack> stacks, CallbackInfo ci) {
        piglin.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).ifPresent(livingEntity -> {
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                stacks.forEach(stack -> CrockPotCriteriaTriggers.PIGLIN_BARTERING_TRIGGER.trigger(serverPlayer, stack));
            }
        });
    }

    /**
     * Inject {@link PiglinAi#throwItemsTowardPlayer(Piglin, Player, List)} so that it will trigger
     * PiglinBarteringTrigger for the player that the Piglin will throw items to.
     *
     * @param piglin the Piglin which will throw items
     * @param player the Player who will be thrown items
     * @param stacks the ItemStacks that will be thrown
     * @param ci     Mixin CallbackInfo which is used to cancel the original method
     */
    @Inject(
            method = "throwItemsTowardPlayer(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;)V",
            at = @At("HEAD")
    )
    private static void throwItemsTowardPlayerHandler(Piglin piglin, Player player, List<ItemStack> stacks, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            stacks.forEach(stack -> CrockPotCriteriaTriggers.PIGLIN_BARTERING_TRIGGER.trigger(serverPlayer, stack));
        }
    }

    /**
     * Inject {@link PiglinAi#wantsToPickup(Piglin, ItemStack)} so that Piglin wants to pick up items that can be
     * used for Special Piglin Bartering. Vanilla behavior has the highest priority, so these createVanillaRL behaviors will be kept:
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
     * @param piglin             the Piglin which wants to pick up item
     * @param wantsToPickupStack the ItemStack which will be picked up
     * @param cir                Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "wantsToPickup(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "net/minecraft/world/entity/monster/piglin/Piglin.canReplaceCurrentItem(Lnet/minecraft/world/item/ItemStack;)Z",
                    ordinal = 0
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void wantsToPickupHandler(Piglin piglin, ItemStack wantsToPickupStack, CallbackInfoReturnable<Boolean> cir, boolean canReplaceCurrentItem) {
        // Gold Nugget, Food and not loved item that can be equipped have their own behavior, so it will be skipped
        // TODO: not loved item that can be equipped check
        if (piglin.level().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(wantsToPickupStack), piglin.level()).isPresent()) {
            cir.setReturnValue(isNotHoldingLovedItemInOffHand(piglin));
        }
    }

    /**
     * Inject {@link PiglinAi#mobInteract(Piglin, Player, InteractionHand)} so that Piglin will know who interacts
     * with it. This will be used for PiglinBarteringTrigger.
     *
     * @param piglin the Piglin which is interacted
     * @param player the Player who interacts with the Piglin
     * @param hand   the Hand that used by the Player when interacting with the Piglin
     * @param cir    Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "mobInteract(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            )
    )
    private static void modInteractHandler(Piglin piglin, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        piglin.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, player);
    }

    /**
     * Overwrite(Inject at head of the method and cancel) {@link PiglinAi#canAdmire(Piglin, ItemStack)} so that
     * Piglin will admire items that can be used for Special Piglin Bartering.
     *
     * @param piglin    the Piglin which will admire items
     * @param itemStack the ItemStack which will be admired
     * @param cir       Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "canAdmire(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void canAdmireHandler(Piglin piglin, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        Predicate<ItemStack> isSpecialBarteringStack = stack -> !stack.is(ItemTags.PIGLIN_REPELLENTS) && !isFood(stack) && piglin.level().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(stack), piglin.level()).isPresent();
        cir.setReturnValue(!isAdmiringDisabled(piglin) && !isAdmiringItem(piglin) && piglin.isAdult() && (itemStack.isPiglinCurrency() || isSpecialBarteringStack.test(itemStack)));
    }

    /**
     * Overwrite(Inject at head of the method and cancel) {@link PiglinAi#isPlayerHoldingLovedItem(LivingEntity)} so that
     * Piglin will look at players that holding items that can be used for Special Piglin Bartering.
     *
     * @param livingEntity the LivingEntity holding items
     * @param cir          Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "isPlayerHoldingLovedItem(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void isPlayerHoldingLovedItemHandler(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        Predicate<ItemStack> isSpecialBarteringItem = stack -> !stack.is(ItemTags.PIGLIN_REPELLENTS) && !isFood(stack) && livingEntity.level().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(stack), livingEntity.level()).isPresent();
        cir.setReturnValue(livingEntity.getType() == EntityType.PLAYER && (livingEntity.isHolding(PiglinAiMixin::isLovedItem) || livingEntity.isHolding(isSpecialBarteringItem)));
    }

    /**
     * Overwrite(Inject at head of the method and cancel) {@link PiglinAi#isNotHoldingLovedItemInOffHand(Piglin)}
     * so that the method will return true when Piglin is holding items that can be used for Special Piglin Bartering in offhand.
     *
     * @param piglin the Piglin which is holding items in offhand
     * @param cir    Mixin CallbackInfoReturnable which is used to cancel the original method
     */
    @Inject(
            method = "isNotHoldingLovedItemInOffHand(Lnet/minecraft/world/entity/monster/piglin/Piglin;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void isNotHoldingLovedItemInOffHandHandler(Piglin piglin, CallbackInfoReturnable<Boolean> cir) {
        // To avoid Piglin picking up items during bartering, cancel this method so that it can determine if the item in offhand can be used for PiglinBarteringRecipe
        var offhandStack = piglin.getOffhandItem();
        Predicate<ItemStack> isSpecialBarteringStack = stack -> !stack.is(ItemTags.PIGLIN_REPELLENTS) && !isFood(stack) && piglin.level().getRecipeManager().getRecipeFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SimpleContainer(stack), piglin.level()).isPresent();
        cir.setReturnValue(offhandStack.isEmpty() || (!isLovedItem(offhandStack) && !isSpecialBarteringStack.test(offhandStack)));
    }
}
