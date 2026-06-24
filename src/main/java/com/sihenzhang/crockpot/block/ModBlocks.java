package com.sihenzhang.crockpot.block;

import com.google.common.base.Suppliers;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.food.CrockPotFoodBlock;
import com.sihenzhang.crockpot.block.food.CrockPotStackableFoodBlock;
import com.sihenzhang.crockpot.block.food.PowCakeBlock;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ModBlocks {
    private ModBlocks() {
    }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CrockPot.MOD_ID);

    public static final DeferredBlock<CrockPotBlock> CROCK_POT = BLOCKS.registerBlock("crock_pot", properties -> new CrockPotBlock(properties, 0));
    public static final DeferredBlock<CrockPotBlock> PORTABLE_CROCK_POT = BLOCKS.registerBlock("portable_crock_pot", properties -> new CrockPotBlock(properties, 1));

    public static final DeferredBlock<BirdcageBlock> BIRDCAGE = BLOCKS.registerBlock("birdcage", BirdcageBlock::new);
    public static final DeferredBlock<DryingRackBlock> DRYING_RACK = BLOCKS.registerBlock("drying_rack", DryingRackBlock::new);

    public static final DeferredBlock<UnknownCropsBlock> UNKNOWN_CROPS = registerBlock("unknown_crops", UnknownCropsBlock::new, cropProperties());
    public static final DeferredBlock<Block> ASPARAGUS = registerCrop("asparaguses", () -> ModItems.ASPARAGUS.get());
    public static final DeferredBlock<CornBlock> CORN = registerBlock("corns", CornBlock::new, cropProperties());
    public static final DeferredBlock<Block> EGGPLANT = registerCrop("eggplants", () -> ModItems.EGGPLANT_SEEDS.get());
    public static final DeferredBlock<Block> GARLIC = registerCrop("garlics", () -> ModItems.GARLIC_SEEDS.get());
    public static final DeferredBlock<Block> ONION = registerCrop("onions", () -> ModItems.ONION_SEEDS.get());
    public static final DeferredBlock<Block> PEPPER = registerCrop("peppers", () -> ModItems.PEPPER_SEEDS.get());
    public static final DeferredBlock<Block> TOMATO = registerCrop("tomatoes", () -> ModItems.TOMATO_SEEDS.get());

    public static final DeferredBlock<Block> ASPARAGUS_SOUP = registerStackableFood("asparagus_soup", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> AVAJ = registerFood("avaj", SoundType.GLASS);
    public static final DeferredBlock<Block> BACON_EGGS = registerStackableFood("bacon_eggs", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> BONE_SOUP = registerStackableFood("bone_soup", 3);
    public static final DeferredBlock<Block> BONE_STEW = registerStackableFood("bone_stew", SoundType.LANTERN, 3);
    public static final DeferredBlock<Block> BREAKFAST_SKILLET = registerStackableFood("breakfast_skillet", SoundType.LANTERN, 3);
    public static final DeferredBlock<Block> BUNNY_STEW = registerStackableFood("bunny_stew", 3);
    public static final DeferredBlock<Block> CALIFORNIA_ROLL = registerStackableFood("california_roll", SoundType.WOOL, 3);
    public static final DeferredBlock<Block> CANDY = registerStackableFood("candy", SoundType.GLASS, 6);
    public static final DeferredBlock<Block> CEVICHE = registerStackableFood("ceviche", 3);
    public static final DeferredBlock<Block> FISH_STICKS = registerStackableFood("fish_sticks", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> FISH_TACOS = registerStackableFood("fish_tacos", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> FLOWER_SALAD = registerStackableFood("flower_salad", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> FROGGLE_BUNWICH = registerStackableFood("froggle_bunwich", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> FRUIT_MEDLEY = registerStackableFood("fruit_medley", SoundType.GLASS, 3);
    public static final DeferredBlock<Block> GAZPACHO = registerStackableFood("gazpacho", SoundType.GLASS, 3);
    public static final DeferredBlock<Block> GLOW_BERRY_MOUSSE = registerStackableFood("glow_berry_mousse", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> GUMMY_CAKE = registerStackableFood("gummy_cake", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> HONEY_HAM = registerStackableFood("honey_ham", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> HONEY_NUGGETS = registerStackableFood("honey_nuggets", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> HOT_CHILI = registerStackableFood("hot_chili", 3);
    public static final DeferredBlock<Block> HOT_COCOA = registerStackableFood("hot_cocoa", SoundType.GLASS, 3);
    public static final DeferredBlock<Block> ICE_CREAM = registerStackableFood("ice_cream", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> ICED_TEA = registerStackableFood("iced_tea", SoundType.GLASS, 3);
    public static final DeferredBlock<Block> JAMMY_PRESERVES = registerStackableFood("jammy_preserves", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> KABOBS = registerStackableFood("kabobs", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> MASHED_POTATOES = registerStackableFood("mashed_potatoes", SoundType.WOOL, 4);
    public static final DeferredBlock<Block> MEAT_BALLS = registerStackableFood("meat_balls", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> MONSTER_LASAGNA = registerStackableFood("monster_lasagna", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> MONSTER_TARTARE = registerStackableFood("monster_tartare", 3);
    public static final DeferredBlock<Block> MOQUECA = registerStackableFood("moqueca", SoundType.LANTERN, 3);
    public static final DeferredBlock<Block> MUSHY_CAKE = registerStackableFood("mushy_cake", SoundType.WOOL, 3);
    public static final DeferredBlock<Block> NETHEROSIA = registerFood("netherosia");
    public static final DeferredBlock<Block> PEPPER_POPPER = registerStackableFood("pepper_popper", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> PEROGIES = registerStackableFood("perogies", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> PLAIN_OMELETTE = registerStackableFood("plain_omelette", SoundType.WOOL, 3);
    public static final DeferredBlock<Block> POTATO_SOUFFLE = registerStackableFood("potato_souffle", SoundType.WOOL, 4);
    public static final DeferredBlock<Block> POTATO_TORNADO = registerStackableFood("potato_tornado", SoundType.WOOD, 3);
    public static final DeferredBlock<PowCakeBlock> POW_CAKE = BLOCKS.registerBlock("pow_cake", PowCakeBlock::new);
    public static final DeferredBlock<Block> PUMPKIN_COOKIE = registerStackableFood("pumpkin_cookie", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> RATATOUILLE = registerStackableFood("ratatouille", SoundType.LANTERN, 3);
    public static final DeferredBlock<Block> SALMON_SUSHI = registerStackableFood("salmon_sushi", SoundType.WOOL, 3);
    public static final DeferredBlock<Block> SALSA = registerStackableFood("salsa", 3);
    public static final DeferredBlock<Block> SCOTCH_EGG = registerStackableFood("scotch_egg", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> SEAFOOD_GUMBO = registerStackableFood("seafood_gumbo", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> SNAKE_BONE_SOUP = registerStackableFood("snake_bone_soup", 3);
    public static final DeferredBlock<Block> STEAMED_HAM_SANDWICH = registerStackableFood("steamed_ham_sandwich", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> STEAMED_STICKS = registerFood("steamed_sticks", SoundType.WOOD);
    public static final DeferredBlock<Block> STUFFED_EGGPLANT = registerStackableFood("stuffed_eggplant", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> SURF_N_TURF = registerStackableFood("surf_n_turf", SoundType.WOOD, 3);
    public static final DeferredBlock<Block> TAFFY = registerStackableFood("taffy", SoundType.GLASS, 6);
    public static final DeferredBlock<Block> TEA = registerStackableFood("tea", SoundType.GLASS, 3);
    public static final DeferredBlock<Block> TROPICAL_BOUILLABAISSE = registerStackableFood("tropical_bouillabaisse", 3);
    public static final DeferredBlock<Block> TURKEY_DINNER = registerStackableFood("turkey_dinner", SoundType.SWEET_BERRY_BUSH, 3);
    public static final DeferredBlock<Block> VEG_STINGER = registerStackableFood("veg_stinger", SoundType.GLASS, 3);
    public static final DeferredBlock<Block> VOLT_GOAT_JELLY = registerStackableFood("volt_goat_jelly", SoundType.CORAL_BLOCK, 3);
    public static final DeferredBlock<Block> WATERMELON_ICLE = registerStackableFood("watermelon_icle", SoundType.GLASS, 3);
    public static final DeferredBlock<Block> WET_GOOP = registerStackableFood("wet_goop", SoundType.CORAL_BLOCK, 3);

    public static final Supplier<List<Block>> FOODS = Suppliers.memoize(() -> List.of(
            ASPARAGUS_SOUP.get(), AVAJ.get(), BACON_EGGS.get(), BONE_SOUP.get(), BONE_STEW.get(),
            BREAKFAST_SKILLET.get(), BUNNY_STEW.get(), CALIFORNIA_ROLL.get(), CANDY.get(), CEVICHE.get(),
            FISH_STICKS.get(), FISH_TACOS.get(), FLOWER_SALAD.get(), FROGGLE_BUNWICH.get(), FRUIT_MEDLEY.get(),
            GAZPACHO.get(), GLOW_BERRY_MOUSSE.get(), GUMMY_CAKE.get(), HONEY_HAM.get(), HONEY_NUGGETS.get(),
            HOT_CHILI.get(), HOT_COCOA.get(), ICE_CREAM.get(), ICED_TEA.get(), JAMMY_PRESERVES.get(),
            KABOBS.get(), MASHED_POTATOES.get(), MEAT_BALLS.get(), MONSTER_LASAGNA.get(), MONSTER_TARTARE.get(),
            MOQUECA.get(), MUSHY_CAKE.get(), PEPPER_POPPER.get(), PEROGIES.get(), PLAIN_OMELETTE.get(),
            POTATO_SOUFFLE.get(), POTATO_TORNADO.get(), POW_CAKE.get(), PUMPKIN_COOKIE.get(), RATATOUILLE.get(),
            SALMON_SUSHI.get(), SALSA.get(), SCOTCH_EGG.get(), SEAFOOD_GUMBO.get(), SNAKE_BONE_SOUP.get(),
            STEAMED_HAM_SANDWICH.get(), STUFFED_EGGPLANT.get(), SURF_N_TURF.get(), TAFFY.get(), TEA.get(),
            TROPICAL_BOUILLABAISSE.get(), TURKEY_DINNER.get(), VEG_STINGER.get(), VOLT_GOAT_JELLY.get(), WATERMELON_ICLE.get(),
            WET_GOOP.get()
    ));
    public static final Supplier<List<Block>> NON_FOODS = Suppliers.memoize(() -> List.of(
            NETHEROSIA.get(), STEAMED_STICKS.get()
    ));

    private static DeferredBlock<Block> registerFood(String name) {
        return BLOCKS.registerBlock(name, CrockPotFoodBlock::new);
    }

    private static final UnaryOperator<BlockBehaviour.Properties> CROP_PROPERTIES = props -> props.mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);

    private static UnaryOperator<BlockBehaviour.Properties> cropProperties() {
        return props -> props.mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);
    }

    private static DeferredBlock<Block> registerCrop(String name, Supplier<? extends ItemLike> seed) {
        return registerBlock(name, props -> new AbstractCropBlock(props) {
            @Override
            protected ItemLike getBaseSeedId() {
                return seed.get();
            }
        }, cropProperties());
    }

    private static DeferredBlock<Block> registerFood(String name, SoundType soundType) {
        return BLOCKS.registerBlock(name, properties -> new CrockPotFoodBlock(properties.sound(soundType)));
    }

    private static DeferredBlock<Block> registerStackableFood(String name, int maxStacks) {
        return BLOCKS.registerBlock(name, properties -> CrockPotStackableFoodBlock.of(properties, maxStacks));
    }

    private static DeferredBlock<Block> registerStackableFood(String name, SoundType soundType, int maxStacks) {
        return BLOCKS.registerBlock(name, properties -> CrockPotStackableFoodBlock.of(properties.sound(soundType), maxStacks));
    }

    private static <B extends Block> DeferredBlock<B> registerBlock(
            String name,
            Function<BlockBehaviour.Properties, ? extends B> factory,
            UnaryOperator<BlockBehaviour.Properties> properties) {
        return BLOCKS.registerBlock(name, factory, properties);
    }
}
