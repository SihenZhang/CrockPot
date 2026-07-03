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

    public static final DeferredBlock<CrockPotBlock> CROCK_POT = registerBlock("crock_pot", props -> new CrockPotBlock(0, props), ModBlocks::crockPotProperties);
    public static final DeferredBlock<CrockPotBlock> PORTABLE_CROCK_POT = registerBlock("portable_crock_pot", props -> new CrockPotBlock(1, props), ModBlocks::crockPotProperties);

    public static final DeferredBlock<BirdcageBlock> BIRDCAGE = registerBlock("birdcage", BirdcageBlock::new, props -> props.mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion());
    public static final DeferredBlock<DryingRackBlock> DRYING_RACK = registerBlock("drying_rack", DryingRackBlock::new, props -> props.mapColor(MapColor.WOOD).strength(2.0F, 2.0F).sound(SoundType.WOOD).noOcclusion());

    public static final DeferredBlock<UnknownCropsBlock> UNKNOWN_CROPS = registerCrop("unknown_crops", UnknownCropsBlock::new);
    public static final DeferredBlock<Block> ASPARAGUS = registerCrop("asparaguses", () -> ModItems.ASPARAGUS);
    public static final DeferredBlock<CornBlock> CORN = registerCrop("corns", CornBlock::new);
    public static final DeferredBlock<Block> EGGPLANT = registerCrop("eggplants", () -> ModItems.EGGPLANT_SEEDS);
    public static final DeferredBlock<Block> GARLIC = registerCrop("garlics", () -> ModItems.GARLIC_SEEDS);
    public static final DeferredBlock<Block> ONION = registerCrop("onions", () -> ModItems.ONION_SEEDS);
    public static final DeferredBlock<Block> PEPPER = registerCrop("peppers", () -> ModItems.PEPPER_SEEDS);
    public static final DeferredBlock<Block> TOMATO = registerCrop("tomatoes", () -> ModItems.TOMATO_SEEDS);

    public static final DeferredBlock<Block> ASPARAGUS_SOUP = registerFood("asparagus_soup", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> AVAJ = registerFood("avaj", props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> BACON_EGGS = registerFood("bacon_eggs", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> BONE_SOUP = registerFood("bone_soup", 3);
    public static final DeferredBlock<Block> BONE_STEW = registerFood("bone_stew", 3, props -> props.sound(SoundType.LANTERN));
    public static final DeferredBlock<Block> BREAKFAST_SKILLET = registerFood("breakfast_skillet", 3, props -> props.sound(SoundType.LANTERN));
    public static final DeferredBlock<Block> BUNNY_STEW = registerFood("bunny_stew", 3);
    public static final DeferredBlock<Block> CALIFORNIA_ROLL = registerFood("california_roll", 3, props -> props.sound(SoundType.WOOL));
    public static final DeferredBlock<Block> CANDY = registerFood("candy", 6, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> CEVICHE = registerFood("ceviche", 3);
    public static final DeferredBlock<Block> FISH_STICKS = registerFood("fish_sticks", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> FISH_TACOS = registerFood("fish_tacos", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> FLOWER_SALAD = registerFood("flower_salad", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> FROGGLE_BUNWICH = registerFood("froggle_bunwich", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> FRUIT_MEDLEY = registerFood("fruit_medley", 3, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> GAZPACHO = registerFood("gazpacho", 3, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> GLOW_BERRY_MOUSSE = registerFood("glow_berry_mousse", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> GUMMY_CAKE = registerFood("gummy_cake", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> HONEY_HAM = registerFood("honey_ham", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> HONEY_NUGGETS = registerFood("honey_nuggets", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> HOT_CHILI = registerFood("hot_chili", 3);
    public static final DeferredBlock<Block> HOT_COCOA = registerFood("hot_cocoa", 3, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> ICE_CREAM = registerFood("ice_cream", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> ICED_TEA = registerFood("iced_tea", 3, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> JAMMY_PRESERVES = registerFood("jammy_preserves", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> KABOBS = registerFood("kabobs", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> MASHED_POTATOES = registerFood("mashed_potatoes", 4, props -> props.sound(SoundType.WOOL));
    public static final DeferredBlock<Block> MEAT_BALLS = registerFood("meat_balls", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> MONSTER_LASAGNA = registerFood("monster_lasagna", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> MONSTER_TARTARE = registerFood("monster_tartare", 3);
    public static final DeferredBlock<Block> MOQUECA = registerFood("moqueca", 3, props -> props.sound(SoundType.LANTERN));
    public static final DeferredBlock<Block> MUSHY_CAKE = registerFood("mushy_cake", 3, props -> props.sound(SoundType.WOOL));
    public static final DeferredBlock<Block> NETHEROSIA = registerFood("netherosia");
    public static final DeferredBlock<Block> PEPPER_POPPER = registerFood("pepper_popper", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> PEROGIES = registerFood("perogies", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> PLAIN_OMELETTE = registerFood("plain_omelette", 3, props -> props.sound(SoundType.WOOL));
    public static final DeferredBlock<Block> POTATO_SOUFFLE = registerFood("potato_souffle", 4, props -> props.sound(SoundType.WOOL));
    public static final DeferredBlock<Block> POTATO_TORNADO = registerFood("potato_tornado", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<PowCakeBlock> POW_CAKE = registerBlock("pow_cake", PowCakeBlock::new, props -> props.sound(SoundType.WOOL));
    public static final DeferredBlock<Block> PUMPKIN_COOKIE = registerFood("pumpkin_cookie", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> RATATOUILLE = registerFood("ratatouille", 3, props -> props.sound(SoundType.LANTERN));
    public static final DeferredBlock<Block> SALMON_SUSHI = registerFood("salmon_sushi", 3, props -> props.sound(SoundType.WOOL));
    public static final DeferredBlock<Block> SALSA = registerFood("salsa", 3);
    public static final DeferredBlock<Block> SCOTCH_EGG = registerFood("scotch_egg", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> SEAFOOD_GUMBO = registerFood("seafood_gumbo", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> SNAKE_BONE_SOUP = registerFood("snake_bone_soup", 3);
    public static final DeferredBlock<Block> STEAMED_HAM_SANDWICH = registerFood("steamed_ham_sandwich", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> STEAMED_STICKS = registerFood("steamed_sticks", props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> STUFFED_EGGPLANT = registerFood("stuffed_eggplant", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> SURF_N_TURF = registerFood("surf_n_turf", 3, props -> props.sound(SoundType.WOOD));
    public static final DeferredBlock<Block> TAFFY = registerFood("taffy", 6, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> TEA = registerFood("tea", 3, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> TROPICAL_BOUILLABAISSE = registerFood("tropical_bouillabaisse", 3);
    public static final DeferredBlock<Block> TURKEY_DINNER = registerFood("turkey_dinner", 3, props -> props.sound(SoundType.SWEET_BERRY_BUSH));
    public static final DeferredBlock<Block> VEG_STINGER = registerFood("veg_stinger", 3, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> VOLT_GOAT_JELLY = registerFood("volt_goat_jelly", 3, props -> props.sound(SoundType.CORAL_BLOCK));
    public static final DeferredBlock<Block> WATERMELON_ICLE = registerFood("watermelon_icle", 3, props -> props.sound(SoundType.GLASS));
    public static final DeferredBlock<Block> WET_GOOP = registerFood("wet_goop", 3, props -> props.sound(SoundType.CORAL_BLOCK));

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

    private static BlockBehaviour.Properties crockPotProperties(BlockBehaviour.Properties properties) {
        return properties.requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .lightLevel(state -> state.getValue(CrockPotBlock.LIT) ? 13 : 0)
                .noOcclusion();
    }

    private static DeferredBlock<Block> registerCrop(String name, Supplier<ItemLike> seed) {
        return registerCrop(name, props -> new AbstractCropBlock(props) {
            @Override
            protected ItemLike getBaseSeedId() {
                return seed.get();
            }
        });
    }

    private static <B extends Block> DeferredBlock<B> registerCrop(String name, Function<BlockBehaviour.Properties, ? extends B> factory) {
        return registerBlock(name, factory, props -> props.mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY));
    }

    private static DeferredBlock<Block> registerFood(String name) {
        return registerFood(name, UnaryOperator.identity());
    }

    private static DeferredBlock<Block> registerFood(String name, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerBlock(name, CrockPotFoodBlock::new, props -> properties.apply(props.noOcclusion()));
    }

    private static DeferredBlock<Block> registerFood(String name, int maxStacks) {
        return registerFood(name, maxStacks, UnaryOperator.identity());
    }

    private static DeferredBlock<Block> registerFood(String name, int maxStacks, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerBlock(name, props -> new CrockPotStackableFoodBlock(maxStacks, props), props -> properties.apply(props.noOcclusion()));
    }

    private static <B extends Block> DeferredBlock<B> registerBlock(
            String name,
            Function<BlockBehaviour.Properties, ? extends B> factory,
            UnaryOperator<BlockBehaviour.Properties> properties) {
        return BLOCKS.registerBlock(name, factory, properties);
    }
}
