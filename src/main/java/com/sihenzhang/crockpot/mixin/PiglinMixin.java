package com.sihenzhang.crockpot.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.util.RecipeUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Piglin.class)
public abstract class PiglinMixin extends AbstractPiglin {
    public PiglinMixin(EntityType<? extends AbstractPiglin> type, Level level) {
        super(type, level);
    }

    /**
     * Lets {@link Piglin#holdInOffHand} move custom bartering inputs to the offhand so piglins can
     * admire them like vanilla currency.
     */
    @ModifyExpressionValue(
            method = "holdInOffHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isPiglinCurrency()Z")
    )
    private boolean holdCustomBarteringItemInOffHand(boolean original, ItemStack itemStack) {
        return original || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), this.level()).isPresent();
    }

    /**
     * Uses the admiring arm pose while a piglin is holding a custom bartering input.
     */
    @WrapOperation(
            method = "getArmPose",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isLovedItem(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private boolean useAdmiringPoseForCustomBarteringItem(ItemStack itemStack, Operation<Boolean> original) {
        return original.call(itemStack) || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), this.level()).isPresent();
    }

    /**
     * Makes equipment replacement logic protect custom bartering inputs the same way it protects
     * vanilla loved items.
     */
    @WrapOperation(
            method = "canReplaceCurrentItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isLovedItem(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private boolean treatCustomBarteringItemAsLovedForReplacement(ItemStack itemStack, Operation<Boolean> original) {
        return original.call(itemStack) || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), this.level()).isPresent();
    }
}
