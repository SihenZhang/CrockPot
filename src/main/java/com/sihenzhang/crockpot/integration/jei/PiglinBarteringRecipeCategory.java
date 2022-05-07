package com.sihenzhang.crockpot.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.NbtUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import com.sihenzhang.crockpot.util.StringUtils;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;

public class PiglinBarteringRecipeCategory implements IRecipeCategory<PiglinBarteringRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public static final RecipeType<PiglinBarteringRecipe> PIGLIN_BARTERING_RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "piglin_bartering", PiglinBarteringRecipe.class);

    public PiglinBarteringRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/piglin_bartering.png"), 0, 0, 176, 112);
        this.icon = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/icons.png"), 32, 0, 16, 16);
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends PiglinBarteringRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<PiglinBarteringRecipe> getRecipeType() {
        return PIGLIN_BARTERING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("integration.crockpot.jei.piglin_bartering");
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
    public void setRecipe(IRecipeLayoutBuilder builder, PiglinBarteringRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 3).setSlotName("inputSlot").addIngredients(recipe.getIngredient());
        List<ItemStack> weightedOutput = recipe.getWeightedResults().unwrap().stream().map(e -> NbtUtils.setLoreString(e.getData().item.getDefaultInstance(), StringUtils.formatCountAndChance(e, recipe.getWeightedResults().totalWeight))).toList();
        List<List<ItemStack>> pagedItemStacks = JeiUtils.getPagedItemStacks(weightedOutput, focuses, RecipeIngredientRole.OUTPUT, 30);
        for (int i = 0; i < pagedItemStacks.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 84 + i % 5 * 18, 2 + i / 5 * 18).addItemStacks(pagedItemStacks.get(i));
        }
    }

    @Override
    public void draw(PiglinBarteringRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Piglin piglin = EntityType.PIGLIN.create(Minecraft.getInstance().level);
        piglin.setImmuneToZombification(true);
        piglin.setItemSlot(EquipmentSlot.MAINHAND, Items.GOLDEN_SWORD.getDefaultInstance());
        Optional<ItemStack> inputStack = recipeSlotsView.findSlotByName("inputSlot").flatMap(slot -> slot.getDisplayedIngredient(VanillaTypes.ITEM_STACK));
        if (inputStack.isPresent()) {
            piglin.setItemSlot(EquipmentSlot.OFFHAND, inputStack.get());
            piglin.getBrain().setMemory(MemoryModuleType.ADMIRING_ITEM, true);
        }
        boolean emptyInOffhand = piglin.getOffhandItem().isEmpty();
        // if Piglin is not holding item in offhand, it will look at the mouse
        if (emptyInOffhand) {
            double yaw = 30.0 - mouseX;
            double pitch = 45.0 - mouseY;
            piglin.yBodyRot = (float) Math.atan(yaw / 40.0F) * 20.0F;
            piglin.setYRot((float) Math.atan(yaw / 40.0F) * 40.0F);
            piglin.setXRot(-((float) Math.atan(pitch / 40.0F)) * 20.0F);
            piglin.yHeadRot = piglin.getYRot();
            piglin.yHeadRotO = piglin.getYRot();
        }
        stack.pushPose();
        stack.translate(emptyInOffhand ? 29.0 : 37.0, 103.0, 50.0);
        stack.scale(-32.0F, 32.0F, 32.0F);
        stack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        if (!emptyInOffhand) {
            stack.mulPose(Vector3f.YN.rotationDegrees(45.0F));
        }
        EntityRenderDispatcher entityRendererDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        MultiBufferSource.BufferSource multiBufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        entityRendererDispatcher.setRenderShadow(false);
        entityRendererDispatcher.render(piglin, 0.0, 0.0, 0.0, 0.0F, 1.0F, stack, multiBufferSource, 0xF000F0);
        entityRendererDispatcher.setRenderShadow(true);
        multiBufferSource.endBatch();
        stack.popPose();
    }
}
