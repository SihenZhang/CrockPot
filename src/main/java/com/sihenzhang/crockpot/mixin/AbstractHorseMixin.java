package com.sihenzhang.crockpot.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {
    /**
     * Allows {@link ModItems#STEAMED_STICKS} to satisfy the {@link AbstractHorse#handleEating}
     * check that normally accepts {@link net.minecraft.world.item.Items#WHEAT}.
     */
    @Definition(id = "is", method = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z")
    @Definition(id = "WHEAT", field = "Lnet/minecraft/world/item/Items;WHEAT:Lnet/minecraft/world/item/Item;")
    @Expression("?.is(WHEAT)")
    @ModifyExpressionValue(method = "handleEating", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean treatSteamedSticksAsHorseFood(boolean original, @Local(argsOnly = true, name = "itemStack") ItemStack itemStack) {
        return original || itemStack.is(ModItems.STEAMED_STICKS);
    }
}
