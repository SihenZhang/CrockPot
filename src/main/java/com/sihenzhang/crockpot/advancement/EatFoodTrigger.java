package com.sihenzhang.crockpot.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class EatFoodTrigger extends SimpleCriterionTrigger<EatFoodTrigger.Instance> {
    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack, int count) {
        this.trigger(player, instance -> instance.matches(stack, count));
    }

    public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item,
                           MinMaxBounds.Ints count) implements SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Instance::item),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("count", MinMaxBounds.Ints.ANY).forGetter(Instance::count)
        ).apply(inst, Instance::new));

        public boolean matches(ItemStack stack, int count) {
            return (this.item.isEmpty() || this.item.get().test(stack)) && this.count.matches(count);
        }
    }
}
