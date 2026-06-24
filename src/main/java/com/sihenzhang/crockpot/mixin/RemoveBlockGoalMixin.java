package com.sihenzhang.crockpot.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sihenzhang.crockpot.block.food.PowCakeBlock;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RemoveBlockGoal.class)
public abstract class RemoveBlockGoalMixin {
    /**
     * Uses {@link ModItems#POW_CAKE} particles for {@link PowCakeBlock.AnimalEatPowCakeGoal}
     * because vanilla {@link RemoveBlockGoal#tick()} always creates
     * {@link net.minecraft.world.item.Items#EGG} item particles.
     */
    @Definition(id = "ItemParticleOption", type = ItemParticleOption.class)
    @Expression("new ItemParticleOption(?, ?)")
    @WrapOperation(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private ItemParticleOption usePowCakeEatingParticles(ParticleType<ItemParticleOption> type, Item item, Operation<ItemParticleOption> original) {
        if ((RemoveBlockGoal) (Object) this instanceof PowCakeBlock.AnimalEatPowCakeGoal) {
            return new ItemParticleOption(type, ModItems.POW_CAKE.get());
        }
        return original.call(type, item);
    }
}
