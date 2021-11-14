package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.LootTableUtils;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.Alternative;
import net.minecraft.loot.conditions.LocationCheck;
import net.minecraft.world.biome.Biomes;
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
        if (event.getName().equals(LootTables.FISHING_FISH)) {
            LootEntry frogLegsEntry = ItemLootEntry.lootTableItem(CrockPotRegistry.frogLegs).setWeight(25)
                    .when(
                            Alternative.alternative(
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.PLAINS)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.SUNFLOWER_PLAINS)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.SWAMP)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.SWAMP_HILLS)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.FOREST)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.WOODED_HILLS)),
                                    LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.FLOWER_FOREST))
                            )
                    ).build();
            LootTableUtils.addEntryToLootTable(event.getTable(), frogLegsEntry);
        }
    }
}
