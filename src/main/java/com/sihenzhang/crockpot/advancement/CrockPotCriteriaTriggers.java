package com.sihenzhang.crockpot.advancement;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CrockPotCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, CrockPot.MOD_ID);
    public static final DeferredHolder<CriterionTrigger<?>, PiglinBarteringTrigger> PIGLIN_BARTERING_TRIGGER = TRIGGERS.register("piglin_bartering", PiglinBarteringTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, EatFoodTrigger> EAT_FOOD_TRIGGER = TRIGGERS.register("eat_food", EatFoodTrigger::new);
}
