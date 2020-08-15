package com.sihenzhang.crockpot.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class CrockPotSeedsDropModifier extends LootModifier {
    private final Item seed;

    protected CrockPotSeedsDropModifier(ILootCondition[] conditionsIn, Item seed) {
        super(conditionsIn);
        this.seed = seed;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(this.seed));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CrockPotSeedsDropModifier> {
        @Override
        public CrockPotSeedsDropModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            Item seed = JSONUtils.getItem(object, "seed");
            return new CrockPotSeedsDropModifier(ailootcondition, seed);
        }
    }
}
