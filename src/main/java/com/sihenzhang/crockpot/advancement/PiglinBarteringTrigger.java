package com.sihenzhang.crockpot.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class PiglinBarteringTrigger extends SimpleCriterionTrigger<PiglinBarteringTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, testTrigger -> testTrigger.matches(player, stack));
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

        public boolean matches(ServerPlayer player, ItemStack stack) {
            return this.item.isPresent() && this.item.get().test(stack);
        }
    }
}
