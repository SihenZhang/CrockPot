package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public final class FoodCategoryAtlas extends TextureAtlasHolder {
    private static FoodCategoryAtlas instance;

    private FoodCategoryAtlas() {
        super(Minecraft.getInstance().getTextureManager(), RLUtils.createRL("textures/atlas/food_categories.png"), RLUtils.createRL("food_categories"));
    }

    @SubscribeEvent
    public static void registerReloadListener(final RegisterClientReloadListenersEvent event) {
        instance = new FoodCategoryAtlas();
        event.registerReloadListener(instance);
    }

    /**
     * A category namespace:path uses assets/namespace/textures/food_category/path.png.
     */
    public static TextureAtlasSprite get(Holder<FoodCategory> category) {
        return instance.getSprite(category.unwrapKey().map(ResourceKey::location).orElseGet(MissingTextureAtlasSprite::getLocation));
    }
}
