package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.loot.functions.Smelt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class HoglinDropLootTableEvent {
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(new ResourceLocation("minecraft:entities/hoglin"))) {
            LootEntry.Builder<?> hoglinNoseEntryBuilder = ItemLootEntry.lootTableItem(CrockPotRegistry.hoglinNose);
            LootPool lootPool = LootPool.lootPool()
                    .name(CrockPot.MOD_ID + "hogin_nose_pool")
                    .setRolls(ConstantRange.exactly(1))
                    .add(hoglinNoseEntryBuilder)
                    .when(KilledByPlayer.killedByPlayer())
                    .when(RandomChanceWithLooting.randomChanceAndLootingBoost(0.3F, 0.03F))
                    .apply(Smelt.smelted().when(EntityHasProperty.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build()))))
                    .build();
            event.getTable().addPool(lootPool);
        }
    }
}
