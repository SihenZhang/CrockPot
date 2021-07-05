package com.sihenzhang.crockpot.mixin;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;

@Mixin(PiglinTasks.class)
public abstract class PiglinTasksMixin {
    @Shadow
    private static void holdInOffhand(PiglinEntity p_241427_0_, ItemStack p_241427_1_) {
    }

    @Shadow
    private static void throwItems(PiglinEntity p_234475_0_, List<ItemStack> p_234475_1_) {
    }

    @Shadow
    private static boolean isLovedItem(Item p_234480_0_) {
        return true;
    }

    @Shadow
    private static void admireGoldItem(LivingEntity p_234501_0_) {
    }

    @Shadow
    private static boolean isFood(Item p_234499_0_) {
        return true;
    }

    @Shadow
    private static boolean isNotHoldingLovedItemInOffHand(PiglinEntity p_234455_0_) {
        return true;
    }

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
    private static void pickUpItemHandler(PiglinEntity p_234470_0_, ItemEntity p_234470_1_, CallbackInfo ci, ItemStack itemstack, Item item, boolean flag) {
        // Gold Nugget will be put into the inventory, vanilla behavior should be prioritised above our own behavior
        if (!flag && item != Items.GOLD_NUGGET && CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.matches(itemstack) != null) {
            p_234470_0_.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(p_234470_0_, itemstack);
            admireGoldItem(p_234470_0_);
            ci.cancel();
        }
    }

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
    private static void stopHoldingOffHandItemHandler(PiglinEntity p_234477_0_, boolean p_234477_1_, CallbackInfo ci, ItemStack itemstack, boolean flag, boolean flag1) {
        PiglinBarteringRecipe recipe;
        if (p_234477_1_ && !flag1 && (recipe = CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.matches(itemstack)) != null) {
            throwItems(p_234477_0_, Collections.singletonList(recipe.createOutput()));
            ci.cancel();
        }
    }

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
    private static void wantsToPickupHandler(PiglinEntity p_234474_0_, ItemStack p_234474_1_, CallbackInfoReturnable<Boolean> cir, Item item, boolean flag) {
        // Gold Nugget, Food and not loved item that can be equipped have their own behavior, so it will be skipped
        if (item != Items.GOLD_NUGGET && !isFood(item) && !(!isLovedItem(item) && ((IPiglinEntityMixin) p_234474_0_).callCanReplaceCurrentItem(p_234474_1_)) && CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.matches(p_234474_1_) != null) {
            cir.setReturnValue(isNotHoldingLovedItemInOffHand(p_234474_0_));
        }
    }

    @Inject(
            method = "isNotHoldingLovedItemInOffHand(Lnet/minecraft/entity/monster/piglin/PiglinEntity;)Z",
            at = @At("TAIL"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void isNotHoldingLovedItemInOffHandHandler(PiglinEntity p_234455_0_, CallbackInfoReturnable<Boolean> cir) {
        // To avoid Piglin picking up items during bartering, cancel this method so that it can determine if the item in off hand can be used for PiglinBarteringRecipe
        cir.setReturnValue(p_234455_0_.getOffhandItem().isEmpty() || (!isLovedItem(p_234455_0_.getOffhandItem().getItem()) && CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.matches(p_234455_0_.getOffhandItem()) == null));
    }
}
