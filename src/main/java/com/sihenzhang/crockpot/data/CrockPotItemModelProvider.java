package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CrockPotItemModelProvider extends ItemModelProvider {
    public CrockPotItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.blockItem(ModBlocks.CROCK_POT.get(), RLUtils.mod("block/crock_pot_gui"));
        this.blockItem(ModBlocks.PORTABLE_CROCK_POT.get(), RLUtils.mod("block/portable_crock_pot_gui"));

        this.simpleItem(ModItems.UNKNOWN_SEEDS.get());
        this.simpleItem(ModItems.ASPARAGUS_SEEDS.get());
        this.simpleItem(ModItems.ASPARAGUS.get());
        this.simpleItem(ModItems.CORN_SEEDS.get());
        this.simpleItem(ModItems.CORN.get());
        this.simpleItem(ModItems.POPCORN.get());
        this.simpleItem(ModItems.EGGPLANT_SEEDS.get());
        this.simpleItem(ModItems.EGGPLANT.get());
        this.simpleItem(ModItems.COOKED_EGGPLANT.get());
        this.simpleItem(ModItems.GARLIC_SEEDS.get());
        this.simpleItem(ModItems.GARLIC.get());
        this.simpleItem(ModItems.ONION_SEEDS.get());
        this.simpleItem(ModItems.ONION.get());
        this.simpleItem(ModItems.PEPPER_SEEDS.get());
        this.simpleItem(ModItems.PEPPER.get());
        this.simpleItem(ModItems.TOMATO_SEEDS.get());
        this.simpleItem(ModItems.TOMATO.get());

        this.simpleItem(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get());
        this.simpleItem(ModItems.BIRDCAGE.get());
        this.simpleItem(ModItems.VOLT_GOAT_HORN.get());

        withExistingParent(getItemName(ModItems.VOLT_GOAT_SPAWN_EGG.get()), RLUtils.vanilla("item/template_spawn_egg"));

        this.simpleItem(ModItems.BREAKFAST_SKILLET.get());
        this.simpleItem(ModItems.GLOW_BERRY_MOUSSE.get());
        this.simpleItem(ModItems.PLAIN_OMELETTE.get());
        this.simpleItem(ModItems.SCOTCH_EGG.get());
        this.simpleItem(ModItems.VOLT_GOAT_JELLY.get());
    }

    public ItemModelBuilder blockItem(Block block) {
        return this.blockItem(block, RLUtils.mod("block/" + getBlockName(block)));
    }

    public ItemModelBuilder blockItem(Block block, ResourceLocation model) {
        return this.withExistingParent(getBlockName(block), model);
    }

    public ItemModelBuilder simpleItem(Item item) {
        return this.simpleItem(item, RLUtils.mod("item/" + getItemName(item)));
    }

    public ItemModelBuilder simpleItem(Item item, ResourceLocation texture) {
        return this.item(getItemName(item), texture);
    }

    public ItemModelBuilder item(String name, ResourceLocation texture) {
        return this.singleTexture(name, RLUtils.vanilla("item/generated"), "layer0", texture);
    }

    public ItemModelBuilder simpleHandheldItem(Item item) {
        return this.simpleHandheldItem(item, RLUtils.mod("item/" + getItemName(item)));
    }

    public ItemModelBuilder simpleHandheldItem(Item item, ResourceLocation texture) {
        return this.handheldItem(getItemName(item), texture);
    }

    public ItemModelBuilder handheldItem(String name, ResourceLocation texture) {
        return this.singleTexture(name, RLUtils.vanilla("item/handheld"), "layer0", texture);
    }

    protected static String getBlockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    protected static String getItemName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }
}
