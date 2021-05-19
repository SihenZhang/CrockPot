package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.Alternative;
import net.minecraft.loot.conditions.LocationCheck;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class FishingLootTableEvent {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();
        if (name.equals(LootTables.GAMEPLAY_FISHING_FISH)) {
            LootPool pool = event.getTable().getPool("main");
            if (pool != null) {
                LootEntry frogLegsEntry = ItemLootEntry.builder(CrockPotRegistry.frogLegs).weight(25)
                        .acceptCondition(Alternative.builder(
                                LocationCheck.builder(LocationPredicate.Builder.builder().biome(Biomes.PLAINS)),
                                LocationCheck.builder(LocationPredicate.Builder.builder().biome(Biomes.SUNFLOWER_PLAINS)),
                                LocationCheck.builder(LocationPredicate.Builder.builder().biome(Biomes.SWAMP)),
                                LocationCheck.builder(LocationPredicate.Builder.builder().biome(Biomes.SWAMP_HILLS)),
                                LocationCheck.builder(LocationPredicate.Builder.builder().biome(Biomes.FOREST)),
                                LocationCheck.builder(LocationPredicate.Builder.builder().biome(Biomes.WOODED_HILLS)),
                                LocationCheck.builder(LocationPredicate.Builder.builder().biome(Biomes.FLOWER_FOREST)))
                        ).build();
                addEntry(pool, frogLegsEntry);
            }
        }
    }

    private static void addEntry(LootPool pool, LootEntry entry) {
        try {
            List<LootEntry> lootEntries = (List<LootEntry>) ObfuscationReflectionHelper.findField(LootPool.class, "field_186453_a").get(pool);
            if (lootEntries.stream().anyMatch(e -> e == entry)) {
                throw new RuntimeException("Attempted to add a duplicate entry to pool: " + entry);
            }
            lootEntries.add(entry);
        } catch (IllegalAccessException e) {
            LOGGER.error("Error occurred when attempting to add a new entry, to the fishing loot table", e);
        }
    }
}
