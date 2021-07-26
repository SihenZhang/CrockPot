package com.sihenzhang.crockpot.recipe.pot.requirements;

import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Predicate;

public abstract class Requirement implements INBTSerializable<CompoundNBT>, Predicate<CrockPotRecipeInput> {
}
