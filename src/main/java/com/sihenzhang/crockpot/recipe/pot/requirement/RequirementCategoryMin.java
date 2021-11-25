package com.sihenzhang.crockpot.recipe.pot.requirement;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import org.apache.commons.lang3.EnumUtils;

public class RequirementCategoryMin implements IRequirement {
    FoodCategory category;
    float min;

    public RequirementCategoryMin(FoodCategory category, float min) {
        this.category = category;
        this.min = min;
    }

    public RequirementCategoryMin(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(CrockPotRecipeInput recipeInput) {
        return recipeInput.mergedFoodValues.get(category) >= min;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.CATEGORY_MIN.name().toLowerCase());
        nbt.putString(RequirementConstants.CATEGORY, category.name());
        nbt.putFloat(RequirementConstants.MIN, min);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!RequirementType.CATEGORY_MIN.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        this.category = EnumUtils.getEnum(FoodCategory.class, nbt.getString(RequirementConstants.CATEGORY).toUpperCase());
        this.min = nbt.getFloat(RequirementConstants.MIN);
    }

    public static RequirementCategoryMin fromJson(JsonObject object) {
        return new RequirementCategoryMin(JsonUtils.getAsEnum(object, "category", FoodCategory.class), JSONUtils.getAsFloat(object, "min"));
    }

    public static RequirementCategoryMin fromNetwork(PacketBuffer buffer) {
        return new RequirementCategoryMin(buffer.readEnum(FoodCategory.class), buffer.readFloat());
    }

    @Override
    public void toNetwork(PacketBuffer buffer) {
        buffer.writeEnum(RequirementType.CATEGORY_MIN);
        buffer.writeEnum(this.category);
        buffer.writeFloat(this.min);
    }
}
