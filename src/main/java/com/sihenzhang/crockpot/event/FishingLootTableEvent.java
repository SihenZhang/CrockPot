package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.LootTableUtils;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class FishingLootTableEvent {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(BuiltInLootTables.FISHING_FISH)) {
            LootPoolEntryContainer frogLegsEntry = LootItem.lootTableItem(CrockPotRegistry.frogLegs).setWeight(25)
                    .when(
                            AlternativeLootItemCondition.alternative(
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.PLAINS)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.SUNFLOWER_PLAINS)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.SWAMP)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.FOREST)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.FLOWER_FOREST))
                            )
                    ).build();
            LootTableUtils.addEntryToLootTable(event.getTable(), frogLegsEntry);
        }
    }
}
