package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ProgressArrowElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrockPotProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final CrockPotProvider INSTANCE = new CrockPotProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (config.get(ModIntegrationJade.CROCK_POT)) {
            CompoundTag serverData = accessor.getServerData();

            if (serverData.contains("CrockPotItemHandler")) {
                ItemStackHandler itemHandler = new ItemStackHandler();
                itemHandler.deserializeNBT(serverData.getCompound("CrockPotItemHandler"));

                IElementHelper helper = tooltip.getElementHelper();

                boolean needDrawInputs = false;
                for (int i = 0; i < 4; i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty()) {
                        needDrawInputs = true;
                        break;
                    }
                }
                if (needDrawInputs) {
                    // Draw Inputs
                    List<ItemStack> inputStacks = new ArrayList<>();
                    List<IElement> inputs = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        ItemStack stackInSlot = itemHandler.getStackInSlot(i);
                        inputStacks.add(stackInSlot);
                        inputs.add(helper.item(stackInSlot));
                    }
                    tooltip.add(inputs);

                    // Draw Food Values
                    if (serverData.getBoolean("DrawFoodValue")) {
                        FoodValues mergedFoodValues = FoodValues.merge(inputStacks.stream().filter(stack -> !stack.isEmpty()).map(stack -> FoodValuesDefinition.getFoodValues(stack.getItem(), accessor.getLevel().getRecipeManager())).collect(Collectors.toList()));
                        int categoryCount = 0;
                        for (Pair<FoodCategory, Float> entry : mergedFoodValues.entrySet()) {
                            if (categoryCount % 3 == 0) {
                                tooltip.add(helper.item(FoodCategory.getItemStack(entry.getKey())));
                            } else {
                                tooltip.append(helper.spacer(2, 0));
                                tooltip.append(helper.item(FoodCategory.getItemStack(entry.getKey())));
                            }
                            tooltip.append(helper.text(Component.literal("×" + entry.getValue())));
                            categoryCount++;
                        }
                    }
                }

                if (serverData.contains("Result")) {
                    ItemStack result = ItemStack.of(serverData.getCompound("Result"));
                    tooltip.add(helper.text(Component.translatable("integration.crockpot.top.recipe")));
                    tooltip.append(helper.item(result));
                    tooltip.append(helper.text(result.getHoverName()));
                }

                if (serverData.contains("CookingProgress")) {
                    float progress = serverData.getFloat("CookingProgress");
                    tooltip.append(new ProgressArrowElement(progress));
//                    tooltip.add(helper.progress(progress, null, helper.progressStyle(), helper.borderStyle()));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof CrockPotBlockEntity crockPotTileEntity) {
            // Remove Jade Inventory data so that we will render in our way
            tag.remove("jadeHandler");

            tag.put("CrockPotItemHandler", crockPotTileEntity.getItemHandler().serializeNBT());

            tag.putBoolean("DrawFoodValue", accessor.getPlayer().isShiftKeyDown());

            if (crockPotTileEntity.isCooking()) {
                tag.put("Result", crockPotTileEntity.getResult().serializeNBT());

                tag.putFloat("CookingProgress", crockPotTileEntity.getCookingProgress());
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(CrockPot.MOD_ID, "crock_pot");
    }
}
