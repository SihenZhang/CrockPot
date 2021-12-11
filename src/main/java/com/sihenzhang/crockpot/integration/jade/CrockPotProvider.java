package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import mcp.mobius.waila.api.ui.IElementHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrockPotProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
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
                            tooltip.append(helper.text(new TextComponent("×" + entry.getValue())));
                            categoryCount++;
                        }
                    }
                }

                if (serverData.contains("Result")) {
                    tooltip.add(helper.text(new TextComponent("Recipe:")));
                    tooltip.append(helper.item(ItemStack.of(serverData.getCompound("Result"))));
                }

                if (serverData.contains("CookingProgress")) {
                    float progress = serverData.getFloat("CookingProgress");
                    tooltip.add(helper.progress(progress, null, helper.progressStyle(), helper.borderStyle()));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, ServerPlayer player, Level level, BlockEntity blockEntity, boolean showDetails) {
        if (blockEntity instanceof CrockPotBlockEntity crockPotTileEntity) {
            // Remove Jade Inventory data so that we will render in our way
            tag.remove("jadeHandler");

            tag.put("CrockPotItemHandler", crockPotTileEntity.getItemHandler().serializeNBT());

            tag.putBoolean("DrawFoodValue", player.isShiftKeyDown());

            if (crockPotTileEntity.isCooking()) {
                tag.put("Result", crockPotTileEntity.getResult().serializeNBT());

                tag.putFloat("CookingProgress", crockPotTileEntity.getCookingProgress());
            }
        }
    }
}
