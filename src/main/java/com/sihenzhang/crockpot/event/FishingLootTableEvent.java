package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.util.LootTableUtils;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class FishingLootTableEvent {
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(BuiltInLootTables.FISHING_FISH)) {
            LootPoolEntryContainer frogLegsEntry = LootItem.lootTableItem(CrockPotItems.FROG_LEGS.get()).setWeight(25)
                    .when(
                            AnyOfCondition.anyOf(
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
