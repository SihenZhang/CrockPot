package com.sihenzhang.crockpot.block;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.food.CrockPotFoodBlock;
import com.sihenzhang.crockpot.block.food.CrockPotStackableFoodBlock;
import com.sihenzhang.crockpot.block.food.PowCakeBlock;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public final class CrockPotBlocks {
    private CrockPotBlocks() {
    }

    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, CrockPot.MOD_ID);

    static {
        BLOCK_TYPES.register("crock_pot", () -> CrockPotBlock.CODEC);
        BLOCK_TYPES.register("birdcage", () -> BirdcageBlock.CODEC);
        BLOCK_TYPES.register("crop", () -> AbstractCrockPotCropBlock.CODEC);
        BLOCK_TYPES.register("unknown_crops", () -> UnknownCropsBlock.CODEC);
        BLOCK_TYPES.register("corn", () -> CornBlock.CODEC);
        BLOCK_TYPES.register("food", () -> CrockPotFoodBlock.CODEC);
        BLOCK_TYPES.register("stackable_food", () -> CrockPotStackableFoodBlock.CODEC);
        BLOCK_TYPES.register("pow_cake", () -> PowCakeBlock.CODEC);
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, CrockPot.MOD_ID);

    public static final DeferredHolder<Block, Block> CROCK_POT = BLOCKS.register("crock_pot", () -> new CrockPotBlock(0));
    public static final DeferredHolder<Block, Block> PORTABLE_CROCK_POT = BLOCKS.register("portable_crock_pot", () -> new CrockPotBlock(1));

    public static final DeferredHolder<Block, Block> BIRDCAGE = BLOCKS.register("birdcage", BirdcageBlock::new);

    public static final DeferredHolder<Block, Block> UNKNOWN_CROPS = BLOCKS.register("unknown_crops", UnknownCropsBlock::new);
    public static final DeferredHolder<Block, Block> ASPARAGUS = BLOCKS.register("asparaguses", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.ASPARAGUS_SEEDS.get();
        }
    });
    public static final DeferredHolder<Block, Block> CORN = BLOCKS.register("corns", CornBlock::new);
    public static final DeferredHolder<Block, Block> EGGPLANT = BLOCKS.register("eggplants", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.EGGPLANT_SEEDS.get();
        }
    });
    public static final DeferredHolder<Block, Block> GARLIC = BLOCKS.register("garlics", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.GARLIC_SEEDS.get();
        }
    });
    public static final DeferredHolder<Block, Block> ONION = BLOCKS.register("onions", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.ONION_SEEDS.get();
        }
    });
    public static final DeferredHolder<Block, Block> PEPPER = BLOCKS.register("peppers", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.PEPPER_SEEDS.get();
        }
    });
    public static final DeferredHolder<Block, Block> TOMATO = BLOCKS.register("tomatoes", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.TOMATO_SEEDS.get();
        }
    });

    public static final DeferredHolder<Block, Block> ASPARAGUS_SOUP = BLOCKS.register("asparagus_soup", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> AVAJ = BLOCKS.register("avaj", () -> new CrockPotFoodBlock(BlockBehaviour.Properties.of().sound(SoundType.GLASS)));
    public static final DeferredHolder<Block, Block> BACON_EGGS = BLOCKS.register("bacon_eggs", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> BONE_SOUP = BLOCKS.register("bone_soup", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> BONE_STEW = BLOCKS.register("bone_stew", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.LANTERN), 3));
    public static final DeferredHolder<Block, Block> BREAKFAST_SKILLET = BLOCKS.register("breakfast_skillet", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.LANTERN), 3));
    public static final DeferredHolder<Block, Block> BUNNY_STEW = BLOCKS.register("bunny_stew", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> CALIFORNIA_ROLL = BLOCKS.register("california_roll", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 3));
    public static final DeferredHolder<Block, Block> CANDY = BLOCKS.register("candy", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 6));
    public static final DeferredHolder<Block, Block> CEVICHE = BLOCKS.register("ceviche", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> FISH_STICKS = BLOCKS.register("fish_sticks", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> FISH_TACOS = BLOCKS.register("fish_tacos", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> FLOWER_SALAD = BLOCKS.register("flower_salad", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> FROGGLE_BUNWICH = BLOCKS.register("froggle_bunwich", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> FRUIT_MEDLEY = BLOCKS.register("fruit_medley", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 3));
    public static final DeferredHolder<Block, Block> GAZPACHO = BLOCKS.register("gazpacho", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 3));
    public static final DeferredHolder<Block, Block> GLOW_BERRY_MOUSSE = BLOCKS.register("glow_berry_mousse", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> GUMMY_CAKE = BLOCKS.register("gummy_cake", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> HONEY_HAM = BLOCKS.register("honey_ham", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> HONEY_NUGGETS = BLOCKS.register("honey_nuggets", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> HOT_CHILI = BLOCKS.register("hot_chili", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> HOT_COCOA = BLOCKS.register("hot_cocoa", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 3));
    public static final DeferredHolder<Block, Block> ICE_CREAM = BLOCKS.register("ice_cream", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> ICED_TEA = BLOCKS.register("iced_tea", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 3));
    public static final DeferredHolder<Block, Block> JAMMY_PRESERVES = BLOCKS.register("jammy_preserves", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> KABOBS = BLOCKS.register("kabobs", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> MASHED_POTATOES = BLOCKS.register("mashed_potatoes", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 4));
    public static final DeferredHolder<Block, Block> MEAT_BALLS = BLOCKS.register("meat_balls", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> MONSTER_LASAGNA = BLOCKS.register("monster_lasagna", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> MONSTER_TARTARE = BLOCKS.register("monster_tartare", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> MOQUECA = BLOCKS.register("moqueca", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.LANTERN), 3));
    public static final DeferredHolder<Block, Block> MUSHY_CAKE = BLOCKS.register("mushy_cake", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 3));
    public static final DeferredHolder<Block, Block> NETHEROSIA = BLOCKS.register("netherosia", () -> new CrockPotFoodBlock());
    public static final DeferredHolder<Block, Block> PEPPER_POPPER = BLOCKS.register("pepper_popper", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> PEROGIES = BLOCKS.register("perogies", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> PLAIN_OMELETTE = BLOCKS.register("plain_omelette", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 3));
    public static final DeferredHolder<Block, Block> POTATO_SOUFFLE = BLOCKS.register("potato_souffle", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 4));
    public static final DeferredHolder<Block, Block> POTATO_TORNADO = BLOCKS.register("potato_tornado", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> POW_CAKE = BLOCKS.register("pow_cake", PowCakeBlock::new);
    public static final DeferredHolder<Block, Block> PUMPKIN_COOKIE = BLOCKS.register("pumpkin_cookie", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> RATATOUILLE = BLOCKS.register("ratatouille", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.LANTERN), 3));
    public static final DeferredHolder<Block, Block> SALMON_SUSHI = BLOCKS.register("salmon_sushi", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 3));
    public static final DeferredHolder<Block, Block> SALSA = BLOCKS.register("salsa", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> SCOTCH_EGG = BLOCKS.register("scotch_egg", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> SEAFOOD_GUMBO = BLOCKS.register("seafood_gumbo", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> SNAKE_BONE_SOUP = BLOCKS.register("snake_bone_soup", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> STEAMED_HAM_SANDWICH = BLOCKS.register("steamed_ham_sandwich", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> STEAMED_STICKS = BLOCKS.register("steamed_sticks", () -> new CrockPotFoodBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD)));
    public static final DeferredHolder<Block, Block> STUFFED_EGGPLANT = BLOCKS.register("stuffed_eggplant", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> SURF_N_TURF = BLOCKS.register("surf_n_turf", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.WOOD), 3));
    public static final DeferredHolder<Block, Block> TAFFY = BLOCKS.register("taffy", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 6));
    public static final DeferredHolder<Block, Block> TEA = BLOCKS.register("tea", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 3));
    public static final DeferredHolder<Block, Block> TROPICAL_BOUILLABAISSE = BLOCKS.register("tropical_bouillabaisse", () -> CrockPotStackableFoodBlock.of(3));
    public static final DeferredHolder<Block, Block> TURKEY_DINNER = BLOCKS.register("turkey_dinner", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.SWEET_BERRY_BUSH), 3));
    public static final DeferredHolder<Block, Block> VEG_STINGER = BLOCKS.register("veg_stinger", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 3));
    public static final DeferredHolder<Block, Block> VOLT_GOAT_JELLY = BLOCKS.register("volt_goat_jelly", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));
    public static final DeferredHolder<Block, Block> WATERMELON_ICLE = BLOCKS.register("watermelon_icle", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.GLASS), 3));
    public static final DeferredHolder<Block, Block> WET_GOOP = BLOCKS.register("wet_goop", () -> CrockPotStackableFoodBlock.of(BlockBehaviour.Properties.of().sound(SoundType.CORAL_BLOCK), 3));

    public static final Supplier<List<Block>> FOODS = Suppliers.memoize(() -> List.of(
            ASPARAGUS_SOUP.get(), AVAJ.get(), BACON_EGGS.get(), BONE_SOUP.get(), BONE_STEW.get(),
            BREAKFAST_SKILLET.get(), BUNNY_STEW.get(), CALIFORNIA_ROLL.get(), CANDY.get(), CEVICHE.get(),
            FISH_STICKS.get(), FISH_TACOS.get(), FLOWER_SALAD.get(), FROGGLE_BUNWICH.get(), FRUIT_MEDLEY.get(),
            GAZPACHO.get(), GLOW_BERRY_MOUSSE.get(), GUMMY_CAKE.get(), HONEY_HAM.get(), HONEY_NUGGETS.get(),
            HOT_CHILI.get(), HOT_COCOA.get(), ICE_CREAM.get(), ICED_TEA.get(), JAMMY_PRESERVES.get(),
            KABOBS.get(), MASHED_POTATOES.get(), MEAT_BALLS.get(), MONSTER_LASAGNA.get(), MONSTER_TARTARE.get(),
            MOQUECA.get(), MUSHY_CAKE.get(), NETHEROSIA.get(), PEPPER_POPPER.get(), PEROGIES.get(),
            PLAIN_OMELETTE.get(), POTATO_SOUFFLE.get(), POTATO_TORNADO.get(), POW_CAKE.get(), PUMPKIN_COOKIE.get(),
            RATATOUILLE.get(), SALMON_SUSHI.get(), SALSA.get(), SCOTCH_EGG.get(), SEAFOOD_GUMBO.get(),
            SNAKE_BONE_SOUP.get(), STEAMED_HAM_SANDWICH.get(), STEAMED_STICKS.get(), STUFFED_EGGPLANT.get(), SURF_N_TURF.get(),
            TAFFY.get(), TEA.get(), TROPICAL_BOUILLABAISSE.get(), TURKEY_DINNER.get(), VEG_STINGER.get(),
            VOLT_GOAT_JELLY.get(), WATERMELON_ICLE.get(), WET_GOOP.get()
    ));
}
