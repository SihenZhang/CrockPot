package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.capability.EatFoodTrigger;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrockPotCriteriaTriggers {
    public static PiglinBarteringTrigger PIGLIN_BARTERING_TRIGGER;
    public static EatFoodTrigger EAT_FOOD_TRIGGER;

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PIGLIN_BARTERING_TRIGGER = CriteriaTriggers.register(new PiglinBarteringTrigger());
            EAT_FOOD_TRIGGER = CriteriaTriggers.register(new EatFoodTrigger());
        });
    }
}
