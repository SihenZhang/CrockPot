package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.registry.FoodCategory;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

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
                itemHandler.deserializeNBT(accessor.getLevel().registryAccess(), serverData.getCompound("CrockPotItemHandler"));

                IElementHelper helper = IElementHelper.get();

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
                        FoodValues mergedFoodValues = FoodValues.merge(inputStacks.stream().filter(stack -> !stack.isEmpty()).map(stack -> FoodValuesDefinition.getFoodValues(stack, accessor.getLevel())).collect(Collectors.toList()));
                        int categoryCount = 0;
                        for (Object2FloatMap.Entry<Holder<FoodCategory>> entry : mergedFoodValues.entrySet()) {
                            if (categoryCount % 3 == 0) {
                                tooltip.add(new FoodCategoryElement(entry.getKey()));
                            } else {
                                tooltip.append(helper.spacer(2, 0));
                                tooltip.append(new FoodCategoryElement(entry.getKey()));
                            }
                            var id = entry.getKey().unwrapKey().orElseThrow().location();
                            var name = Component.translatable(id.toLanguageKey("food_category"));
                            tooltip.append(helper.text(name.append(" ×" + entry.getFloatValue()).withStyle(Style.EMPTY.withColor(entry.getKey().value().color()))));
                            categoryCount++;
                        }
                    }
                }

                if (serverData.contains("Result")) {
                    ItemStack result = ItemStack.parseOptional(accessor.getLevel().registryAccess(), serverData.getCompound("Result"));
                    tooltip.add(helper.text(Component.translatable("integration.crockpot.top.recipe")));
                    tooltip.append(helper.item(result));
                    tooltip.append(helper.text(result.getHoverName()));
                }

                if (serverData.contains("CookingProgress")) {
                    float progress = serverData.getFloat("CookingProgress");
                    tooltip.add(helper.progress(progress, null, helper.progressStyle(), BoxStyle.getNestedBox(), false));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof CrockPotBlockEntity crockPotTileEntity) {
            tag.put("CrockPotItemHandler", crockPotTileEntity.getItemHandler().serializeNBT(accessor.getLevel().registryAccess()));

            tag.putBoolean("DrawFoodValue", accessor.getPlayer().isShiftKeyDown());

            if (crockPotTileEntity.isCooking()) {
                tag.put("Result", crockPotTileEntity.getResult().save(accessor.getLevel().registryAccess()));

                tag.putFloat("CookingProgress", crockPotTileEntity.getCookingProgress());
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(CrockPot.MOD_ID, "crock_pot");
    }
}
