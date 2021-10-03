package com.sihenzhang.crockpot.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JeiUtils {
    public static void reloadJei() {
        if (EffectiveSide.get().isClient()) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                MinecraftForge.EVENT_BUS.post(new RecipesUpdatedEvent(player.connection.getRecipeManager()));
                MinecraftForge.EVENT_BUS.post(new TagsUpdatedEvent.CustomTagTypes(player.connection.getTags()));
            }
        }
    }

    public static List<List<ItemStack>> getPagedIngredientsOutputs(IRecipeLayout recipeLayout, IIngredients ingredients, int size) {
        List<List<ItemStack>> ingredientsOutputs = ingredients.getOutputs(VanillaTypes.ITEM);
        IFocus<ItemStack> focus = recipeLayout.getFocus(VanillaTypes.ITEM);
        if (focus != null && focus.getMode() == IFocus.Mode.OUTPUT) {
            ingredientsOutputs = ingredientsOutputs.stream().filter(list -> ItemStack.isSame(list.get(0), focus.getValue())).collect(Collectors.toList());
        }
        if (ingredientsOutputs.size() > size) {
            List<List<ItemStack>> pagedIngredientsOutputs = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                pagedIngredientsOutputs.add(new ArrayList<>(ingredientsOutputs.get(i)));
            }
            int pages = (int) Math.ceil((double) ingredientsOutputs.size() / size);
            for (int i = 1; i < pages; i++) {
                for (int j = 0; j < size; j++) {
                    pagedIngredientsOutputs.get(j).add(i * size + j < ingredientsOutputs.size() ? ingredientsOutputs.get(i * size + j).get(0) : null);
                }
            }
            ingredientsOutputs = pagedIngredientsOutputs;
        }
        return ingredientsOutputs;
    }
}
