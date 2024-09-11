package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public enum RequirementType implements StringRepresentable {
    CATEGORY_MAX("CATEGORY_MAX", () -> RequirementCategoryMax.CODEC, () -> RequirementCategoryMax.STREAM_CODEC),
    CATEGORY_MAX_EXCLUSIVE("CATEGORY_MAX_EXCLUSIVE", () -> RequirementCategoryMaxExclusive.CODEC, () -> RequirementCategoryMaxExclusive.STREAM_CODEC),
    CATEGORY_MIN("CATEGORY_MIN", () -> RequirementCategoryMin.CODEC, () -> RequirementCategoryMin.STREAM_CODEC),
    CATEGORY_MIN_EXCLUSIVE("CATEGORY_MIN_EXCLUSIVE", () -> RequirementCategoryMinExclusive.CODEC, () -> RequirementCategoryMinExclusive.STREAM_CODEC),
    MUST_CONTAIN_INGREDIENT("MUST_CONTAIN_INGREDIENT", () -> RequirementMustContainIngredient.CODEC, () -> RequirementMustContainIngredient.STREAM_CODEC),
    MUST_CONTAIN_INGREDIENT_LESS_THAN("MUST_CONTAIN_INGREDIENT_LESS_THAN", () -> RequirementMustContainIngredientLessThan.CODEC, () -> RequirementMustContainIngredientLessThan.STREAM_CODEC),
    COMBINATION_AND("COMBINATION_AND", () -> RequirementCombinationAnd.CODEC, () -> RequirementCombinationAnd.STREAM_CODEC),
    COMBINATION_OR("COMBINATION_OR", () -> RequirementCombinationOr.CODEC, () -> RequirementCombinationOr.STREAM_CODEC);

    public static final Codec<RequirementType> CODEC = StringRepresentable.fromEnum(RequirementType::values);

    private final String name;
    private final Supplier<MapCodec<? extends IRequirement>> codec;
    private final Supplier<StreamCodec<RegistryFriendlyByteBuf, ? extends IRequirement>> streamCodec;

    RequirementType(String name, Supplier<MapCodec<? extends IRequirement>> codec, Supplier<StreamCodec<RegistryFriendlyByteBuf, ? extends IRequirement>> streamCodec) {
        this.name = name;
        this.codec = codec;
        this.streamCodec = streamCodec;
    }

    public MapCodec<? extends IRequirement> codec() {
        return codec.get();
    }

    public StreamCodec<RegistryFriendlyByteBuf, ? extends IRequirement> streamCodec() {
        return streamCodec.get();
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
