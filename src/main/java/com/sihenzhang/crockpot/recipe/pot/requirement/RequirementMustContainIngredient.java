package com.sihenzhang.crockpot.recipe.pot.requirement;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import com.sihenzhang.crockpot.util.NbtUtils;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Supplier;

public class RequirementMustContainIngredient implements IRequirement {
    Supplier<Ingredient> ingredient;
    int quantity;

    public RequirementMustContainIngredient(Supplier<Ingredient> ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public RequirementMustContainIngredient(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(CrockPotRecipeInput recipeInput) {
        return recipeInput.stacks.stream().filter(stack -> ingredient.get().test(stack)).count() >= quantity;
    }

    public Supplier<Ingredient> getIngredient() {
		return ingredient;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.MUST_CONTAIN_INGREDIENT.name().toLowerCase());
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
        if (!RequirementType.MUST_CONTAIN_INGREDIENT.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        this.ingredient = () -> NbtUtils.readIngredient(nbt.get(RequirementConstants.INGREDIENT));
        this.quantity = nbt.getInt(RequirementConstants.QUANTITY);
    }
}
