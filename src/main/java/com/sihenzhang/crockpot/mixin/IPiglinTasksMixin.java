package com.sihenzhang.crockpot.mixin;

import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PiglinTasks.class)
public interface IPiglinTasksMixin {
    @Invoker
    static boolean callIsFood(Item p_234499_0_) {
        return true;
    }
}
