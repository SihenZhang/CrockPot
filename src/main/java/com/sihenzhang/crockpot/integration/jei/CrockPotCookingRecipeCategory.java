package com.sihenzhang.crockpot.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.AbstractCrockPotBlock;
import com.sihenzhang.crockpot.integration.jei.gui.requirement.AbstractDrawableRequirement;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

public class CrockPotCookingRecipeCategory implements IRecipeCategory<CrockPotCookingRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable priority;
    private final IDrawable time;
    private final LoadingCache<CrockPotCookingRecipe, List<AbstractDrawableRequirement<? extends IRequirement>>> cachedDrawables;

    public static final RecipeType<CrockPotCookingRecipe> CROCK_POT_COOKING_RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "crock_pot_cooking", CrockPotCookingRecipe.class);

    public CrockPotCookingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/crock_pot_cooking.png"), 0, 0, 176, 133);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get().getDefaultInstance());
        this.priority = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/crock_pot_cooking.png"), 176, 0, 16, 16);
        this.time = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/crock_pot_cooking.png"), 176, 16, 16, 16);
        this.cachedDrawables = CacheBuilder.newBuilder().maximumSize(32).build(new CacheLoader<>() {
            @Override
            public List<AbstractDrawableRequirement<? extends IRequirement>> load(CrockPotCookingRecipe key) {
                return AbstractDrawableRequirement.getDrawables(key.getRequirements());
            }
        });
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends CrockPotCookingRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<CrockPotCookingRecipe> getRecipeType() {
        return CROCK_POT_COOKING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("integration.crockpot.jei.crock_pot_cooking");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrockPotCookingRecipe recipe, IFocusGroup focuses) {
        int xOffset = 2;
        int yOffset = 2;
        int maxWidth = 0;
        var drawables = cachedDrawables.getUnchecked(recipe);
        for (var drawable : drawables) {
            if (!drawable.getInvisibleInputs().isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(drawable.getInvisibleInputs());
            }
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            var guiItemStacksInfos = drawable.getGuiItemStacksInfos(xOffset, yOffset);
            guiItemStacksInfos.forEach(guiItemStacksInfo -> builder.addSlot(guiItemStacksInfo.role, guiItemStacksInfo.x, guiItemStacksInfo.y).addItemStacks(guiItemStacksInfo.stacks));
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
        List<ItemStack> pots = ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.CROCK_POTS).stream()
                .filter(block -> block instanceof AbstractCrockPotBlock).map(AbstractCrockPotBlock.class::cast)
                .filter(pot -> pot.getPotLevel() >= recipe.getPotLevel()).map(block -> block.asItem().getDefaultInstance()).toList();
        builder.addSlot(RecipeIngredientRole.CATALYST, 62, 104).addItemStacks(pots);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 110).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(CrockPotCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        int cookingTime = recipe.getCookingTime();
        if (cookingTime > 0) {
            int cookingTimeSeconds = cookingTime / 20;
            time.draw(stack, 0, 117);
            font.draw(stack, new TranslatableComponent("integration.crockpot.jei.crock_pot_cooking.cooking_time.second", cookingTimeSeconds), 17, 121, 0xFF808080);
        }
        String priorityString = String.valueOf(recipe.getPriority());
        int priorityWidth = font.width(priorityString);
        priority.draw(stack, 159 - priorityWidth, 117);
        font.draw(stack, priorityString, 175 - priorityWidth, 121, 0xFF808080);

        int xOffset = 2;
        int yOffset = 2;
        int maxWidth = 0;
        var drawables = cachedDrawables.getUnchecked(recipe);
        for (var drawable : drawables) {
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            drawable.draw(stack, xOffset, yOffset);
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
    }

    @Override
    public List<Component> getTooltipStrings(CrockPotCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 0.0 && mouseX <= 16.0 && mouseY >= 117.0 && mouseY <= 133.0) {
            return Collections.singletonList(new TranslatableComponent("integration.crockpot.jei.crock_pot_cooking.cooking_time"));
        }
        String priorityString = String.valueOf(recipe.getPriority());
        int priorityWidth = Minecraft.getInstance().font.width(priorityString);
        if (mouseX >= 159.0 - priorityWidth && mouseX <= 175.0 - priorityWidth && mouseY >= 117.0 && mouseY <= 133.0) {
            return Collections.singletonList(new TranslatableComponent("integration.crockpot.jei.crock_pot_cooking.priority"));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
