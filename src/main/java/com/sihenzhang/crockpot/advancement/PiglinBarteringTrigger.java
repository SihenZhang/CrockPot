package com.sihenzhang.crockpot.advancement;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class PiglinBarteringTrigger extends SimpleCriterionTrigger<PiglinBarteringTrigger.Instance> {
    private static final ResourceLocation ID = RLUtils.createRL("piglin_bartering");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        var itemPredicate = ItemPredicate.fromJson(json.get("item"));
        return new PiglinBarteringTrigger.Instance(entityPredicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, testTrigger -> testTrigger.matches(player, stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public Instance(EntityPredicate.Composite player, ItemPredicate item) {
            super(PiglinBarteringTrigger.ID, player);
            this.item = item;
        }

        public boolean matches(ServerPlayer player, ItemStack stack) {
            return this.item.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            var conditionsJson = super.serializeToJson(conditions);
            conditionsJson.add("item", this.item.serializeToJson());
            return conditionsJson;
        }
    }
}
