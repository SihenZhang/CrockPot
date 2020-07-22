package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.base.CrockPotIngredient;
import com.sihenzhang.crockpot.base.CrockPotIngredientManager;
import com.sihenzhang.crockpot.base.CrockPotIngredientType;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.recipe.RecipeManager;
import com.sihenzhang.crockpot.recipe.requirements.Requirement;
import com.sihenzhang.crockpot.recipe.requirements.RequirementMustContainItem;
import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mod(CrockPot.MOD_ID)
public class CrockPot {
    public static final String MOD_ID = "crockpot";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(CrockPotRegistry.crockPotBlockItem.get());
        }
    };

    public static final CrockPotIngredientManager INGREDIENT_MANAGER = new CrockPotIngredientManager();
    public static final RecipeManager RECIPE_MANAGER = new RecipeManager();

    public CrockPot() {
        CrockPotRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CrockPotRegistry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClineSetupEvent);
    }

    public void onServerStarting(FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(INGREDIENT_MANAGER);
        event.getServer().getResourceManager().addReloadListener(RECIPE_MANAGER);
    }

    public void onToolTip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        CrockPotIngredient crockPotIngredient = INGREDIENT_MANAGER.getIngredientFromItem(item);
        if (crockPotIngredient != null) {
            StringBuilder result = new StringBuilder();
            List<ITextComponent> toolTip = event.getToolTip();
            boolean isFirstIngredientValue = true;
            for (Map.Entry<CrockPotIngredientType, Float> ingredient : crockPotIngredient.getIngredientValue().entrySet()) {
                if (!isFirstIngredientValue) {
                    result.append(", ");
                }
                result.append(ingredient.getKey()).append(": ").append(ingredient.getValue());
                isFirstIngredientValue = false;
            }
            toolTip.add(new StringTextComponent(result.toString()));
        }
    }

    public void onClineSetupEvent(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CrockPotRegistry.crockPotContainer.get(), CrockPotScreen::new);
        MinecraftForge.EVENT_BUS.addListener(this::onToolTip);
    }
}
