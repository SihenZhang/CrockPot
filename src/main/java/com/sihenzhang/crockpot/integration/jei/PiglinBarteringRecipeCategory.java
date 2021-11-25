package com.sihenzhang.crockpot.integration.jei;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.NbtUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class PiglinBarteringRecipeCategory implements IRecipeCategory<PiglinBarteringRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "piglin_bartering");
    private final IDrawable background;
    private final IDrawable icon;
    private final Cache<PiglinBarteringRecipe, IGuiIngredient<ItemStack>> cachedInputGuiIngredients;

    public PiglinBarteringRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/piglin_bartering.png"), 0, 0, 176, 112);
        this.icon = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/icons.png"), 32, 0, 16, 16);
        this.cachedInputGuiIngredients = CacheBuilder.newBuilder().maximumSize(16).build();
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
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getWeightedResults().stream().map(e -> NbtUtils.setLoreString(e.item.getDefaultInstance(), WeightedItem.getCountAndChance(e, recipe.getWeightedResults()))).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PiglinBarteringRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        int slot = 0;
        guiItemStacks.init(slot++, true, 31, 2);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                guiItemStacks.init(slot++, false, 84 + col * 18, 2 + row * 18);
            }
        }
        List<List<ItemStack>> pagedIngredientsOutputs = JeiUtils.getPagedIngredients(recipeLayout, ingredients, 30, false);
        guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        for (int i = 0; i < pagedIngredientsOutputs.size(); i++) {
            guiItemStacks.set(i + 1, pagedIngredientsOutputs.get(i));
        }

        this.cachedInputGuiIngredients.put(recipe, guiItemStacks.getGuiIngredients().get(0));
    }

    @Override
    public void draw(PiglinBarteringRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        PiglinEntity piglinEntity = EntityType.PIGLIN.create(Minecraft.getInstance().level);
        piglinEntity.setImmuneToZombification(true);
        piglinEntity.setItemSlot(EquipmentSlotType.MAINHAND, Items.GOLDEN_SWORD.getDefaultInstance());
        IGuiIngredient<ItemStack> inputGuiIngredient = this.cachedInputGuiIngredients.getIfPresent(recipe);
        if (inputGuiIngredient != null) {
            ItemStack inputStack = inputGuiIngredient.getDisplayedIngredient();
            if (inputStack != null) {
                piglinEntity.setItemSlot(EquipmentSlotType.OFFHAND, inputStack);
                piglinEntity.getBrain().setMemory(MemoryModuleType.ADMIRING_ITEM, true);
            }
        }
        boolean emptyInOffhand = piglinEntity.getOffhandItem().isEmpty();
        // if Piglin is not holding item in offhand, it will look at the mouse
        if (emptyInOffhand) {
            double yaw = 30.0 - mouseX;
            double pitch = 45.0 - mouseY;
            piglinEntity.yBodyRot = (float) Math.atan(yaw / 40.0F) * 20.0F;
            piglinEntity.yRot = (float) Math.atan(yaw / 40.0F) * 40.0F;
            piglinEntity.xRot = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
            piglinEntity.yHeadRot = piglinEntity.yRot;
            piglinEntity.yHeadRotO = piglinEntity.yRot;
        }
        matrixStack.pushPose();
        matrixStack.translate(emptyInOffhand ? 29.0 : 37.0, 103.0, 50.0);
        matrixStack.scale(-32.0F, 32.0F, 32.0F);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        if (!emptyInOffhand) {
            matrixStack.mulPose(Vector3f.YN.rotationDegrees(45.0F));
        }
        EntityRendererManager entityRendererManager = Minecraft.getInstance().getEntityRenderDispatcher();
        IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        entityRendererManager.setRenderShadow(false);
        entityRendererManager.render(piglinEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack, renderTypeBuffer, 0xF000F0);
        entityRendererManager.setRenderShadow(true);
        renderTypeBuffer.endBatch();
        matrixStack.popPose();
    }
}
