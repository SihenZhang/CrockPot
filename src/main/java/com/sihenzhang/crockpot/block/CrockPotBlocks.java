package com.sihenzhang.crockpot.block;

import com.google.common.base.Suppliers;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public final class CrockPotBlocks {
    private CrockPotBlocks() {
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);

    public static final RegistryObject<Block> CROCK_POT = BLOCKS.register("crock_pot", () -> new CrockPotBlock(0));
    public static final RegistryObject<Block> PORTABLE_CROCK_POT = BLOCKS.register("portable_crock_pot", () -> new CrockPotBlock(1));

    public static final RegistryObject<Block> BIRDCAGE = BLOCKS.register("birdcage", BirdcageBlock::new);

    public static final RegistryObject<Block> UNKNOWN_CROPS = BLOCKS.register("unknown_crops", UnknownCropsBlock::new);
    public static final RegistryObject<Block> ASPARAGUS = BLOCKS.register("asparaguses", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.ASPARAGUS_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> CORN = BLOCKS.register("corns", CornBlock::new);
    public static final RegistryObject<Block> EGGPLANT = BLOCKS.register("eggplants", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.EGGPLANT_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> GARLIC = BLOCKS.register("garlics", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.GARLIC_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> ONION = BLOCKS.register("onions", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.ONION_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> PEPPER = BLOCKS.register("peppers", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.PEPPER_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> TOMATO = BLOCKS.register("tomatoes", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.TOMATO_SEEDS.get();
        }
    });

    public static final RegistryObject<Block> ASPARAGUS_SOUP = BLOCKS.register("asparagus_soup", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> AVAJ = BLOCKS.register("avaj", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> BACON_EGGS = BLOCKS.register("bacon_eggs", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> BONE_SOUP = BLOCKS.register("bone_soup", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> BONE_STEW = BLOCKS.register("bone_stew", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> BREAKFAST_SKILLET = BLOCKS.register("breakfast_skillet", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> BUNNY_STEW = BLOCKS.register("bunny_stew", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> CALIFORNIA_ROLL = BLOCKS.register("california_roll", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> CANDY = BLOCKS.register("candy", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> CEVICHE = BLOCKS.register("ceviche", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> FISH_STICKS = BLOCKS.register("fish_sticks", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> FISH_TACOS = BLOCKS.register("fish_tacos", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> FLOWER_SALAD = BLOCKS.register("flower_salad", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> FROGGLE_BUNWICH = BLOCKS.register("froggle_bunwich", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> FRUIT_MEDLEY = BLOCKS.register("fruit_medley", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> GAZPACHO = BLOCKS.register("gazpacho", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> GLOW_BERRY_MOUSSE = BLOCKS.register("glow_berry_mousse", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> HONEY_HAM = BLOCKS.register("honey_ham", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> HONEY_NUGGETS = BLOCKS.register("honey_nuggets", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> HOT_CHILI = BLOCKS.register("hot_chili", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> HOT_COCOA = BLOCKS.register("hot_cocoa", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> ICE_CREAM = BLOCKS.register("ice_cream", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> ICED_TEA = BLOCKS.register("iced_tea", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> JAMMY_PRESERVES = BLOCKS.register("jammy_preserves", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> KABOBS = BLOCKS.register("kabobs", CrockPotFoodBlock::new);
    //    public static final RegistryObject<Block> MASHED_POTATOES = BLOCKS.register("mashed_potatoes", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> MEAT_BALLS = BLOCKS.register("meat_balls", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> MONSTER_LASAGNA = BLOCKS.register("monster_lasagna", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> MONSTER_TARTARE = BLOCKS.register("monster_tartare", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> MOQUECA = BLOCKS.register("moqueca", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> MUSHY_CAKE = BLOCKS.register("mushy_cake", CrockPotFoodBlock::new);
    //    public static final RegistryObject<Block> NETHEROSIA = BLOCKS.register("netherosia", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> PEPPER_POPPER = BLOCKS.register("pepper_popper", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> PEROGIES = BLOCKS.register("perogies", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> PLAIN_OMELETTE = BLOCKS.register("plain_omelette", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> POTATO_SOUFFLE = BLOCKS.register("potato_souffle", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> POTATO_TORNADO = BLOCKS.register("potato_tornado", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> POW_CAKE = BLOCKS.register("pow_cake", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> PUMPKIN_COOKIE = BLOCKS.register("pumpkin_cookie", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> RATATOUILLE = BLOCKS.register("ratatouille", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> SALMON_SUSHI = BLOCKS.register("salmon_sushi", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> SALSA = BLOCKS.register("salsa", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> SCOTCH_EGG = BLOCKS.register("scotch_egg", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> SEAFOOD_GUMBO = BLOCKS.register("seafood_gumbo", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> STUFFED_EGGPLANT = BLOCKS.register("stuffed_eggplant", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> SURF_N_TURF = BLOCKS.register("surf_n_turf", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> TAFFY = BLOCKS.register("taffy", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> TEA = BLOCKS.register("tea", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> TROPICAL_BOUILLABAISSE = BLOCKS.register("tropical_bouillabaisse", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> TURKEY_DINNER = BLOCKS.register("turkey_dinner", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> VEG_STINGER = BLOCKS.register("veg_stinger", CrockPotFoodBlock::new);
    //    public static final RegistryObject<Block> VOLT_GOAT_JELLY = BLOCKS.register("volt_goat_jelly", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> WATERMELON_ICLE = BLOCKS.register("watermelon_icle", CrockPotFoodBlock::new);
    public static final RegistryObject<Block> WET_GOOP = BLOCKS.register("wet_goop", CrockPotFoodBlock::new);

    public static final Supplier<List<Block>> FOODS = Suppliers.memoize(() -> List.of(
            ASPARAGUS_SOUP.get(), AVAJ.get(), BACON_EGGS.get(), BONE_SOUP.get(), BONE_STEW.get(),
            BREAKFAST_SKILLET.get(), BUNNY_STEW.get(), CALIFORNIA_ROLL.get(), CANDY.get(), CEVICHE.get(),
            FISH_STICKS.get(), FISH_TACOS.get(), FLOWER_SALAD.get(), FROGGLE_BUNWICH.get(), FRUIT_MEDLEY.get(),
            GAZPACHO.get(), GLOW_BERRY_MOUSSE.get(), HONEY_HAM.get(), HONEY_NUGGETS.get(), HOT_CHILI.get(),
            HOT_COCOA.get(), ICE_CREAM.get(), ICED_TEA.get(), JAMMY_PRESERVES.get(), KABOBS.get(),
            MEAT_BALLS.get(), MONSTER_LASAGNA.get(), MONSTER_TARTARE.get(), MOQUECA.get(), MUSHY_CAKE.get(),
            PEPPER_POPPER.get(), PEROGIES.get(), PLAIN_OMELETTE.get(), POTATO_SOUFFLE.get(), POTATO_TORNADO.get(),
            POW_CAKE.get(), PUMPKIN_COOKIE.get(), RATATOUILLE.get(), SALMON_SUSHI.get(), SALSA.get(),
            SCOTCH_EGG.get(), SEAFOOD_GUMBO.get(), STUFFED_EGGPLANT.get(), SURF_N_TURF.get(), TAFFY.get(),
            TEA.get(), TROPICAL_BOUILLABAISSE.get(), TURKEY_DINNER.get(), VEG_STINGER.get(), WATERMELON_ICLE.get(),
            WET_GOOP.get()
    ));
}
