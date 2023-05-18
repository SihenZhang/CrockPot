package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CrockPotItemTagsProvider extends ItemTagsProvider {
    public CrockPotItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagsProviderFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, blockTagsProviderFuture, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Pot
        this.copy(CrockPotBlockTags.CROCK_POTS, CrockPotItemTags.CROCK_POTS);

        // Milkmade Hat
        var milkmadeHats = new Item[]{CrockPotItems.MILKMADE_HAT.get(), CrockPotItems.CREATIVE_MILKMADE_HAT.get()};
        this.tag(CrockPotItemTags.MILKMADE_HATS).add(milkmadeHats);

        // Parrot Eggs
        CrockPotItems.PARROT_EGGS.forEach((variant, egg) -> this.tag(CrockPotItemTags.PARROT_EGGS).add(egg.get()));
        this.tag(Tags.Items.EGGS).addTag(CrockPotItemTags.PARROT_EGGS);

        // Forge Tags for Compatability
        this.tag(CrockPotItemTags.CROPS_ASPARAGUS).add(CrockPotItems.ASPARAGUS.get());
        this.tag(CrockPotItemTags.CROPS_CORN).add(CrockPotItems.CORN.get());
        this.tag(CrockPotItemTags.CROPS_EGGPLANT).add(CrockPotItems.EGGPLANT.get());
        this.tag(CrockPotItemTags.CROPS_GARLIC).add(CrockPotItems.GARLIC.get());
        this.tag(CrockPotItemTags.CROPS_ONION).add(CrockPotItems.ONION.get());
        this.tag(CrockPotItemTags.CROPS_PEPPER).add(CrockPotItems.PEPPER.get());
        this.tag(CrockPotItemTags.CROPS_TOMATO).add(CrockPotItems.TOMATO.get());
        this.tag(Tags.Items.CROPS).addTags(CrockPotItemTags.CROPS_ASPARAGUS, CrockPotItemTags.CROPS_CORN, CrockPotItemTags.CROPS_EGGPLANT, CrockPotItemTags.CROPS_GARLIC, CrockPotItemTags.CROPS_ONION, CrockPotItemTags.CROPS_PEPPER, CrockPotItemTags.CROPS_TOMATO);
        this.tag(CrockPotItemTags.SEEDS_ASPARAGUS).add(CrockPotItems.ASPARAGUS_SEEDS.get());
        this.tag(CrockPotItemTags.SEEDS_CORN).add(CrockPotItems.CORN_SEEDS.get());
        this.tag(CrockPotItemTags.SEEDS_EGGPLANT).add(CrockPotItems.EGGPLANT_SEEDS.get());
        this.tag(CrockPotItemTags.SEEDS_GARLIC).add(CrockPotItems.GARLIC_SEEDS.get());
        this.tag(CrockPotItemTags.SEEDS_ONION).add(CrockPotItems.ONION_SEEDS.get());
        this.tag(CrockPotItemTags.SEEDS_PEPPER).add(CrockPotItems.PEPPER_SEEDS.get());
        this.tag(CrockPotItemTags.SEEDS_TOMATO).add(CrockPotItems.TOMATO_SEEDS.get());
        this.tag(Tags.Items.SEEDS).addTags(CrockPotItemTags.SEEDS_ASPARAGUS, CrockPotItemTags.SEEDS_CORN, CrockPotItemTags.SEEDS_EGGPLANT, CrockPotItemTags.SEEDS_GARLIC, CrockPotItemTags.SEEDS_ONION, CrockPotItemTags.SEEDS_PEPPER, CrockPotItemTags.SEEDS_TOMATO);
        this.tag(CrockPotItemTags.VEGETABLES_BEETROOT).add(Items.BEETROOT);
        this.tag(CrockPotItemTags.VEGETABLES_CARROT).add(Items.CARROT);
        this.tag(CrockPotItemTags.VEGETABLES_POTATO).add(Items.POTATO);
        this.tag(CrockPotItemTags.VEGETABLES_PUMPKIN).add(Items.PUMPKIN);
        this.tag(CrockPotItemTags.VEGETABLES_ASPARAGUS).add(CrockPotItems.ASPARAGUS.get());
        this.tag(CrockPotItemTags.VEGETABLES_CORN).add(CrockPotItems.CORN.get());
        this.tag(CrockPotItemTags.VEGETABLES_EGGPLANT).add(CrockPotItems.EGGPLANT.get());
        this.tag(CrockPotItemTags.VEGETABLES_GARLIC).add(CrockPotItems.GARLIC.get());
        this.tag(CrockPotItemTags.VEGETABLES_ONION).add(CrockPotItems.ONION.get());
        this.tag(CrockPotItemTags.VEGETABLES_PEPPER).add(CrockPotItems.PEPPER.get());
        this.tag(CrockPotItemTags.VEGETABLES_TOMATO).add(CrockPotItems.TOMATO.get());
        this.tag(CrockPotItemTags.VEGETABLES).addTags(CrockPotItemTags.VEGETABLES_BEETROOT, CrockPotItemTags.VEGETABLES_CARROT, CrockPotItemTags.VEGETABLES_POTATO, CrockPotItemTags.VEGETABLES_PUMPKIN, CrockPotItemTags.VEGETABLES_ASPARAGUS, CrockPotItemTags.VEGETABLES_CORN, CrockPotItemTags.VEGETABLES_EGGPLANT, CrockPotItemTags.VEGETABLES_GARLIC, CrockPotItemTags.VEGETABLES_ONION, CrockPotItemTags.VEGETABLES_PEPPER, CrockPotItemTags.VEGETABLES_TOMATO);
        this.tag(CrockPotItemTags.FRUITS_APPLE).add(Items.APPLE);
        this.tag(CrockPotItemTags.FRUITS).addTag(CrockPotItemTags.FRUITS_APPLE);
        this.tag(CrockPotItemTags.RAW_BEEF).add(Items.BEEF);
        this.tag(CrockPotItemTags.RAW_CHICKEN).add(Items.CHICKEN);
        this.tag(CrockPotItemTags.RAW_MUTTON).add(Items.MUTTON);
        this.tag(CrockPotItemTags.RAW_PORK).add(Items.PORKCHOP);
        this.tag(CrockPotItemTags.RAW_RABBIT).add(Items.RABBIT);
        this.tag(CrockPotItemTags.COOKED_BEEF).add(Items.COOKED_BEEF);
        this.tag(CrockPotItemTags.COOKED_CHICKEN).add(Items.COOKED_CHICKEN);
        this.tag(CrockPotItemTags.COOKED_MUTTON).add(Items.COOKED_MUTTON);
        this.tag(CrockPotItemTags.COOKED_PORK).add(Items.COOKED_PORKCHOP);
        this.tag(CrockPotItemTags.COOKED_RABBIT).add(Items.COOKED_RABBIT);
        this.tag(CrockPotItemTags.RAW_FISHES_COD).add(Items.COD);
        this.tag(CrockPotItemTags.RAW_FISHES_SALMON).add(Items.SALMON);
        this.tag(CrockPotItemTags.RAW_FISHES_TROPICAL_FISH).add(Items.TROPICAL_FISH);
        this.tag(CrockPotItemTags.RAW_FISHES).addTags(CrockPotItemTags.RAW_FISHES_COD, CrockPotItemTags.RAW_FISHES_SALMON, CrockPotItemTags.RAW_FISHES_TROPICAL_FISH);
        this.tag(CrockPotItemTags.COOKED_FISHES_COD).add(Items.COOKED_COD);
        this.tag(CrockPotItemTags.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON);
        this.tag(CrockPotItemTags.COOKED_FISHES).addTags(CrockPotItemTags.COOKED_FISHES_COD, CrockPotItemTags.COOKED_FISHES_SALMON);
        this.tag(CrockPotItemTags.RAW_FROGS).add(CrockPotItems.FROG_LEGS.get());
        this.tag(CrockPotItemTags.COOKED_FROGS).add(CrockPotItems.COOKED_FROG_LEGS.get());

        // Curios
        this.tag(CrockPotItemTags.CURIO).add(CrockPotItems.GNAWS_COIN.get());
        this.tag(CrockPotItemTags.HEAD).add(milkmadeHats);
    }

    @Override
    public String getName() {
        return "CrockPot Item Tags";
    }
}
