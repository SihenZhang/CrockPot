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

public class RequirementMustContainIngredient implements IRequirement {
    public static final MapCodec<RequirementMustContainIngredient> CODEC = RecipeCodecs.object(
            json -> fromJson(json.getAsJsonObject()), RequirementMustContainIngredient::toJson, "ingredient", "quantity");

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
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return IntStream.range(0, recipeWrapper.getContainerSize()).mapToObj(recipeWrapper::getItem).filter(ingredient).count() >= quantity;
    }

    public static RequirementMustContainIngredient fromJson(JsonObject object) {
        return new RequirementMustContainIngredient(JsonUtils.getAsIngredient(object, "ingredient", true), GsonHelper.getAsInt(object, "quantity"));
    }

    @Override
    public RequirementType getType() {
        return RequirementType.MUST_CONTAIN_INGREDIENT;
    }

    public JsonElement toJson() {
        var obj = new JsonObject();
        obj.add("ingredient", Ingredient.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, ingredient).getOrThrow());
        obj.addProperty("quantity", quantity);
        return obj;
    }

    public static RequirementMustContainIngredient fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RequirementMustContainIngredient(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readByte());
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.MUST_CONTAIN_INGREDIENT);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        buffer.writeByte(quantity);
    }
}
