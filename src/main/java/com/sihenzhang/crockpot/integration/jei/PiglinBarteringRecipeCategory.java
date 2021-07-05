package com.sihenzhang.crockpot.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.stream.Collectors;

public class PiglinBarteringRecipeCategory implements IRecipeCategory<PiglinBarteringRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "piglin_bartering");
    private static final DecimalFormat CHANCE_FORMAT = new DecimalFormat("0.00%");
    private final IDrawable background;
    private final IDrawable icon;

    public PiglinBarteringRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/piglin_bartering_jei.png"), 0, 0, 176, 114);
        this.icon = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei_icons.png"), 0, 0, 16, 16);
    }

    @Override
    public ResourceLocation getUid() {
        return PiglinBarteringRecipeCategory.UID;
    }

    @Override
    public Class<? extends PiglinBarteringRecipe> getRecipeClass() {
        return PiglinBarteringRecipe.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("integration.crockpot.jei.piglin_bartering");
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
    public void setIngredients(PiglinBarteringRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getInput()));
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getWeightedOutputs().stream().map(e -> e.item.getDefaultInstance()).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PiglinBarteringRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        int slot = 0;
        guiItemStacks.init(slot++, true, 35, 2);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                guiItemStacks.init(slot++, false, 94 + j * 20, 12 + i * 20);
            }
        }
        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (!input) {
                WeightedItem weightedItem = recipe.getWeightedOutputs().get(slotIndex - 1);
                float chance = (float) weightedItem.weight / WeightedRandom.getTotalWeight(recipe.getWeightedOutputs());
                IFormattableTextComponent tooltipTextComponent;
                if (weightedItem.isRanged()) {
                    tooltipTextComponent = new StringTextComponent(weightedItem.min + "-" + weightedItem.max);
                } else {
                    tooltipTextComponent = new StringTextComponent(Integer.toString(weightedItem.min));
                }
                tooltip.add(tooltipTextComponent.append(" (" + CHANCE_FORMAT.format(chance) + ")"));
            }
        });
        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(PiglinBarteringRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        PiglinEntity piglinEntity = EntityType.PIGLIN.create(Minecraft.getInstance().level);
        double yaw = 33 - mouseX;
        double pitch = 45 - mouseY;
        piglinEntity.yBodyRot = (float) Math.atan(yaw / 40.0F) * 20.0F;
        piglinEntity.yRot = (float) Math.atan(yaw / 40.0F) * 40.0F;
        piglinEntity.xRot = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
        piglinEntity.yHeadRot = piglinEntity.yRot;
        piglinEntity.yHeadRotO = piglinEntity.yRot;
        matrixStack.pushPose();
        matrixStack.translate(32.0, 103.0, 50.0);
        matrixStack.scale(-32.0F, 32.0F, 32.0F);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        EntityRendererManager entityRendererManager = Minecraft.getInstance().getEntityRenderDispatcher();
        IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        entityRendererManager.setRenderShadow(false);
        entityRendererManager.render(piglinEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack, renderTypeBuffer, 0xF000F0);
        entityRendererManager.setRenderShadow(true);
        renderTypeBuffer.endBatch();
        matrixStack.popPose();
    }
}
