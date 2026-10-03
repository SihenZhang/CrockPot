package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.recipe.RecipeCodecs;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.IntStream;

public class RequirementMustContainIngredientLessThan implements IRequirement {
    public static final MapCodec<RequirementMustContainIngredientLessThan> CODEC = RecipeCodecs.object(
            json -> fromJson(json.getAsJsonObject()), RequirementMustContainIngredientLessThan::toJson, "ingredient", "quantity");

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
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return IntStream.range(0, recipeWrapper.getContainerSize()).mapToObj(recipeWrapper::getItem).filter(ingredient).count() <= quantity;
    }

    public static RequirementMustContainIngredientLessThan fromJson(JsonObject object) {
        return new RequirementMustContainIngredientLessThan(JsonUtils.getAsIngredient(object, "ingredient", true), GsonHelper.getAsInt(object, "quantity"));
    }

    @Override
    public RequirementType getType() {
        return RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN;
    }

    public JsonElement toJson() {
        var obj = new JsonObject();
        obj.add("ingredient", Ingredient.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, ingredient).getOrThrow());
        obj.addProperty("quantity", quantity);
        return obj;
    }

    public static RequirementMustContainIngredientLessThan fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RequirementMustContainIngredientLessThan(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readByte());
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        buffer.writeByte(quantity);
    }
}
