package com.sihenzhang.crockpot.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class EatFoodTrigger extends SimpleCriterionTrigger<EatFoodTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack, int count) {
        this.trigger(player, testTrigger -> testTrigger.matches(stack, count));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item,
                                  MinMaxBounds.Ints count) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                builder -> builder.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item),
                                MinMaxBounds.Ints.CODEC.optionalFieldOf("count", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::count)
                        )
                        .apply(builder, TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> eatenItem(ItemPredicate.Builder item, MinMaxBounds.Ints count) {
            return ModCriterionTriggers.EAT_FOOD_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(item.build()), count));
        }

        public boolean matches(ItemStack item, int count) {
            return this.item.isPresent() && this.item.get().test(item) && this.count.matches(count);
        }
    }
}
