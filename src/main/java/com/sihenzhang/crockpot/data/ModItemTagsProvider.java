package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagsProviderFuture) {
        super(output, providerFuture, CrockPot.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Pot
        this.tag(ModItemTags.CROCK_POTS).add(ModItems.CROCK_POT.get(), ModItems.PORTABLE_CROCK_POT.get());

        // Milkmade Hat
        var milkmadeHats = new Item[]{ModItems.MILKMADE_HAT.get(), ModItems.CREATIVE_MILKMADE_HAT.get()};
        this.tag(ModItemTags.MILKMADE_HATS).add(milkmadeHats);

        // Parrot Eggs
        ModItems.PARROT_EGGS.forEach((variant, egg) -> this.tag(ModItemTags.PARROT_EGGS).add(egg.get()));
        this.tag(Tags.Items.EGGS).addTag(ModItemTags.PARROT_EGGS);

        this.tag(ItemTags.CHICKEN_FOOD).addAll(ModItems.SEEDS.get());
        this.tag(ItemTags.PARROT_FOOD).addAll(ModItems.SEEDS.get());
        this.tag(ItemTags.HORSE_FOOD).add(ModItems.STEAMED_STICKS.get());

        // Forge Tags for Compatability
        this.tag(ModItemTags.CROPS_ASPARAGUS).add(ModItems.ASPARAGUS.get());
        this.tag(ModItemTags.CROPS_CORN).add(ModItems.CORN.get());
        this.tag(ModItemTags.CROPS_EGGPLANT).add(ModItems.EGGPLANT.get());
        this.tag(ModItemTags.CROPS_GARLIC).add(ModItems.GARLIC.get());
        this.tag(ModItemTags.CROPS_ONION).add(ModItems.ONION.get());
        this.tag(ModItemTags.CROPS_PEPPER).add(ModItems.PEPPER.get());
        this.tag(ModItemTags.CROPS_TOMATO).add(ModItems.TOMATO.get());
        this.tag(Tags.Items.CROPS).addTags(ModItemTags.CROPS_ASPARAGUS, ModItemTags.CROPS_CORN, ModItemTags.CROPS_EGGPLANT, ModItemTags.CROPS_GARLIC, ModItemTags.CROPS_ONION, ModItemTags.CROPS_PEPPER, ModItemTags.CROPS_TOMATO);
        this.tag(ModItemTags.SEEDS_ASPARAGUS).add(ModItems.ASPARAGUS_SEEDS.get());
        this.tag(ModItemTags.SEEDS_CORN).add(ModItems.CORN_SEEDS.get());
        this.tag(ModItemTags.SEEDS_EGGPLANT).add(ModItems.EGGPLANT_SEEDS.get());
        this.tag(ModItemTags.SEEDS_GARLIC).add(ModItems.GARLIC_SEEDS.get());
        this.tag(ModItemTags.SEEDS_ONION).add(ModItems.ONION_SEEDS.get());
        this.tag(ModItemTags.SEEDS_PEPPER).add(ModItems.PEPPER_SEEDS.get());
        this.tag(ModItemTags.SEEDS_TOMATO).add(ModItems.TOMATO_SEEDS.get());
        this.tag(Tags.Items.SEEDS).addTags(ModItemTags.SEEDS_ASPARAGUS, ModItemTags.SEEDS_CORN, ModItemTags.SEEDS_EGGPLANT, ModItemTags.SEEDS_GARLIC, ModItemTags.SEEDS_ONION, ModItemTags.SEEDS_PEPPER, ModItemTags.SEEDS_TOMATO);
        this.tag(ModItemTags.VEGETABLES_BEETROOT).add(Items.BEETROOT);
        this.tag(ModItemTags.VEGETABLES_CARROT).add(Items.CARROT);
        this.tag(ModItemTags.VEGETABLES_POTATO).add(Items.POTATO);
        this.tag(ModItemTags.VEGETABLES_PUMPKIN).add(Items.PUMPKIN);
        this.tag(ModItemTags.VEGETABLES_ASPARAGUS).add(ModItems.ASPARAGUS.get());
        this.tag(ModItemTags.VEGETABLES_CORN).add(ModItems.CORN.get());
        this.tag(ModItemTags.VEGETABLES_EGGPLANT).add(ModItems.EGGPLANT.get());
        this.tag(ModItemTags.VEGETABLES_GARLIC).add(ModItems.GARLIC.get());
        this.tag(ModItemTags.VEGETABLES_ONION).add(ModItems.ONION.get());
        this.tag(ModItemTags.VEGETABLES_PEPPER).add(ModItems.PEPPER.get());
        this.tag(ModItemTags.VEGETABLES_TOMATO).add(ModItems.TOMATO.get());
        this.tag(ModItemTags.VEGETABLES).addTags(ModItemTags.VEGETABLES_BEETROOT, ModItemTags.VEGETABLES_CARROT, ModItemTags.VEGETABLES_POTATO, ModItemTags.VEGETABLES_PUMPKIN, ModItemTags.VEGETABLES_ASPARAGUS, ModItemTags.VEGETABLES_CORN, ModItemTags.VEGETABLES_EGGPLANT, ModItemTags.VEGETABLES_GARLIC, ModItemTags.VEGETABLES_ONION, ModItemTags.VEGETABLES_PEPPER, ModItemTags.VEGETABLES_TOMATO);
        this.tag(ModItemTags.FRUITS_APPLE).add(Items.APPLE);
        this.tag(ModItemTags.FRUITS).addTag(ModItemTags.FRUITS_APPLE);
        this.tag(ModItemTags.RAW_BEEF).add(Items.BEEF);
        this.tag(ModItemTags.RAW_CHICKEN).add(Items.CHICKEN);
        this.tag(ModItemTags.RAW_MUTTON).add(Items.MUTTON);
        this.tag(ModItemTags.RAW_PORK).add(Items.PORKCHOP);
        this.tag(ModItemTags.RAW_RABBIT).add(Items.RABBIT);
        this.tag(ModItemTags.COOKED_BEEF).add(Items.COOKED_BEEF);
        this.tag(ModItemTags.COOKED_CHICKEN).add(Items.COOKED_CHICKEN);
        this.tag(ModItemTags.COOKED_MUTTON).add(Items.COOKED_MUTTON);
        this.tag(ModItemTags.COOKED_PORK).add(Items.COOKED_PORKCHOP);
        this.tag(ModItemTags.COOKED_RABBIT).add(Items.COOKED_RABBIT);
        this.tag(ModItemTags.RAW_FISHES_COD).add(Items.COD);
        this.tag(ModItemTags.RAW_FISHES_SALMON).add(Items.SALMON);
        this.tag(ModItemTags.RAW_FISHES_TROPICAL_FISH).add(Items.TROPICAL_FISH);
        this.tag(ModItemTags.RAW_FISHES).addTags(ModItemTags.RAW_FISHES_COD, ModItemTags.RAW_FISHES_SALMON, ModItemTags.RAW_FISHES_TROPICAL_FISH);
        this.tag(ModItemTags.COOKED_FISHES_COD).add(Items.COOKED_COD);
        this.tag(ModItemTags.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON);
        this.tag(ModItemTags.COOKED_FISHES).addTags(ModItemTags.COOKED_FISHES_COD, ModItemTags.COOKED_FISHES_SALMON);
        this.tag(ModItemTags.RAW_FROGS).add(ModItems.FROG_LEGS.get());
        this.tag(ModItemTags.COOKED_FROGS).add(ModItems.COOKED_FROG_LEGS.get());
        this.tag(ModItemTags.COOKED_EGGS).add(ModItems.COOKED_EGG.get());
        this.tag(ModItemTags.MILK_BOTTLE).add(ModItems.MILK_BOTTLE.get());
        this.tag(ModItemTags.MILK).addTag(ModItemTags.MILK_BOTTLE);
    }

    @Override
    public String getName() {
        return "CrockPot Item Tags";
    }
}
