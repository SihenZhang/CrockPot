package com.sihenzhang.crockpot.recipe;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.recipe.requirements.Requirement;
import com.sihenzhang.crockpot.recipe.requirements.RequirementType;
import com.sihenzhang.crockpot.recipe.requirements.RequirementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class Recipe implements INBTSerializable<CompoundNBT>, Predicate<RecipeInput> {
    List<Pair<Requirement, RequirementType>> requirements = new LinkedList<>();
    int priority, weight;
    ItemStack result;

    public Recipe(int priority, int weight, ItemStack result) {
        this.priority = priority;
        this.weight = weight;
        this.result = result;
    }

    public void addRequirement(Requirement requirement, RequirementType type) {
        this.requirements.add(new Pair<>(requirement, type));

    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT req = new ListNBT();
        for (Pair<Requirement, RequirementType> p : requirements) {
            CompoundNBT e = new CompoundNBT();
            e.putString("type", p.getSecond().name());
            e.put("requirement", p.getFirst().serializeNBT());
            req.add(e);
        }
        nbt.put("requirements", req);
        nbt.putInt("priority", priority);
        nbt.putInt("weight", weight);
        nbt.put("result", result.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.priority = nbt.getInt("priority");
        this.weight = nbt.getInt("weight");
        this.result = ItemStack.EMPTY.copy();
        result.deserializeNBT((CompoundNBT) nbt.get("result"));
        ListNBT requirements = (ListNBT) nbt.get("requirements");
        assert requirements != null;
        for (INBT r : requirements) {
            CompoundNBT cast = (CompoundNBT) r;
            this.requirements.add(
                    new Pair<>(
                            RequirementUtil.deserialize((CompoundNBT) Objects.requireNonNull(cast.get("requirement"))),
                            RequirementType.valueOf(nbt.getString("type"))
                    )
            );
        }
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        for (Pair<Requirement, RequirementType> req : this.requirements) {
            if (req.getFirst().test(recipeInput)) {
                if (req.getSecond() == RequirementType.SUFFICIENT) return true;
            } else {
                if (req.getSecond() == RequirementType.REQUIRED) return false;
            }
        }
        return true;
    }
}
