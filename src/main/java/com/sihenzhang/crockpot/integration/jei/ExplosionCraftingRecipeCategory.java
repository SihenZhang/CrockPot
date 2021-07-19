package com.sihenzhang.crockpot.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.explosion.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.utils.MathUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.LargeExplosionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.util.Collections;

public class ExplosionCraftingRecipeCategory implements IRecipeCategory<ExplosionCraftingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "explosion_crafting");
    private static final DecimalFormat CHANCE_FORMAT = new DecimalFormat("0.##%");
    private final IDrawable background;
    private final IDrawable icon;

    public ExplosionCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/explosion_crafting.png"), 0, 0, 97, 36);
        this.icon = guiHelper.createDrawableIngredient(Items.TNT.getDefaultInstance());
    }

    @Override
    public ResourceLocation getUid() {
        return ExplosionCraftingRecipeCategory.UID;
    }

    @Override
    public Class<? extends ExplosionCraftingRecipe> getRecipeClass() {
        return ExplosionCraftingRecipe.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("integration.crockpot.jei.explosion_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(ExplosionCraftingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getInput()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput().getDefaultInstance());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExplosionCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 8, 9);
        guiItemStacks.init(1, false, 67, 9);
        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(ExplosionCraftingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
//        BufferBuilder builder = Tessellator.getInstance().getBuilder();
//        builder.begin(7, DefaultVertexFormats.PARTICLE);
//        Particle particle = minecraft.particleEngine.createParticle(ParticleTypes.EXPLOSION, 0, 0, 0, 0, 0, 0);
//        particle.render(builder, minecraft.gameRenderer.getMainCamera(), minecraft.getFrameTime());
        float lossRate = recipe.getLossRate();
        if (!MathUtils.fuzzyEquals(lossRate, 1.0F)) {
            FontRenderer fontRenderer = minecraft.font;
            String rate = CHANCE_FORMAT.format(1.0F - lossRate);
            int width = fontRenderer.width(rate);
            fontRenderer.draw(matrixStack, rate, 79 - width / 2.0F, 40, 0xFF808080);
        }
        if (recipe.isOnlyBlock()) {
            FontRenderer fontRenderer = minecraft.font;
            String string = "Only Block";
            int width = fontRenderer.width(string);
            fontRenderer.draw(matrixStack, string, 16 - width / 2.0F, 40, 0xFFFF5555);
        }
    }
}
