package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipeInput;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class RequirementMustContainIngredient implements IRequirement {
    private final Ingredient ingredient;
    private final int quantity;

    public RequirementMustContainIngredient(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = Math.min(quantity, 4);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean test(CrockPotCookingRecipeInput recipeInput) {
        return recipeInput.stacks.stream().filter(ingredient).count() >= quantity;
    }

    public static RequirementMustContainIngredient fromJson(JsonObject object) {
        return new RequirementMustContainIngredient(JsonUtils.getAsIngredient(object, "ingredient", true), JSONUtils.getAsInt(object, "quantity"));
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", RequirementType.MUST_CONTAIN_INGREDIENT.name());
        obj.add("ingredient", ingredient.toJson());
        obj.addProperty("quantity", quantity);
        return obj;
    }

    public static RequirementMustContainIngredient fromNetwork(PacketBuffer buffer) {
        return new RequirementMustContainIngredient(Ingredient.fromNetwork(buffer), buffer.readByte());
    }

    @Override
    public void toNetwork(PacketBuffer buffer) {
        buffer.writeEnum(RequirementType.MUST_CONTAIN_INGREDIENT);
        ingredient.toNetwork(buffer);
        buffer.writeByte(quantity);
    }
}
