package com.sihenzhang.crockpot.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class PiglinBarteringTrigger extends SimpleCriterionTrigger<PiglinBarteringTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack item) {
        this.trigger(player, testTrigger -> testTrigger.matches(item));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<ItemPredicate> item) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                builder -> builder.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)
                        )
                        .apply(builder, TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> itemPiglinBartered(ItemPredicate.Builder item) {
            return ModCriterionTriggers.PIGLIN_BARTERING_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(item.build())));
        }

        public boolean matches(ItemStack item) {
            return this.item.isEmpty() || this.item.get().test(item);
        }
    }
}
