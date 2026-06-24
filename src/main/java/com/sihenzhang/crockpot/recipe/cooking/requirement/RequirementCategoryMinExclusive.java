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

public class RequirementCategoryMinExclusive implements IRequirement {
    public static final MapCodec<RequirementCategoryMinExclusive> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FoodCategory.CODEC.fieldOf("category").forGetter(RequirementCategoryMinExclusive::getCategory),
            Codec.FLOAT.fieldOf("min").forGetter(RequirementCategoryMinExclusive::getMin)
    ).apply(instance, RequirementCategoryMinExclusive::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCategoryMinExclusive> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY),
            RequirementCategoryMinExclusive::getCategory,
            ByteBufCodecs.FLOAT,
            RequirementCategoryMinExclusive::getMin,
            RequirementCategoryMinExclusive::new
    );

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
    public RequirementType getType() {
        return RequirementType.CATEGORY_MIN_EXCLUSIVE;
    }

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return recipeWrapper.getFoodValues().get(category) > min;
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
