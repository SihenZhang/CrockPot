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

public record RequirementCategoryMin(FoodCategory category, float min) implements IRequirement {
    public static final MapCodec<RequirementCategoryMin> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMin::category),
                    Codec.FLOAT.fieldOf("min").forGetter(RequirementCategoryMin::min)
            ).apply(instance, RequirementCategoryMin::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCategoryMin> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(FoodCategory.class),
            RequirementCategoryMin::category,
            ByteBufCodecs.FLOAT,
            RequirementCategoryMin::min,
            RequirementCategoryMin::new
    );

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.foodValues().get(category) >= min;
    }

    @Override
    public RequirementType type() {
        return RequirementType.CATEGORY_MIN;
    }
}
