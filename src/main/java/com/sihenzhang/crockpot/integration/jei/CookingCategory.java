package com.sihenzhang.crockpot.integration.jei;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.integration.jei.gui.RequirementDrawer;
import com.sihenzhang.crockpot.integration.jei.gui.RequirementDrawer.DrawInfo;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipe;
import com.sihenzhang.crockpot.recipe.pot.requirement.IRequirement;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CookingCategory implements IRecipeCategory<CrockPotRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "cooking");
    private final IDrawable background;
    private final IDrawable icon;

    public CookingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(190,150);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(CrockPotRegistry.crockPotBasicBlock));
    }

    @Override
    public ResourceLocation getUid() {
        return CookingCategory.UID;
    }

    @Override
    public Class<? extends CrockPotRecipe> getRecipeClass() {
        return CrockPotRecipe.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().toString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("integration.crockpot.jei.cooking");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
    public static ItemStack[] pots=new ItemStack[] {
    		new ItemStack(CrockPotRegistry.crockPotBasicBlock),
    		new ItemStack(CrockPotRegistry.crockPotAdvancedBlock),
    		new ItemStack(CrockPotRegistry.crockPotUltimateBlock)
    };
    @Override
    public void setIngredients(CrockPotRecipe recipe, IIngredients ingredients) {
    	 ingredients.setInput(VanillaTypes.ITEM,pots[recipe.getPotLevel()]);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
       
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout,CrockPotRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
       
        guiItemStacks.init(0, true,170, 18);
        guiItemStacks.init(1, false,170, 0);
        guiItemStacks.set(ingredients);
		int yoff=2;
		int slot=0;
		
		for(IRequirement i:recipe.getRequirements()) {
			Pair<IDrawable, List<DrawInfo>> ip=RequirementDrawer.drawItems(i, 2, yoff);
			yoff+=ip.getFirst().getHeight();
			for(DrawInfo di:ip.getSecond()) {
				guiItemStacks.init(++slot,true,di.x,di.y);
				guiItemStacks.set(slot,di.is);
			}
		}
        
    }

	@Override
	public void draw(CrockPotRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, matrixStack, mouseX, mouseY);
		Minecraft.getInstance().font.draw(matrixStack,"p:"+recipe.getPriority(),160,36,0);
		Minecraft.getInstance().font.draw(matrixStack,"w:"+recipe.getWeight(),160,48,0);
		int yoff=2;
		for(IRequirement i:recipe.getRequirements()) {
			IDrawable id=RequirementDrawer.drawRequirement(i, matrixStack,2,yoff);
			yoff+=id.getHeight();
		}
	}


}
