package com.sihenzhang.crockpot.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.util.RecipeUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.piglin.StartAdmiringItemIfSeen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StartAdmiringItemIfSeen.class)
public abstract class StartAdmiringItemIfSeenMixin {
    /**
     * Allows the behavior that starts admiration for visible loved items to also react to custom
     * bartering inputs.
     */
    @WrapOperation(
            method = "lambda$create$2",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isLovedItem(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private static boolean treatVisibleCustomBarteringItemAsLoved(ItemStack itemStack, Operation<Boolean> original, @Local(argsOnly = true, name = "level") ServerLevel level) {
        return original.call(itemStack) || RecipeUtil.getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), new SingleRecipeInput(itemStack), level).isPresent();
    }
}
