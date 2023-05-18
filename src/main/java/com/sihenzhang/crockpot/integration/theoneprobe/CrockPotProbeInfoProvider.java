package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.RLUtils;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CrockPotProbeInfoProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(this);
        return null;
    }

    @Override
    public ResourceLocation getID() {
        return RLUtils.createRL("crock_pot");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData data) {
        BlockEntity blockEntity = level.getBlockEntity(data.getPos());
        if (blockEntity instanceof CrockPotBlockEntity crockPotTileEntity) {
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
                ItemStack[] inputStacks = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    inputStacks[i] = itemHandler.getStackInSlot(i);
                }
                IProbeInfo inputs = probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(0xff999999).spacing(0));
                Arrays.stream(inputStacks).forEach(inputs::item);
                // Draw Food Values
                if (player.isShiftKeyDown()) {
                    IProbeInfo foodValues = probeInfo.vertical(probeInfo.defaultLayoutStyle().spacing(0));
                    FoodValues mergedFoodValues = FoodValues.merge(Arrays.stream(inputStacks).filter(stack -> !stack.isEmpty())
                            .map(stack -> FoodValuesDefinition.getFoodValues(stack.getItem(), level.getRecipeManager())).collect(Collectors.toList()));
                    IProbeInfo foodValuesHorizontal = null;
                    int categoryCount = 0;
                    for (Pair<FoodCategory, Float> entry : mergedFoodValues.entrySet()) {
                        Component suffix = Component.literal("×" + entry.getValue());
                        if (categoryCount % 2 == 0) {
                            foodValuesHorizontal = foodValues.horizontal(probeInfo.defaultLayoutStyle().spacing(4));
                        }
                        foodValuesHorizontal.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                                .item(FoodCategory.getItemStack(entry.getKey()))
                                .text(suffix);
                        categoryCount++;
                    }
                }
            }
            if (crockPotTileEntity.isCooking()) {
                // Draw Output
                ItemStack result = crockPotTileEntity.getResult();
                if (!result.isEmpty()) {
                    Component prefix = Component.translatable("integration.crockpot.top.recipe");
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .text(prefix)
                            .item(result)
                            .itemLabel(result);
                }
                // Draw Progress
                float progress = crockPotTileEntity.getCookingProgress();
                if (progress > 1E-6F) {
                    probeInfo.progress((int) (progress * 100), 100, probeInfo.defaultProgressStyle().suffix("%"));
                }
            }
        }
    }
}
