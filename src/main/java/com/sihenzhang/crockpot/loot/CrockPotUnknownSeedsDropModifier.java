package com.sihenzhang.crockpot.loot;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class CrockPotUnknownSeedsDropModifier extends LootModifier {
    protected CrockPotUnknownSeedsDropModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (CrockPotConfig.ENABLE_UNKNOWN_SEEDS.get()) {
            generatedLoot.add(CrockPotRegistry.unknownSeeds.get().getDefaultInstance());
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CrockPotUnknownSeedsDropModifier> {
        @Override
        public CrockPotUnknownSeedsDropModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new CrockPotUnknownSeedsDropModifier(ailootcondition);
        }

        @Override
        public JsonObject write(CrockPotUnknownSeedsDropModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
