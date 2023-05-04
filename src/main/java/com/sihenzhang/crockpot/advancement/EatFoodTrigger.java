package com.sihenzhang.crockpot.advancement;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class EatFoodTrigger extends SimpleCriterionTrigger<EatFoodTrigger.Instance> {
    private static final ResourceLocation ID = RLUtils.createRL("eat_food");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        var itemPredicate = ItemPredicate.fromJson(json.get("item"));
        var count = MinMaxBounds.Ints.fromJson(json.get("count"));
        return new Instance(entityPredicate, itemPredicate, count);
    }

    public void trigger(ServerPlayer player, ItemStack stack, int count) {
        this.trigger(player, testTrigger -> testTrigger.matches(player, stack, count));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        private final MinMaxBounds.Ints count;

        public Instance(EntityPredicate.Composite player, ItemPredicate item, MinMaxBounds.Ints count) {
            super(EatFoodTrigger.ID, player);
            this.item = item;
            this.count = count;
        }

        public boolean matches(ServerPlayer player, ItemStack stack, int count) {
            return this.item.matches(stack) && this.count.matches(count);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            var conditionsJson = super.serializeToJson(conditions);
            conditionsJson.add("item", this.item.serializeToJson());
            conditionsJson.add("count", this.count.serializeToJson());
            return conditionsJson;
        }
    }
}
