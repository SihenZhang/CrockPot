package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class RequirementCategoryMax implements IRequirement {
    public static final MapCodec<RequirementCategoryMax> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMax::getCategory),
            Codec.FLOAT.fieldOf("max").forGetter(RequirementCategoryMax::getMax)
    ).apply(instance, RequirementCategoryMax::new));

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
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.getFoodValues().get(category) <= max;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.CATEGORY_MAX;
    }

    public static RequirementCategoryMax fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RequirementCategoryMax(FoodCategory.STREAM_CODEC.decode(buffer), buffer.readFloat());
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.CATEGORY_MAX);
        FoodCategory.STREAM_CODEC.encode(buffer, category);
        buffer.writeFloat(max);
    }
}
