package com.sihenzhang.crockpot.mixin;

import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PiglinEntity.class)
public interface IPiglinEntityMixin {
    @Invoker
    boolean callCanReplaceCurrentItem(ItemStack p_234440_1_);
}
