package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipeInput;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class RequirementCategoryMinExclusive implements IRequirement {
    private final FoodCategory category;
    private final float min;

    public RequirementCategoryMinExclusive(FoodCategory category, float min) {
        this.category = category;
        this.min = min;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public float getMin() {
        return min;
    }

    @Override
    public boolean test(CrockPotCookingRecipeInput recipeInput) {
        return recipeInput.mergedFoodValues.get(category) > min;
    }

    public static RequirementCategoryMinExclusive fromJson(JsonObject object) {
        return new RequirementCategoryMinExclusive(JsonUtils.getAsEnum(object, "category", FoodCategory.class), GsonHelper.getAsFloat(object, "min"));
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", RequirementType.CATEGORY_MIN_EXCLUSIVE.name());
        obj.addProperty("category", category.name());
        obj.addProperty("min", min);
        return obj;
    }

    public static RequirementCategoryMinExclusive fromNetwork(FriendlyByteBuf buffer) {
        return new RequirementCategoryMinExclusive(buffer.readEnum(FoodCategory.class), buffer.readFloat());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.CATEGORY_MIN_EXCLUSIVE);
        buffer.writeEnum(category);
        buffer.writeFloat(min);
    }
}
