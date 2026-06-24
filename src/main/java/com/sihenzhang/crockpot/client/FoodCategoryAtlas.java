package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public final class FoodCategoryAtlas {
    /**
     * A food category {@code namespace:path} uses the sprite with the same ID, loaded from
     * {@code assets/namespace/textures/food_category/path.png}.
     */
    public static final Identifier ATLAS_ID = IdUtil.mod("food_categories");
    public static final Identifier ATLAS_TEXTURE = IdUtil.mod("textures/atlas/food_categories.png");

    private FoodCategoryAtlas() {
    }

    @SubscribeEvent
    public static void registerAtlas(final RegisterTextureAtlasesEvent event) {
        event.register(new AtlasManager.AtlasConfig(ATLAS_TEXTURE, ATLAS_ID, false));
    }

    public static TextureAtlasSprite getSprite(Identifier foodCategoryId) {
        return Minecraft.getInstance()
                .getAtlasManager()
                .getAtlasOrThrow(ATLAS_ID)
                .getSprite(foodCategoryId);
    }
}
