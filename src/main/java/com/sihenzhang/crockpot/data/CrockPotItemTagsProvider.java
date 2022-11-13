package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class CrockPotItemTagsProvider extends ItemTagsProvider {
    public CrockPotItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
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

        // Curios
        this.tag(CrockPotItemTags.CURIO).add(CrockPotItems.GNAWS_COIN.get());
        this.tag(CrockPotItemTags.HEAD).add(milkmadeHats);
    }

    @Override
    public String getName() {
        return "CrockPot Item Tags";
    }
}
