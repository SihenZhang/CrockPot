package com.sihenzhang.crockpot.recipe.pot.requirement;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import com.sihenzhang.crockpot.util.JsonUtils;
import com.sihenzhang.crockpot.util.NbtUtils;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import java.util.function.Supplier;

public class RequirementMustContainIngredientLessThan implements IRequirement {
    Supplier<Ingredient> ingredient;
    int quantity;

    public RequirementMustContainIngredientLessThan(Supplier<Ingredient> ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public RequirementMustContainIngredientLessThan(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(CrockPotRecipeInput recipeInput) {
        return recipeInput.stacks.stream().filter(stack -> ingredient.get().test(stack)).count() <= quantity;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN.name().toLowerCase());
        try {
            nbt.put(RequirementConstants.INGREDIENT, NbtUtils.writeIngredient(ingredient.get()));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        nbt.putInt(RequirementConstants.QUANTITY, quantity);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        this.ingredient = () -> NbtUtils.readIngredient(nbt.get(RequirementConstants.INGREDIENT));
        this.quantity = nbt.getInt(RequirementConstants.QUANTITY);
    }

    public static RequirementMustContainIngredientLessThan fromJson(JsonObject object) {
        Supplier<Ingredient> ingredient = () -> JsonUtils.getAsIngredient(object, "ingredient", true);
        return new RequirementMustContainIngredientLessThan(ingredient, JSONUtils.getAsInt(object, "quantity"));
    }

    public static RequirementMustContainIngredientLessThan fromNetwork(PacketBuffer buffer) {
        return new RequirementMustContainIngredientLessThan(() -> Ingredient.fromNetwork(buffer), buffer.readByte());
    }

    @Override
    public void toNetwork(PacketBuffer buffer) {
        buffer.writeEnum(RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN);
        this.ingredient.get().toNetwork(buffer);
        buffer.writeByte(this.quantity);
    }
}
