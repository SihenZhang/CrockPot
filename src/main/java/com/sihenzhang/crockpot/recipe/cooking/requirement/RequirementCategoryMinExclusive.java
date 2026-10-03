package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class RequirementCategoryMinExclusive implements IRequirement {
    public static final MapCodec<RequirementCategoryMinExclusive> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMinExclusive::getCategory),
            Codec.FLOAT.fieldOf("min").forGetter(RequirementCategoryMinExclusive::getMin)
    ).apply(instance, RequirementCategoryMinExclusive::new));

    private final Holder<FoodCategory> category;
    private final float min;

    public RequirementCategoryMinExclusive(Holder<FoodCategory> category, float min) {
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
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.getFoodValues().get(category) > min;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.CATEGORY_MIN_EXCLUSIVE;
    }

    public static RequirementCategoryMinExclusive fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RequirementCategoryMinExclusive(FoodCategory.STREAM_CODEC.decode(buffer), buffer.readFloat());
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.CATEGORY_MIN_EXCLUSIVE);
        FoodCategory.STREAM_CODEC.encode(buffer, category);
        buffer.writeFloat(min);
    }
}
