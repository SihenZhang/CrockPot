package com.sihenzhang.crockpot.recipe.cooking;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.AbstractCrockPotRecipe;
import com.sihenzhang.crockpot.recipe.CrockPotRecipeTypes;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class CrockPotCookingRecipe extends AbstractCrockPotRecipe {
    private final List<IRequirement> requirements;
    private final ItemStack result;
    private final int priority, weight, cookingTime, potLevel;

    public CrockPotCookingRecipe(ResourceLocation id, List<IRequirement> requirements, ItemStack result, int priority, int weight, int cookingTime, int potLevel) {
        super(id);
        this.requirements = ImmutableList.copyOf(requirements);
        this.result = result;
        this.priority = priority;
        this.weight = Math.max(weight, 1);
        this.cookingTime = cookingTime;
        this.potLevel = potLevel;
    }

    public List<IRequirement> getRequirements() {
        return requirements;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getPriority() {
        return priority;
    }

    public int getWeight() {
        return weight;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getPotLevel() {
        return potLevel;
    }

    public boolean matches(CrockPotCookingRecipeInput input) {
        return input.potLevel >= this.potLevel && this.requirements.stream().allMatch(r -> r.test(input));
    }

    @Nullable
    public static CrockPotCookingRecipe getRecipeFor(CrockPotCookingRecipeInput input, Random random, RecipeManager recipeManager) {
        List<CrockPotCookingRecipe> recipes = recipeManager.getAllRecipesFor(CrockPotRecipeTypes.CROCK_POT_COOKING_RECIPE_TYPE);
        recipes.sort(Comparator.comparing(CrockPotCookingRecipe::getPriority).reversed());
        List<CrockPotCookingRecipe> matchedRecipes = new ArrayList<>();
        boolean isFirst = true;
        int priority = 0;
        for (CrockPotCookingRecipe recipe : recipes) {
            if (isFirst) {
                if (recipe.matches(input)) {
                    priority = recipe.getPriority();
                    matchedRecipes.add(recipe);
                    isFirst = false;
                }
            } else {
                if (recipe.getPriority() != priority) {
                    break;
                } else {
                    if (recipe.matches(input)) {
                        matchedRecipes.add(recipe);
                    }
                }
            }
        }
        if (matchedRecipes.isEmpty()) {
            return null;
        }
        int weightSum = matchedRecipes.stream().mapToInt(CrockPotCookingRecipe::getWeight).sum();
        int rand = random.nextInt(weightSum) + 1;
        for (CrockPotCookingRecipe matchedRecipe : matchedRecipes) {
            rand -= matchedRecipe.getWeight();
            if (rand <= 0) {
                return matchedRecipe;
            }
        }
        return null;
    }

    public ItemStack assemble() {
        return this.result.copy();
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return CrockPotRegistry.crockPotCooking;
    }

    @Override
    public IRecipeType<?> getType() {
        return CrockPotRecipeTypes.CROCK_POT_COOKING_RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrockPotCookingRecipe> {
        @Override
        public CrockPotCookingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            List<IRequirement> requirements = new ArrayList<>();
            JSONUtils.getAsJsonArray(serializedRecipe, "requirements").forEach(requirement -> {
                JsonObject object = JSONUtils.convertToJsonObject(requirement, "requirement");
                requirements.add(IRequirement.fromJson(object));
            });
            ItemStack result = JsonUtils.getAsItemStack(serializedRecipe, "result");
            int priority = JSONUtils.getAsInt(serializedRecipe, "priority");
            int weight = JSONUtils.getAsInt(serializedRecipe, "weight", 1);
            int cookingTime = JSONUtils.getAsInt(serializedRecipe, "cookingtime");
            int potLevel = JSONUtils.getAsInt(serializedRecipe, "potlevel");
            return new CrockPotCookingRecipe(recipeId, requirements, result, priority, weight, cookingTime, potLevel);
        }

        @Nullable
        @Override
        public CrockPotCookingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            List<IRequirement> requirements = new ArrayList<>();
            int length = buffer.readVarInt();
            for (int i = 0; i < length; i++) {
                requirements.add(IRequirement.fromNetwork(buffer));
            }
            ItemStack result = buffer.readItem();
            int priority = buffer.readVarInt();
            int weight = buffer.readVarInt();
            int cookingTime = buffer.readVarInt();
            int potLevel = buffer.readByte();
            return new CrockPotCookingRecipe(recipeId, requirements, result, priority, weight, cookingTime, potLevel);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CrockPotCookingRecipe recipe) {
            buffer.writeVarInt(recipe.getRequirements().size());
            recipe.getRequirements().forEach(requirement -> requirement.toNetwork(buffer));
            buffer.writeItem(recipe.getResult());
            buffer.writeVarInt(recipe.getPriority());
            buffer.writeVarInt(recipe.getWeight());
            buffer.writeVarInt(recipe.getCookingTime());
            buffer.writeByte(recipe.getPotLevel());
        }
    }
}
