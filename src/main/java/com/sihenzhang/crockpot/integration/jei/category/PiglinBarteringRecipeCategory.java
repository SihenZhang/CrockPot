package com.sihenzhang.crockpot.integration.jei.category;

import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.RecipeTypes;
import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.IdUtil;
import com.sihenzhang.crockpot.util.NumberUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.joml.Vector2f;

public class PiglinBarteringRecipeCategory extends AbstractRecipeCategory<RecipeHolder<PiglinBarteringRecipe>> {
    private static final String INPUT_SLOT = "inputSlot";
    private static final int PIGLIN_SIZE = 32;
    private static final float PIGLIN_OFFSET_Y = 0.0F;
    private static final float PIGLIN_LOOK_RANGE = 40.0F;
    private static final float EMPTY_PIGLIN_LOOK_X = 30.0F;
    private static final float EMPTY_PIGLIN_LOOK_Y = 45.0F;
    private static final EntityBounds EMPTY_PIGLIN_BOUNDS = new EntityBounds(0, 36, 50, 108);
    private static final EntityBounds ADMIRING_PIGLIN_BOUNDS = new EntityBounds(5, 36, 61, 108);

    private final IDrawable background;

    public PiglinBarteringRecipeCategory(IGuiHelper guiHelper) {
        super(
                RecipeTypes.PIGLIN_BARTERING,
                I18nUtil.integration(ModIntegrationJei.MOD_ID, "piglin_bartering"),
                guiHelper.createDrawable(ModIntegrationJei.ICONS, 32, 0, 16, 16),
                177,
                108
        );
        this.background = guiHelper.createDrawable(IdUtil.mod("textures/gui/jei/piglin_bartering.png"), 0, 0, 177, 108);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PiglinBarteringRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        builder.addInputSlot(25, 1).setStandardSlotBackground().setSlotName(INPUT_SLOT).add(recipe.getIngredient());
        var entries = recipe.getWeightedResults().unwrap();
        var totalWeight = entries.stream().mapToInt(entry -> entry.weight()).sum();
        entries.forEach(entry -> {
            var result = entry.value();
            var chance = totalWeight == 0 ? 0.0D : (double) entry.weight() / totalWeight * 100.0D;
            builder.addOutputSlot()
                    .setStandardSlotBackground()
                    .add(result.item.getDefaultInstance())
                    .addRichTooltipCallback((_, tooltip) -> {
                        tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "output_chance", NumberUtil.decimalFormat("0.00", chance)).withStyle(ChatFormatting.GRAY));
                        if (result.isRanged()) {
                            tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "min_output", result.min).withStyle(ChatFormatting.GRAY));
                            tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "max_output", result.max).withStyle(ChatFormatting.GRAY));
                        } else {
                            tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "exact_output", result.min).withStyle(ChatFormatting.GRAY));
                        }
                    });
        });
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<PiglinBarteringRecipe> recipeHolder, IFocusGroup focuses) {
        var outputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.OUTPUT);
        var scrollGrid = builder.addScrollGridWidget(outputSlots, 5, 6);
        scrollGrid.setPosition(70, 0);
    }

    @Override
    public void draw(RecipeHolder<PiglinBarteringRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        var piglin = EntityType.PIGLIN.create(level, EntitySpawnReason.TRIGGERED);
        if (piglin == null) {
            return;
        }
        piglin.setImmuneToZombification(true);
        piglin.setItemSlot(EquipmentSlot.MAINHAND, Items.GOLDEN_SWORD.getDefaultInstance());
        recipeSlotsView.findSlotByName(INPUT_SLOT)
                .flatMap(slot -> slot.getDisplayedIngredient(VanillaTypes.ITEM_STACK))
                .ifPresent(inputStack -> {
                    piglin.setItemSlot(EquipmentSlot.OFFHAND, inputStack);
                    piglin.getBrain().setMemory(MemoryModuleType.ADMIRING_ITEM, true);
        });
        var emptyInOffhand = piglin.getOffhandItem().isEmpty();
        if (emptyInOffhand) {
            var bounds = transformEntityBounds(guiGraphics, EMPTY_PIGLIN_BOUNDS);
            var xAngle = angleFrom(EMPTY_PIGLIN_LOOK_X, mouseX);
            var yAngle = angleFrom(EMPTY_PIGLIN_LOOK_Y, mouseY);
            InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, bounds.x0(), bounds.y0(), bounds.x1(), bounds.y1(), PIGLIN_SIZE, PIGLIN_OFFSET_Y, xAngle, yAngle, piglin);
        } else {
            var bounds = transformEntityBounds(guiGraphics, ADMIRING_PIGLIN_BOUNDS);
            InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, bounds.x0(), bounds.y0(), bounds.x1(), bounds.y1(), PIGLIN_SIZE, PIGLIN_OFFSET_Y, -0.7F, 0.0F, piglin);
        }
    }

    private static float angleFrom(float lookPosition, double mousePosition) {
        return (float) Math.atan((lookPosition - mousePosition) / PIGLIN_LOOK_RANGE);
    }

    private static EntityBounds transformEntityBounds(GuiGraphicsExtractor guiGraphics, EntityBounds bounds) {
        var topLeft = transformPosition(guiGraphics, bounds.x0(), bounds.y0());
        var bottomRight = transformPosition(guiGraphics, bounds.x1(), bounds.y1());
        return new EntityBounds(Math.round(topLeft.x), Math.round(topLeft.y), Math.round(bottomRight.x), Math.round(bottomRight.y));
    }

    private static Vector2f transformPosition(GuiGraphicsExtractor guiGraphics, float x, float y) {
        return guiGraphics.pose().transformPosition(x, y, new Vector2f());
    }

    private record EntityBounds(int x0, int y0, int x1, int y1) {
    }
}
