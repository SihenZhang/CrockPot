package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import snownee.jade.Renderables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrockPotProvider implements IComponentProvider, IServerDataProvider<TileEntity> {
    public static final CrockPotProvider INSTANCE = new CrockPotProvider();

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (config.get(ModIntegrationJade.CROCK_POT)) {
            CompoundNBT serverData = accessor.getServerData();

            if (serverData.contains("CrockPotItemHandler")) {
                ItemStackHandler itemHandler = new ItemStackHandler();
                itemHandler.deserializeNBT(serverData.getCompound("CrockPotItemHandler"));

                List<ItemStack> inputStacks = new ArrayList<>();
                List<ITextComponent> inputs = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    ItemStack stackInSlot = itemHandler.getStackInSlot(i);
                    inputStacks.add(stackInSlot);
                    inputs.add(Renderables.item(stackInSlot));
                }

                int progress = serverData.getInt("CookingProgress");
                CompoundNBT progressTag = new CompoundNBT();
                progressTag.putInt("progress", progress);
                progressTag.putInt("total", progress > 0 ? 100 : 0);

                ITextComponent result = Renderables.item(serverData.contains("Result") ? ItemStack.of(serverData.getCompound("Result")) : itemHandler.getStackInSlot(5));

                tooltip.add(Renderables.of(Renderables.of(inputs.toArray(new ITextComponent[0])), new RenderableTextComponent(new ResourceLocation("furnace_progress"), progressTag), result));

                if (serverData.getBoolean("DrawFoodValue")) {
                    FoodValues mergedFoodValues = FoodValues.merge(inputStacks.stream().filter(stack -> !stack.isEmpty()).map(stack -> FoodValuesDefinition.getFoodValues(stack.getItem(), accessor.getWorld().getRecipeManager())).collect(Collectors.toList()));
                    List<ITextComponent> foodValuesRow = new ArrayList<>();
                    int categoryCount = 0;
                    for (Pair<FoodCategory, Float> entry : mergedFoodValues.entrySet()) {
                        if (categoryCount % 3 == 0 && !foodValuesRow.isEmpty()) {
                            tooltip.add(Renderables.of(foodValuesRow.toArray(new ITextComponent[0])));
                            foodValuesRow.clear();
                        }
                        if (!foodValuesRow.isEmpty()) {
                            foodValuesRow.add(Renderables.spacer(2, 0));
                        }
                        foodValuesRow.add(Renderables.item(FoodCategory.getItemStack(entry.getKey())));
                        foodValuesRow.add(Renderables.offsetText("×" + entry.getValue(), 0, 4));
                        categoryCount++;
                    }
                    if (!foodValuesRow.isEmpty()) {
                        tooltip.add(Renderables.of(foodValuesRow.toArray(new ITextComponent[0])));
                    }
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity tileEntity) {
        if (tileEntity instanceof CrockPotTileEntity) {
            CrockPotTileEntity crockPotTileEntity = (CrockPotTileEntity) tileEntity;

            // Remove Jade Inventory data so that we will render in our way
            tag.remove("jadeHandler");

            tag.put("CrockPotItemHandler", crockPotTileEntity.getItemHandler().serializeNBT());

            tag.putBoolean("DrawFoodValue", player.isShiftKeyDown());

            if (crockPotTileEntity.isCooking()) {
                tag.put("Result", crockPotTileEntity.getResult().serializeNBT());

                tag.putInt("CookingProgress", (int) (crockPotTileEntity.getCookingProgress() * 100));
            }
        }
    }
}
