package com.sihenzhang.crockpot.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(
            inst.group(
                    ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item),
                    Codec.INT.fieldOf("count").forGetter(m -> m.count)
            )).apply(inst, AddItemModifier::new)));

    private final Item item;
    private final int count;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int count) {
        super(conditionsIn);
        this.item = item;
        this.count = count;
    }

    @Override
    protected @Nonnull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(item, count));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
