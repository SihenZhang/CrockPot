package com.sihenzhang.crockpot.recipe.bartering;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class PiglinBarteringTrigger extends AbstractCriterionTrigger<PiglinBarteringTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(CrockPot.MOD_ID, "piglin_bartering");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        return new PiglinBarteringTrigger.Instance(entityPredicate, itemPredicate);
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, testTrigger -> testTrigger.matches(player, stack));
    }

    public static class Instance extends CriterionInstance {
        private final ItemPredicate item;

        public Instance(EntityPredicate.AndPredicate player, ItemPredicate item) {
            super(PiglinBarteringTrigger.ID, player);
            this.item = item;
        }

        public boolean matches(ServerPlayerEntity player, ItemStack stack) {
            return this.item.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer conditions) {
            JsonObject conditionsJson = super.serializeToJson(conditions);
            conditionsJson.add("item", this.item.serializeToJson());
            return conditionsJson;
        }
    }
}
