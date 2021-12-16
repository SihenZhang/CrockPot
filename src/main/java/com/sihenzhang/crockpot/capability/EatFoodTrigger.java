package com.sihenzhang.crockpot.capability;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class EatFoodTrigger extends AbstractCriterionTrigger<EatFoodTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(CrockPot.MOD_ID, "eat_food");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        MinMaxBounds.IntBound count = MinMaxBounds.IntBound.fromJson(json.get("count"));
        return new EatFoodTrigger.Instance(entityPredicate, itemPredicate, count);
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack, int count) {
        this.trigger(player, testTrigger -> testTrigger.matches(player, stack, count));
    }

    public static class Instance extends CriterionInstance {
        private final ItemPredicate item;
        private final MinMaxBounds.IntBound count;

        public Instance(EntityPredicate.AndPredicate player, ItemPredicate item, MinMaxBounds.IntBound count) {
            super(EatFoodTrigger.ID, player);
            this.item = item;
            this.count = count;
        }

        public boolean matches(ServerPlayerEntity player, ItemStack stack, int count) {
            return this.item.matches(stack) && this.count.matches(count);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer conditions) {
            JsonObject conditionsJson = super.serializeToJson(conditions);
            conditionsJson.add("item", this.item.serializeToJson());
            conditionsJson.add("count", this.count.serializeToJson());
            return conditionsJson;
        }
    }
}
