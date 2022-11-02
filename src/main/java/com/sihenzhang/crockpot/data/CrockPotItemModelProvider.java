package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrockPotItemModelProvider extends ItemModelProvider {
    public CrockPotItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.simpleItem(CrockPotRegistry.UNKNOWN_SEEDS.get());
        this.simpleItem(CrockPotRegistry.ASPARAGUS_SEEDS.get());
        this.simpleItem(CrockPotRegistry.ASPARAGUS.get());
        this.simpleItem(CrockPotRegistry.CORN_SEEDS.get());
        this.simpleItem(CrockPotRegistry.CORN.get());
        this.simpleItem(CrockPotRegistry.POPCORN.get());
        this.simpleItem(CrockPotRegistry.EGGPLANT_SEEDS.get());
        this.simpleItem(CrockPotRegistry.EGGPLANT.get());
        this.simpleItem(CrockPotRegistry.COOKED_EGGPLANT.get());
        this.simpleItem(CrockPotRegistry.GARLIC_SEEDS.get());
        this.simpleItem(CrockPotRegistry.GARLIC.get());
        this.simpleItem(CrockPotRegistry.ONION_SEEDS.get());
        this.simpleItem(CrockPotRegistry.ONION.get());
        this.simpleItem(CrockPotRegistry.PEPPER_SEEDS.get());
        this.simpleItem(CrockPotRegistry.PEPPER.get());
        this.simpleItem(CrockPotRegistry.TOMATO_SEEDS.get());
        this.simpleItem(CrockPotRegistry.TOMATO.get());

        this.simpleItem(CrockPotRegistry.BIRDCAGE_BLOCK_ITEM.get());

        this.simpleItem(CrockPotRegistry.BREAKFAST_SKILLET.get());
        this.simpleItem(CrockPotRegistry.GLOW_BERRY_MOUSSE.get());
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
        return block.getRegistryName().getPath();
    }

    protected static String getItemName(ItemLike item) {
        return item.asItem().getRegistryName().getPath();
    }
}
