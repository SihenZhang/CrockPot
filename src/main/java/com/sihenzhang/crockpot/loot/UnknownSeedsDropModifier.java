package com.sihenzhang.crockpot.loot;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class UnknownSeedsDropModifier extends LootModifier {
    public UnknownSeedsDropModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(CrockPotItems.UNKNOWN_SEEDS.get().getDefaultInstance());
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<UnknownSeedsDropModifier> {
        @Override
        public UnknownSeedsDropModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new UnknownSeedsDropModifier(ailootcondition);
        }

        @Override
        public JsonObject write(UnknownSeedsDropModifier instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
