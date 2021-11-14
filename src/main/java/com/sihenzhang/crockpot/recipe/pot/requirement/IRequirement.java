package com.sihenzhang.crockpot.recipe.pot.requirement;

import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Predicate;

public interface IRequirement extends INBTSerializable<CompoundNBT>, Predicate<CrockPotRecipeInput> {
}
