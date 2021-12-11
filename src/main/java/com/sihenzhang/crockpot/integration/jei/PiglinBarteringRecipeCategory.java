package com.sihenzhang.crockpot.integration.jei;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.RangedItem;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.MathUtils;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
    public void setIngredients(PiglinBarteringRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getWeightedResults().unwrap().stream().map(e -> NbtUtils.setLoreString(e.getData().item.getDefaultInstance(), getCountAndChanceString(e, recipe.getWeightedResults().totalWeight))).collect(Collectors.toList()));
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

        cachedInputGuiIngredients.put(recipe, guiItemStacks.getGuiIngredients().get(0));
    }

    @Override
    public void draw(PiglinBarteringRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
        Piglin piglin = EntityType.PIGLIN.create(Minecraft.getInstance().level);
        piglin.setImmuneToZombification(true);
        piglin.setItemSlot(EquipmentSlot.MAINHAND, Items.GOLDEN_SWORD.getDefaultInstance());
        IGuiIngredient<ItemStack> inputGuiIngredient = cachedInputGuiIngredients.getIfPresent(recipe);
        if (inputGuiIngredient != null) {
            ItemStack inputStack = inputGuiIngredient.getDisplayedIngredient();
            if (inputStack != null) {
                piglin.setItemSlot(EquipmentSlot.OFFHAND, inputStack);
                piglin.getBrain().setMemory(MemoryModuleType.ADMIRING_ITEM, true);
            }
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

    private static String getCountAndChanceString(WeightedEntry.Wrapper<RangedItem> weightedRangedItem, int totalWeight) {
        RangedItem rangedItem = weightedRangedItem.getData();
        float chance = (float) weightedRangedItem.getWeight().asInt() / totalWeight;
        StringBuilder chanceTooltip = new StringBuilder();
        if (rangedItem.isRanged()) {
            chanceTooltip.append(rangedItem.min).append("-").append(rangedItem.max);
        } else {
            chanceTooltip.append(rangedItem.min);
        }
        chanceTooltip.append(" (").append(MathUtils.format(chance, "0.00%")).append(")");
        return chanceTooltip.toString();
    }
}
