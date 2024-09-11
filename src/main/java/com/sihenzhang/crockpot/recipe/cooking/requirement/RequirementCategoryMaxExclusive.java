package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record RequirementCategoryMaxExclusive(FoodCategory category, float max) implements IRequirement {
    public static final MapCodec<RequirementCategoryMaxExclusive> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMaxExclusive::category),
                    Codec.FLOAT.fieldOf("max").forGetter(RequirementCategoryMaxExclusive::max)
            ).apply(instance, RequirementCategoryMaxExclusive::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCategoryMaxExclusive> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(FoodCategory.class),
            RequirementCategoryMaxExclusive::category,
            ByteBufCodecs.FLOAT,
            RequirementCategoryMaxExclusive::max,
            RequirementCategoryMaxExclusive::new
    );

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.foodValues().get(category) < max;
    }

    @Override
    public RequirementType type() {
        return RequirementType.CATEGORY_MAX_EXCLUSIVE;
    }
}
