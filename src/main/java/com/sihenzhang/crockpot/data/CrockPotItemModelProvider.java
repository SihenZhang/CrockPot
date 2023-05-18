package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class CrockPotItemModelProvider extends ItemModelProvider {
    public CrockPotItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.simpleItem(CrockPotItems.UNKNOWN_SEEDS.get());
        this.simpleItem(CrockPotItems.ASPARAGUS_SEEDS.get());
        this.simpleItem(CrockPotItems.ASPARAGUS.get());
        this.simpleItem(CrockPotItems.CORN_SEEDS.get());
        this.simpleItem(CrockPotItems.CORN.get());
        this.simpleItem(CrockPotItems.POPCORN.get());
        this.simpleItem(CrockPotItems.EGGPLANT_SEEDS.get());
        this.simpleItem(CrockPotItems.EGGPLANT.get());
        this.simpleItem(CrockPotItems.COOKED_EGGPLANT.get());
        this.simpleItem(CrockPotItems.GARLIC_SEEDS.get());
        this.simpleItem(CrockPotItems.GARLIC.get());
        this.simpleItem(CrockPotItems.ONION_SEEDS.get());
        this.simpleItem(CrockPotItems.ONION.get());
        this.simpleItem(CrockPotItems.PEPPER_SEEDS.get());
        this.simpleItem(CrockPotItems.PEPPER.get());
        this.simpleItem(CrockPotItems.TOMATO_SEEDS.get());
        this.simpleItem(CrockPotItems.TOMATO.get());

        this.simpleItem(CrockPotItems.BIRDCAGE.get());

        this.simpleItem(CrockPotItems.BREAKFAST_SKILLET.get());
        this.simpleItem(CrockPotItems.GLOW_BERRY_MOUSSE.get());
        this.simpleItem(CrockPotItems.PLAIN_OMELETTE.get());
        this.simpleItem(CrockPotItems.SCOTCH_EGG.get());
    }

    public ItemModelBuilder blockItem(Block block) {
        return this.blockItem(block, RLUtils.createRL("block/" + getBlockName(block)));
    }

    public ItemModelBuilder blockItem(Block block, ResourceLocation model) {
        return this.withExistingParent(getBlockName(block), model);
    }

    public ItemModelBuilder simpleItem(Item item) {
        return this.simpleItem(item, RLUtils.createRL("item/" + getItemName(item)));
    }

    public ItemModelBuilder simpleItem(Item item, ResourceLocation texture) {
        return this.item(getItemName(item), texture);
    }

    public ItemModelBuilder item(String name, ResourceLocation texture) {
        return this.singleTexture(name, RLUtils.createVanillaRL("item/generated"), "layer0", texture);
    }

    public ItemModelBuilder simpleHandheldItem(Item item) {
        return this.simpleHandheldItem(item, RLUtils.createRL("item/" + getItemName(item)));
    }

    public ItemModelBuilder simpleHandheldItem(Item item, ResourceLocation texture) {
        return this.handheldItem(getItemName(item), texture);
    }

    public ItemModelBuilder handheldItem(String name, ResourceLocation texture) {
        return this.singleTexture(name, RLUtils.createVanillaRL("item/handheld"), "layer0", texture);
    }

    protected static String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    protected static String getItemName(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }
}
