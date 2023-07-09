package com.sihenzhang.crockpot.util;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class LootTableUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void addEntryToLootTable(LootTable lootTable, String poolName, LootPoolEntryContainer... lootEntries) {
//        LootPool pool = Objects.requireNonNull(lootTable.getPool(poolName));
//        try {
//            for (LootPoolEntryContainer lootEntry : lootEntries) {
//                addEntryToLootPool(pool, lootEntry);
//            }
//        } catch (IllegalAccessException e) {
//            LOGGER.error("Error occurred when attempting to add a new entry to \"" + lootTable.getLootTableId() + "\" loot table", e);
//        }
    }

    public static void addEntryToLootTable(LootTable lootTable, LootPoolEntryContainer... lootEntries) {
        addEntryToLootTable(lootTable, "main", lootEntries);
    }

    private static void addEntryToLootPool(LootPool lootPool, LootPoolEntryContainer lootEntry) throws IllegalAccessException {
        Field entries = ObfuscationReflectionHelper.findField(LootPool.class, "f_79023_");
        LootPoolEntryContainer[] lootPoolEntriesArray = (LootPoolEntryContainer[]) entries.get(lootPool);
        List<LootPoolEntryContainer> newLootEntries = new ArrayList<>(List.of(lootPoolEntriesArray));
        if (newLootEntries.stream().anyMatch(e -> e == lootEntry)) {
            throw new RuntimeException("Attempted to add a duplicate entry to pool: " + lootEntry);
        }
        newLootEntries.add(lootEntry);
        LootPoolEntryContainer[] newLootEntriesArray = newLootEntries.toArray(new LootPoolEntryContainer[0]);
        entries.set(lootPool, newLootEntriesArray);
    }
}
