package com.sihenzhang.crockpot.loot;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class FishingLootModifier extends LootModifier {
    private final Item item;
    private final int count;

    public FishingLootModifier(LootItemCondition[] conditionsIn, Item item, int count) {
        super(conditionsIn);
        this.item = item;
        this.count = count;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(item, count));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FishingLootModifier> {
        @Override
        public FishingLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            var item = JsonUtils.getAsItem(object, "item");
            var count = GsonHelper.getAsInt(object, "count", 1);
            return new FishingLootModifier(ailootcondition, item, count);
        }

        @Override
        public JsonObject write(FishingLootModifier instance) {
            var result = this.makeConditions(instance.conditions);
            var itemKey = ForgeRegistries.ITEMS.getKey(instance.item).toString();
            result.addProperty("item", itemKey);
            if (instance.count > 1) {
                result.addProperty("count", instance.count);
            }
            return result;
        }
    }
}
