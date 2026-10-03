package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class RequirementCategoryMaxExclusive implements IRequirement {
    public static final MapCodec<RequirementCategoryMaxExclusive> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMaxExclusive::getCategory),
            Codec.FLOAT.fieldOf("max").forGetter(RequirementCategoryMaxExclusive::getMax)
    ).apply(instance, RequirementCategoryMaxExclusive::new));

    private final Holder<FoodCategory> category;
    private final float max;

    public RequirementCategoryMaxExclusive(Holder<FoodCategory> category, float max) {
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
        return recipeWrapper.getFoodValues().get(category) < max;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.CATEGORY_MAX_EXCLUSIVE;
    }

    public static RequirementCategoryMaxExclusive fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RequirementCategoryMaxExclusive(FoodCategory.STREAM_CODEC.decode(buffer), buffer.readFloat());
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.CATEGORY_MAX_EXCLUSIVE);
        FoodCategory.STREAM_CODEC.encode(buffer, category);
        buffer.writeFloat(max);
    }
}
