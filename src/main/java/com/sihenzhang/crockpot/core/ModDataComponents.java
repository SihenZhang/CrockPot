package com.sihenzhang.crockpot.core;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.component.ItemTooltips;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ModDataComponents {
    private ModDataComponents() {}

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CrockPot.MOD_ID);

    public static final Supplier<DataComponentType<ItemTooltips>> ITEM_TOOLTIPS = register(
            "item_tooltips",
            builder -> builder.persistent(ItemTooltips.CODEC).networkSynchronized(ItemTooltips.STREAM_CODEC).cacheEncoding()
    );
    public static final Supplier<DataComponentType<Unit>> CONSUMABLE_TOOLTIPS = register(
            "consumable_tooltips",
            builder -> builder.persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding()
    );

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENTS.registerComponentType(name, builder);
    }
}
