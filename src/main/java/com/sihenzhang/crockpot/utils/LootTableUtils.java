package com.sihenzhang.crockpot.utils;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

public final class LootTableUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void addEntryToLootTable(LootTable lootTable, String poolName, LootEntry... lootEntries) {
        LootPool pool = Objects.requireNonNull(lootTable.getPool(poolName));
        try {
            for (LootEntry lootEntry : lootEntries) {
                addEntryToLootPool(pool, lootEntry);
            }
        } catch (IllegalAccessException e) {
            LOGGER.error("Error occurred when attempting to add a new entry to \"" + lootTable.getLootTableId() + "\" loot table", e);
        }
    }

    public static void addEntryToLootTable(LootTable lootTable, LootEntry... lootEntries) {
        addEntryToLootTable(lootTable, "main", lootEntries);
    }

    private static void addEntryToLootPool(LootPool lootPool, LootEntry lootEntry) throws IllegalAccessException {
        @SuppressWarnings("unchecked") List<LootEntry> lootEntries = (List<LootEntry>) ObfuscationReflectionHelper.findField(LootPool.class, "field_186453_a").get(lootPool);
        if (lootEntries.stream().anyMatch(e -> e == lootEntry)) {
            throw new RuntimeException("Attempted to add a duplicate entry to pool: " + lootEntry);
        }
        lootEntries.add(lootEntry);
    }
}
