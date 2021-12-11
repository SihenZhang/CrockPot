package com.sihenzhang.crockpot.mixin;

import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Piglin.class)
public interface IPiglinMixin {
    @Invoker
    boolean callCanReplaceCurrentItem(ItemStack candidate);
}
