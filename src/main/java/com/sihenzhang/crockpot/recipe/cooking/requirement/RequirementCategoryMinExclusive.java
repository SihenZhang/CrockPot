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

public record RequirementCategoryMinExclusive(FoodCategory category, float min) implements IRequirement {
    public static final MapCodec<RequirementCategoryMinExclusive> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMinExclusive::category),
                    Codec.FLOAT.fieldOf("min").forGetter(RequirementCategoryMinExclusive::min)
            ).apply(instance, RequirementCategoryMinExclusive::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCategoryMinExclusive> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(FoodCategory.class),
            RequirementCategoryMinExclusive::category,
            ByteBufCodecs.FLOAT,
            RequirementCategoryMinExclusive::min,
            RequirementCategoryMinExclusive::new
    );

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.foodValues().get(category) > min;
    }

    @Override
    public RequirementType type() {
        return RequirementType.CATEGORY_MIN_EXCLUSIVE;
    }
}
