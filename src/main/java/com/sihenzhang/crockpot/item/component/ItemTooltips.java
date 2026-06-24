package com.sihenzhang.crockpot.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.function.Consumer;

public record ItemTooltips(List<Component> lines) implements TooltipProvider {
    public static final ItemTooltips EMPTY = new ItemTooltips(List.of());
    public static final int MAX_LINES = 256;
    public static final Codec<ItemTooltips> CODEC = ComponentSerialization.CODEC.sizeLimitedListOf(MAX_LINES)
            .xmap(ItemTooltips::new, ItemTooltips::lines);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemTooltips> STREAM_CODEC = ComponentSerialization.STREAM_CODEC
            .apply(ByteBufCodecs.list(MAX_LINES))
            .map(ItemTooltips::new, ItemTooltips::lines);

    public ItemTooltips {
        if (lines.size() > MAX_LINES) {
            throw new IllegalArgumentException("Got " + lines.size() + " lines, but maximum is " + MAX_LINES);
        }
    }

    public ItemTooltips withLineAdded(Component component) {
        return new ItemTooltips(Util.copyAndAdd(this.lines, component));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        lines.forEach(consumer);
    }
}
