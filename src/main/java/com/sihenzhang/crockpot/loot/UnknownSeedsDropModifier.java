package com.sihenzhang.crockpot.loot;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.item.CrockPotItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class UnknownSeedsDropModifier extends LootModifier {

    public static final RegistryObject<Codec<UnknownSeedsDropModifier>> UNKNOWN_SEEDS_DROP
            = CrockPotLootModifiers.LOOT_MODIFIER_SERIALIZERS.register("unknown_seeds_drop",
                    () -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, UnknownSeedsDropModifier::new)));
    public UnknownSeedsDropModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(CrockPotItems.UNKNOWN_SEEDS.get().getDefaultInstance());
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return UNKNOWN_SEEDS_DROP.get();
    }

}
