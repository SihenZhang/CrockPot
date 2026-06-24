package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class RequirementCategoryMax implements IRequirement {
    public static final MapCodec<RequirementCategoryMax> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMax::getCategory),
            Codec.FLOAT.fieldOf("max").forGetter(RequirementCategoryMax::getMax)
    ).apply(instance, RequirementCategoryMax::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCategoryMax> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY),
            RequirementCategoryMax::getCategory,
            ByteBufCodecs.FLOAT,
            RequirementCategoryMax::getMax,
            RequirementCategoryMax::new
    );

    private final Holder<FoodCategory> category;
    private final float max;

    public RequirementCategoryMax(Holder<FoodCategory> category, float max) {
        this.category = category;
        this.max = max;
    }

    public Holder<FoodCategory> getCategory() {
        return category;
    }

    public float getMax() {
        return max;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.CATEGORY_MAX;
    }

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.getFoodValues().get(category) <= max;
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
