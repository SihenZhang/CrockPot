package com.sihenzhang.crockpot.loot;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class CrockPotUnknownSeedsDropModifier extends LootModifier {
    protected CrockPotUnknownSeedsDropModifier(ILootCondition[] conditionsIn) {
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
        public CrockPotUnknownSeedsDropModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new CrockPotUnknownSeedsDropModifier(ailootcondition);
        }

        @Override
        public JsonObject write(CrockPotUnknownSeedsDropModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
