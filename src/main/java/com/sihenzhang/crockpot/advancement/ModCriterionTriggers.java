package com.sihenzhang.crockpot.advancement;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModCriterionTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, CrockPot.MOD_ID);

    public static final Supplier<PiglinBarteringTrigger> PIGLIN_BARTERING_TRIGGER = TRIGGERS.register("piglin_bartering", PiglinBarteringTrigger::new);
    public static final Supplier<EatFoodTrigger> EAT_FOOD_TRIGGER = TRIGGERS.register("eat_food", EatFoodTrigger::new);
}
