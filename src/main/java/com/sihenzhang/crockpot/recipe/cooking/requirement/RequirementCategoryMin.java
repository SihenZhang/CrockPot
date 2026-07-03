package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class RequirementCategoryMin implements IRequirement {
    public static final MapCodec<RequirementCategoryMin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMin::getCategory),
            Codec.FLOAT.fieldOf("min").forGetter(RequirementCategoryMin::getMin)
    ).apply(instance, RequirementCategoryMin::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCategoryMin> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY),
            RequirementCategoryMin::getCategory,
            ByteBufCodecs.FLOAT,
            RequirementCategoryMin::getMin,
            RequirementCategoryMin::new
    );

    private final Holder<FoodCategory> category;
    private final float min;

    public RequirementCategoryMin(Holder<FoodCategory> category, float min) {
        this.category = category;
        this.min = min;
    }

    public Holder<FoodCategory> getCategory() {
        return category;
    }

    public float getMin() {
        return min;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.CATEGORY_MIN;
    }

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.foodValues().get(category) >= min;
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
