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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModIntegrationTheOneProbe implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
    public static final String MOD_ID = "theoneprobe";
    public static final String METHOD_NAME = "getTheOneProbe";

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
                ItemStack[] inputStacks = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    inputStacks[i] = itemHandler.getStackInSlot(i);
                }
                IProbeInfo inputs = probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(0xff999999).spacing(0));
                Arrays.stream(inputStacks).forEach(inputs::item);
                // Draw Food Values
                if (player.isSneaking()) {
                    IProbeInfo foodValues = probeInfo.vertical(probeInfo.defaultLayoutStyle().spacing(0));
                    List<EnumMap<FoodCategory, Float>> foodValueList = new ArrayList<>(4);
                    Arrays.stream(inputStacks).filter(stack -> !stack.isEmpty())
                            .forEach(stack -> foodValueList.add(CrockPot.FOOD_CATEGORY_MANAGER.valuesOf(stack.getItem())));
                    FoodValueSum foodValueSum = new FoodValueSum(foodValueList);
                    IProbeInfo foodValuesHorizontal = null;
                    int categoryCount = 0;
                    for (FoodCategory category : EnumUtils.getEnumList(FoodCategory.class)) {
                        float foodValue = foodValueSum.getFoodValue(category);
                        if (foodValue != 0) {
                            ITextComponent suffix = new StringTextComponent("x" + foodValue);
                            if (categoryCount % 2 == 0) {
                                foodValuesHorizontal = foodValues.horizontal(probeInfo.defaultLayoutStyle().spacing(4));
                            }
                            foodValuesHorizontal.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                                    .item(FoodCategory.getItemStack(category))
                                    .text(suffix);
                            categoryCount++;
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

    @SubscribeEvent
    public static void sendIMCMessage(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(ModIntegrationTheOneProbe.MOD_ID)) {
            InterModComms.sendTo(ModIntegrationTheOneProbe.MOD_ID, ModIntegrationTheOneProbe.METHOD_NAME, ModIntegrationTheOneProbe::new);
        }
    }
}
