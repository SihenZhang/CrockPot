package com.sihenzhang.crockpot.integration.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sihenzhang.crockpot.integration.jei.JeiUtils;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.RecipeTypes;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredient;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.DryingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.NumberUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;

import java.util.Comparator;
import java.util.List;

public class DryingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<DryingRecipe>> {
    private static final int WIDTH = 127;
    private static final int HEIGHT = 58;
    private static final int INPUT_X = 19;
    private static final int INPUT_Y = 10;
    private static final int ARROW_X = 51;
    private static final int ARROW_Y = 10;
    private static final int OUTPUT_X = 91;
    private static final int OUTPUT_Y = 10;
    private static final int TIME_Y = 45;

    private final IDrawable arrow;
    private final IDrawableAnimated animatedArrow;
    private final LoadingCache<DryingRecipe, List<ItemStack>> cachedMatchedInputs;

    public DryingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RecipeTypes.DRYING,
                I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying"),
                guiHelper.createDrawableItemLike(ModItems.DRYING_RACK.get()),
                WIDTH,
                HEIGHT
        );
        this.arrow = guiHelper.getRecipeArrow();
        this.animatedArrow = guiHelper.createAnimatedRecipeArrow(200);
        this.cachedMatchedInputs = CacheBuilder.newBuilder().maximumSize(32).build(new CacheLoader<>() {
            @Override
            public List<ItemStack> load(DryingRecipe key) {
                return findMatchedInputs(key);
            }
        });
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<DryingRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        if (recipe.isIngredientRecipe()) {
            recipe.getIngredient()
                    .ifPresent(ingredient -> builder.addInputSlot(INPUT_X, INPUT_Y)
                            .setStandardSlotBackground()
                            .add(ingredient));
        } else {
            builder.addInputSlot(INPUT_X, INPUT_Y)
                    .setStandardSlotBackground()
                    .addItemStacks(this.cachedMatchedInputs.getUnchecked(recipe))
                    .addRichTooltipCallback((_, tooltip) -> this.addRequirementsTooltip(tooltip, recipe));
        }
        builder.addOutputSlot(OUTPUT_X, OUTPUT_Y).setOutputSlotBackground().add(recipe.getResultItem());
    }

    @Override
    public void draw(RecipeHolder<DryingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.drawArrow(guiGraphics, ARROW_X, ARROW_Y);
        this.drawTime(guiGraphics, recipeHolder.value().getDryingTime());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<DryingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        var text = I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.drying_time.second", recipeHolder.value().getDryingTime() / 20);
        var x = (WIDTH - font.width(text)) / 2;
        if (mouseX >= x && mouseX <= x + font.width(text) && mouseY >= TIME_Y && mouseY <= TIME_Y + font.lineHeight) {
            tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.drying_time"));
        }
    }

    private void drawArrow(GuiGraphicsExtractor guiGraphics, int x, int y) {
        this.arrow.draw(guiGraphics, x, y);
        this.animatedArrow.draw(guiGraphics, x, y);
    }

    private void drawTime(GuiGraphicsExtractor guiGraphics, int dryingTime) {
        var font = Minecraft.getInstance().font;
        var text = I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.drying_time.second", dryingTime / 20);
        guiGraphics.text(font, text, (WIDTH - font.width(text)) / 2, TIME_Y, 0xFF808080, false);
    }

    public static List<ItemStack> findMatchedInputs(DryingRecipe recipe) {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return List.of();
        }
        return BuiltInRegistries.ITEM.stream()
                .map(Item::getDefaultInstance)
                .filter(stack -> !stack.isEmpty())
                .filter(stack -> !ItemStack.isSameItemSameComponents(recipe.getResultItem(), stack))
                .filter(stack -> recipe.matches(new SingleRecipeInput(stack), level))
                .sorted(Comparator.comparing(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())))
                .toList();
    }

    private void addRequirementsTooltip(ITooltipBuilder tooltip, DryingRecipe recipe) {
        if (recipe.getRequirements().isEmpty()) {
            return;
        }
        tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirements").withStyle(ChatFormatting.GRAY));
        recipe.getRequirements().stream()
                .map(DryingRecipeCategory::describeRequirement)
                .forEach(component -> tooltip.add(component.copy().withStyle(ChatFormatting.GRAY)));
    }

    private static Component describeRequirement(IRequirement requirement) {
        if (requirement instanceof RequirementCategoryMin categoryMin) {
            return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.category.ge", categoryName(categoryMin.getCategory()), format(categoryMin.getMin()));
        } else if (requirement instanceof RequirementCategoryMinExclusive categoryMinExclusive) {
            return NumberUtil.isClose(categoryMinExclusive.getMin(), 0.0F)
                    ? I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.category.any", categoryName(categoryMinExclusive.getCategory()))
                    : I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.category.gt", categoryName(categoryMinExclusive.getCategory()), format(categoryMinExclusive.getMin()));
        } else if (requirement instanceof RequirementCategoryMax categoryMax) {
            return NumberUtil.isClose(categoryMax.getMax(), 0.0F)
                    ? I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.category.no", categoryName(categoryMax.getCategory()))
                    : I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.category.le", categoryName(categoryMax.getCategory()), format(categoryMax.getMax()));
        } else if (requirement instanceof RequirementCategoryMaxExclusive categoryMaxExclusive) {
            return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.category.lt", categoryName(categoryMaxExclusive.getCategory()), format(categoryMaxExclusive.getMax()));
        } else if (requirement instanceof RequirementMustContainIngredient mustContain) {
            return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.ingredient.ge", mustContain.getQuantity(), ingredientName(mustContain.getIngredient()));
        } else if (requirement instanceof RequirementMustContainIngredientLessThan lessThan) {
            return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.ingredient.le", lessThan.getQuantity(), ingredientName(lessThan.getIngredient()));
        } else if (requirement instanceof RequirementCombinationAnd and) {
            return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.and", describeRequirement(and.getFirst()), describeRequirement(and.getSecond()));
        } else if (requirement instanceof RequirementCombinationOr or) {
            return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.or", describeRequirement(or.getFirst()), describeRequirement(or.getSecond()));
        }
        return Component.empty();
    }

    private static Component categoryName(Holder<FoodCategory> category) {
        return new FoodCategoryIngredient(category).displayName();
    }

    private static Component ingredientName(Ingredient ingredient) {
        var items = JeiUtils.getItemsFromIngredientWithoutEmptyTag(ingredient);
        if (items.isEmpty()) {
            return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.ingredient.unknown");
        }
        if (items.size() == 1) {
            return items.get(0).getHoverName();
        }
        return I18nUtil.integration(ModIntegrationJei.MOD_ID, "drying.requirement.ingredient.many", items.get(0).getHoverName(), items.size() - 1);
    }

    private static String format(float value) {
        return NumberUtil.decimalFormat("0.##", value);
    }
}
