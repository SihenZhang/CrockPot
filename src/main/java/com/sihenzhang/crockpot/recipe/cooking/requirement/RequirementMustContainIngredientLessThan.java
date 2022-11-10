package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipeInput;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class RequirementMustContainIngredientLessThan implements IRequirement {
    private final Ingredient ingredient;
    private final int quantity;

    public RequirementMustContainIngredientLessThan(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean test(CrockPotCookingRecipeInput recipeInput) {
        return recipeInput.stacks().stream().filter(ingredient).count() <= quantity;
    }

    public static RequirementMustContainIngredientLessThan fromJson(JsonObject object) {
        return new RequirementMustContainIngredientLessThan(JsonUtils.getAsIngredient(object, "ingredient", true), GsonHelper.getAsInt(object, "quantity"));
    }

    @Override
    public JsonElement toJson() {
        var obj = new JsonObject();
        obj.addProperty("type", RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN.name());
        obj.add("ingredient", ingredient.toJson());
        obj.addProperty("quantity", quantity);
        return obj;
    }

    public static RequirementMustContainIngredientLessThan fromNetwork(FriendlyByteBuf buffer) {
        return new RequirementMustContainIngredientLessThan(Ingredient.fromNetwork(buffer), buffer.readByte());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN);
        ingredient.toNetwork(buffer);
        buffer.writeByte(quantity);
    }
}
