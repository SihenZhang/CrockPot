package com.sihenzhang.crockpot.integration;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValueSum;
import com.sihenzhang.crockpot.recipe.Recipe;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;

public class ModIntegrationTheOneProbe implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(this);
        return null;
    }

    @Override
    public String getID() {
        return CrockPot.MOD_ID + ":crock_pot";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof CrockPotTileEntity) {
            CrockPotTileEntity crockPotTileEntity = (CrockPotTileEntity) tileEntity;
            boolean needDrawInputs = false;
            ItemStackHandler itemHandler = crockPotTileEntity.getItemHandler();
            for (int i = 0; i < 4; i++) {
                if (!itemHandler.getStackInSlot(i).isEmpty()) {
                    needDrawInputs = true;
                    break;
                }
            }
            if (needDrawInputs) {
                // Draw Inputs
                IProbeInfo inputs = probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(0xff999999).spacing(0));
                for (int i = 0; i < 4; i++) {
                    inputs.item(itemHandler.getStackInSlot(i));
                }
                // Draw Ingredients
                if (player.isSneaking()) {
                    IProbeInfo ingredients = probeInfo.vertical(probeInfo.defaultLayoutStyle().spacing(0));
                    List<EnumMap<FoodCategory, Float>> ingredientList = new ArrayList<>(4);
                    for (int i = 0; i < 4; i++) {
                        ItemStack stack = itemHandler.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            ingredientList.add(CrockPot.INGREDIENT_MANAGER.valuesOf(stack.getItem()));
                        }
                    }
                    FoodValueSum foodValueSum = new FoodValueSum(ingredientList);
                    IProbeInfo ingredientHorizontal = null;
                    int ingredientCount = 0;
                    for (FoodCategory type : FoodCategory.values()) {
                        float ingredientValue = foodValueSum.getIngredient(type);
                        if (ingredientValue != 0) {
                            ITextComponent suffix = new StringTextComponent("x" + ingredientValue);
                            if (ingredientCount % 2 == 0) {
                                ingredientHorizontal = ingredients.horizontal(probeInfo.defaultLayoutStyle().spacing(4));
                            }
                            ingredientHorizontal.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                                    .item(FoodCategory.getItemStack(type))
                                    .text(suffix);
                            ingredientCount++;
                        }
                    }
                }
            }
            if (crockPotTileEntity.isProcessing()) {
                // Draw Output
                Recipe currentRecipe = crockPotTileEntity.getCurrentRecipe();
                if (!currentRecipe.isEmpty()) {
                    ITextComponent prefix = new TranslationTextComponent("integration.crockpot.top.recipe");
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .text(prefix)
                            .item(currentRecipe.getResult())
                            .text(currentRecipe.getResult().getDisplayName());
                }
                // Draw Progress
                float progress = crockPotTileEntity.getProcessTimeProgress();
                if (progress > 1E-6F) {
                    probeInfo.progress((int) (progress * 100), 100, probeInfo.defaultProgressStyle().suffix("%"));
                }
            }
        }
    }
}
