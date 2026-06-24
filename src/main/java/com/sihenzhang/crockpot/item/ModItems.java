package com.sihenzhang.crockpot.item;

import com.google.common.base.Suppliers;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.core.ModDataComponents;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.item.component.ItemTooltips;
import com.sihenzhang.crockpot.item.food.CandyItem;
import com.sihenzhang.crockpot.item.food.Consumables;
import com.sihenzhang.crockpot.item.food.CrockPotFoodBlockItem;
import com.sihenzhang.crockpot.item.food.CrockPotFoodItem;
import com.sihenzhang.crockpot.item.food.Foods;
import com.sihenzhang.crockpot.util.IdUtil;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ModItems {
    private ModItems() {
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CrockPot.MOD_ID);

    public static final DeferredItem<BlockItem> CROCK_POT = registerBlock("crock_pot", ModBlocks.CROCK_POT,
            CrockPotBlockItem::new);
    public static final DeferredItem<BlockItem> PORTABLE_CROCK_POT = registerBlock("portable_crock_pot",
            ModBlocks.PORTABLE_CROCK_POT,
            CrockPotBlockItem::new,
            props -> props.rarity(Rarity.UNCOMMON));

    public static final DeferredItem<BlockItem> UNKNOWN_SEEDS = registerItem("unknown_seeds", ModBlocks.UNKNOWN_CROPS);
    public static final DeferredItem<BlockItem> ASPARAGUS_SEEDS = registerItem("asparagus_seeds", ModBlocks.ASPARAGUS);
    public static final DeferredItem<Item> ASPARAGUS = registerFood("asparagus",
            prop -> prop.food(Foods.ASPARAGUS, Consumables.ASPARAGUS)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<BlockItem> CORN_SEEDS = registerItem("corn_seeds", ModBlocks.CORN);
    public static final DeferredItem<Item> CORN = registerFood("corn",
            prop -> prop.food(Foods.CORN, Consumables.CORN)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> POPCORN = registerFood("popcorn",
            prop -> prop.food(Foods.POPCORN, Consumables.POPCORN)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<BlockItem> EGGPLANT_SEEDS = registerItem("eggplant_seeds", ModBlocks.EGGPLANT);
    public static final DeferredItem<Item> EGGPLANT = registerFood("eggplant",
            prop -> prop.food(Foods.EGGPLANT, Consumables.EGGPLANT)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> COOKED_EGGPLANT = registerFood("cooked_eggplant",
            prop -> prop.food(Foods.COOKED_EGGPLANT, Consumables.COOKED_EGGPLANT)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<BlockItem> GARLIC_SEEDS = registerItem("garlic_seeds", ModBlocks.GARLIC);
    public static final DeferredItem<Item> GARLIC = registerFood("garlic",
            prop -> prop.food(Foods.GARLIC, Consumables.GARLIC)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<BlockItem> ONION_SEEDS = registerItem("onion_seeds", ModBlocks.ONION);
    public static final DeferredItem<Item> ONION = registerFood("onion",
            prop -> prop.food(Foods.ONION, Consumables.ONION)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<BlockItem> PEPPER_SEEDS = registerItem("pepper_seeds", ModBlocks.PEPPER);
    public static final DeferredItem<Item> PEPPER = registerFood("pepper",
            prop -> prop.food(Foods.PEPPER, Consumables.PEPPER)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<BlockItem> TOMATO_SEEDS = registerItem("tomato_seeds", ModBlocks.TOMATO);
    public static final DeferredItem<Item> TOMATO = registerFood("tomato",
            prop -> prop.food(Foods.TOMATO, Consumables.TOMATO)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> SMALL_JERKY = registerFood("small_jerky",
            prop -> prop.food(Foods.SMALL_JERKY, Consumables.DRIED_FOOD)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> JERKY = registerFood("jerky",
            prop -> prop.food(Foods.JERKY, Consumables.DRIED_FOOD)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> SMALL_MONSTER_JERKY = registerFood("small_monster_jerky",
            prop -> prop.food(Foods.SMALL_MONSTER_JERKY, Consumables.DRIED_FOOD)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> MONSTER_JERKY = registerFood("monster_jerky",
            prop -> prop.food(Foods.MONSTER_JERKY, Consumables.DRIED_FOOD)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> SMALL_DRIED_FISH = registerFood("small_dried_fish",
            prop -> prop.food(Foods.SMALL_DRIED_FISH, Consumables.DRIED_FOOD)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> DRIED_FISH = registerFood("dried_fish",
            prop -> prop.food(Foods.DRIED_FISH, Consumables.DRIED_FOOD)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> LARGE_DRIED_FISH = registerFood("large_dried_fish",
            prop -> prop.food(Foods.LARGE_DRIED_FISH, Consumables.DRIED_FOOD)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));

    public static final Supplier<Set<Item>> SEEDS = Suppliers.memoize(() -> Set.of(
            UNKNOWN_SEEDS.get(), ASPARAGUS_SEEDS.get(), CORN_SEEDS.get(), EGGPLANT_SEEDS.get(),
            GARLIC_SEEDS.get(), ONION_SEEDS.get(), PEPPER_SEEDS.get(), TOMATO_SEEDS.get()
    ));
    public static final Supplier<Set<Item>> CROPS = Suppliers.memoize(() -> Set.of(
            ASPARAGUS.get(), CORN.get(), EGGPLANT.get(), GARLIC.get(), ONION.get(),
            PEPPER.get(), TOMATO.get()
    ));
    public static final Supplier<Set<Item>> COOKED_CROPS = Suppliers.memoize(() -> Set.of(
            POPCORN.get(), COOKED_EGGPLANT.get()
    ));
    public static final Supplier<Set<Item>> DRIED_FOODS = Suppliers.memoize(() -> Set.of(
            SMALL_JERKY.get(), JERKY.get(), SMALL_MONSTER_JERKY.get(), MONSTER_JERKY.get(),
            SMALL_DRIED_FISH.get(), DRIED_FISH.get(), LARGE_DRIED_FISH.get()
    ));

    public static final DeferredItem<BlockItem> BIRDCAGE =
            ITEMS.registerSimpleBlockItem("birdcage", ModBlocks.BIRDCAGE);
    public static final DeferredItem<BlockItem> DRYING_RACK =
            ITEMS.registerSimpleBlockItem("drying_rack", ModBlocks.DRYING_RACK);
    public static final Map<Parrot.Variant, DeferredItem<ParrotEggItem>> PARROT_EGGS = Util.make(
            new EnumMap<>(Parrot.Variant.class),
            map -> {
                for (var variant : Parrot.Variant.values()) {
                    map.put(
                            variant,
                            registerItem(
                                    "parrot_egg_" + variant.getSerializedName(),
                                    props -> new ParrotEggItem(variant, props),
                                    props -> props.stacksTo(16)
                            )
                    );
                }
            });

    public static final DeferredItem<SmithingTemplateItem> CROCK_POT_UPGRADE_SMITHING_TEMPLATE =
            registerItem("pot_upgrade_smithing_template", props -> new SmithingTemplateItem(
                    I18nUtil.tooltip("smithing_template.pot_upgrade.applies_to").withStyle(ChatFormatting.BLUE),
                    I18nUtil.tooltip("smithing_template.pot_upgrade.ingredients").withStyle(ChatFormatting.BLUE),
                    I18nUtil.tooltip("smithing_template.pot_upgrade.base_slot_description"),
                    I18nUtil.tooltip("smithing_template.pot_upgrade.additions_slot_description"),
                    List.of(IdUtil.mod("item/empty_slot_pot")),
                    List.of(IdUtil.mod("item/empty_slot_block")),
                    props
            ));
    public static final DeferredItem<Item> BLACKSTONE_DUST = ITEMS.registerSimpleItem("blackstone_dust");
    public static final DeferredItem<Item> COLLECTED_DUST = ITEMS.registerSimpleItem("collected_dust",
            prop -> prop.component(
                    ModDataComponents.ITEM_TOOLTIPS,
                    ItemTooltips.EMPTY.withLineAdded(
                            I18nUtil.tooltip("collected_dust").withStyle(ChatFormatting.DARK_AQUA)
                    )
            ));
    public static final DeferredItem<Item> COOKED_EGG = registerFood("cooked_egg",
            prop -> prop.food(Foods.COOKED_EGG, Consumables.COOKED_EGG)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> FROG_LEGS = registerFood("frog_legs",
            prop -> prop.food(Foods.FROG_LEGS, Consumables.FROG_LEGS)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> COOKED_FROG_LEGS = registerFood("cooked_frog_legs",
            prop -> prop.food(Foods.COOKED_FROG_LEGS, Consumables.COOKED_FROG_LEGS)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> HOGLIN_NOSE = registerFood("hoglin_nose",
            prop -> prop.food(Foods.HOGLIN_NOSE, Consumables.HOGLIN_NOSE)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> COOKED_HOGLIN_NOSE = registerFood("cooked_hoglin_nose",
            prop -> prop.food(Foods.COOKED_HOGLIN_NOSE, Consumables.COOKED_HOGLIN_NOSE)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> MILK_BOTTLE = registerFood("milk_bottle",
            prop -> prop.food(Foods.MILK_BOTTLE, Consumables.MILK_BOTTLE)
                    .usingConvertsTo(Items.GLASS_BOTTLE)
                    .component(ModDataComponents.ITEM_TOOLTIPS,
                            ItemTooltips.EMPTY.withLineAdded(
                                    I18nUtil.tooltip("milk_bottle").withStyle(ChatFormatting.DARK_AQUA)
                            ))
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> SYRUP = registerFood("syrup",
            prop -> prop.food(Foods.SYRUP, Consumables.SYRUP)
                    .component(DataComponents.TOOLTIP_DISPLAY,
                            TooltipDisplay.DEFAULT.withHidden(ModDataComponents.CONSUMABLE_TOOLTIPS.get(), true)));
    public static final DeferredItem<Item> VOLT_GOAT_HORN = ITEMS.registerSimpleItem("volt_goat_horn");
    public static final DeferredItem<MilkmadeHatItem> MILKMADE_HAT = registerItem("milkmade_hat",
            MilkmadeHatItem::new,
            props -> props.durability(180).setNoCombineRepair().equippable(EquipmentSlot.HEAD).useCooldown(5.0F));
    public static final DeferredItem<MilkmadeHatItem> CREATIVE_MILKMADE_HAT =
            registerItem("creative_milkmade_hat", MilkmadeHatItem::new,
                    props -> props.stacksTo(1)
                            .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                            .equippable(EquipmentSlot.HEAD)
                            .useCooldown(1.0F)
                            .rarity(Rarity.EPIC)
                            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    public static final DeferredItem<GnawsCoinItem> GNAWS_COIN = registerItem("gnaws_coin", GnawsCoinItem::new,
            props -> props.stacksTo(1)
                    .fireResistant()
                    .rarity(Rarity.EPIC)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .component(ModDataComponents.ITEM_TOOLTIPS,
                            ItemTooltips.EMPTY.withLineAdded(
                                    I18nUtil.tooltip("gnaws_coin").withStyle(ChatFormatting.AQUA)
                            )));

    public static final DeferredItem<BlockItem> ASPARAGUS_SOUP =
            registerFood("asparagus_soup", ModBlocks.ASPARAGUS_SOUP,
                    props -> props.food(Foods.ASPARAGUS_SOUP, Consumables.ASPARAGUS_SOUP));
    public static final DeferredItem<BlockItem> AVAJ =
            registerFood("avaj", ModBlocks.AVAJ,
                    props -> props.food(Foods.AVAJ, Consumables.AVAJ).rarity(Rarity.EPIC));
    public static final DeferredItem<BlockItem> BACON_EGGS =
            registerFood("bacon_eggs", ModBlocks.BACON_EGGS,
                    props -> props.food(Foods.BACON_EGGS, Consumables.BACON_EGGS));
    public static final DeferredItem<BlockItem> BONE_SOUP =
            registerFood("bone_soup", ModBlocks.BONE_SOUP,
                    props -> props.food(Foods.BONE_SOUP, Consumables.BONE_SOUP).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> BONE_STEW =
            registerFood("bone_stew", ModBlocks.BONE_STEW,
                    props -> props.food(Foods.BONE_STEW, Consumables.BONE_STEW));
    public static final DeferredItem<BlockItem> BREAKFAST_SKILLET =
            registerFood("breakfast_skillet", ModBlocks.BREAKFAST_SKILLET,
                    props -> props.food(Foods.BREAKFAST_SKILLET, Consumables.BREAKFAST_SKILLET));
    public static final DeferredItem<BlockItem> BUNNY_STEW =
            registerFood("bunny_stew", ModBlocks.BUNNY_STEW,
                    props -> props.food(Foods.BUNNY_STEW, Consumables.BUNNY_STEW));
    public static final DeferredItem<BlockItem> CALIFORNIA_ROLL =
            registerFood("california_roll", ModBlocks.CALIFORNIA_ROLL,
                    props -> props.food(Foods.CALIFORNIA_ROLL, Consumables.CALIFORNIA_ROLL));
    public static final DeferredItem<BlockItem> CANDY =
            registerBlock("candy", CandyItem::new,
                    props -> props.food(Foods.CANDY, Consumables.CANDY));
    public static final DeferredItem<BlockItem> CEVICHE =
            registerFood("ceviche", ModBlocks.CEVICHE,
                    props -> props.food(Foods.CEVICHE, Consumables.CEVICHE));
    public static final DeferredItem<BlockItem> FISH_STICKS =
            registerFood("fish_sticks", ModBlocks.FISH_STICKS,
                    props -> props.food(Foods.FISH_STICKS, Consumables.FISH_STICKS));
    public static final DeferredItem<BlockItem> FISH_TACOS =
            registerFood("fish_tacos", ModBlocks.FISH_TACOS,
                    props -> props.food(Foods.FISH_TACOS, Consumables.FISH_TACOS));
    public static final DeferredItem<BlockItem> FLOWER_SALAD =
            registerFood("flower_salad", ModBlocks.FLOWER_SALAD,
                    props -> props.food(Foods.FLOWER_SALAD, Consumables.FLOWER_SALAD).useCooldown(3.0F));
    public static final DeferredItem<BlockItem> FROGGLE_BUNWICH =
            registerFood("froggle_bunwich", ModBlocks.FROGGLE_BUNWICH,
                    props -> props.food(Foods.FROGGLE_BUNWICH, Consumables.FROGGLE_BUNWICH));
    public static final DeferredItem<BlockItem> FRUIT_MEDLEY =
            registerFood("fruit_medley", ModBlocks.FRUIT_MEDLEY,
                    props -> props.food(Foods.FRUIT_MEDLEY, Consumables.FRUIT_MEDLEY));
    public static final DeferredItem<BlockItem> GAZPACHO =
            registerFood("gazpacho", ModBlocks.GAZPACHO,
                    props -> props.food(Foods.GAZPACHO, Consumables.GAZPACHO).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> GLOW_BERRY_MOUSSE =
            registerFood("glow_berry_mousse", ModBlocks.GLOW_BERRY_MOUSSE,
                    props -> props.food(Foods.GLOW_BERRY_MOUSSE, Consumables.GLOW_BERRY_MOUSSE)
                            .rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> GUMMY_CAKE =
            registerFood("gummy_cake", ModBlocks.GUMMY_CAKE,
                    props -> props.food(Foods.GUMMY_CAKE, Consumables.GUMMY_CAKE));
    public static final DeferredItem<BlockItem> HONEY_HAM =
            registerFood("honey_ham", ModBlocks.HONEY_HAM,
                    props -> props.food(Foods.HONEY_HAM, Consumables.HONEY_HAM));
    public static final DeferredItem<BlockItem> HONEY_NUGGETS =
            registerFood("honey_nuggets", ModBlocks.HONEY_NUGGETS,
                    props -> props.food(Foods.HONEY_NUGGETS, Consumables.HONEY_NUGGETS));
    public static final DeferredItem<BlockItem> HOT_CHILI =
            registerFood("hot_chili", ModBlocks.HOT_CHILI,
                    props -> props.food(Foods.HOT_CHILI, Consumables.HOT_CHILI));
    public static final DeferredItem<BlockItem> HOT_COCOA =
            registerFood("hot_cocoa", ModBlocks.HOT_COCOA,
                    props -> props.food(Foods.HOT_COCOA, Consumables.HOT_COCOA));
    public static final DeferredItem<BlockItem> ICE_CREAM =
            registerFood("ice_cream", ModBlocks.ICE_CREAM,
                    props -> props.food(Foods.ICE_CREAM, Consumables.ICE_CREAM).useCooldown(1.0F));
    public static final DeferredItem<BlockItem> ICED_TEA =
            registerFood("iced_tea", ModBlocks.ICED_TEA,
                    props -> props.food(Foods.ICED_TEA, Consumables.ICED_TEA));
    public static final DeferredItem<BlockItem> JAMMY_PRESERVES =
            registerFood("jammy_preserves", ModBlocks.JAMMY_PRESERVES,
                    props -> props.food(Foods.JAMMY_PRESERVES, Consumables.JAMMY_PRESERVES));
    public static final DeferredItem<BlockItem> KABOBS =
            registerFood("kabobs", ModBlocks.KABOBS,
                    props -> props.food(Foods.KABOBS, Consumables.KABOBS));
    public static final DeferredItem<BlockItem> MASHED_POTATOES =
            registerFood("mashed_potatoes", ModBlocks.MASHED_POTATOES,
                    props -> props.food(Foods.MASHED_POTATOES, Consumables.MASHED_POTATOES));
    public static final DeferredItem<BlockItem> MEAT_BALLS =
            registerFood("meat_balls", ModBlocks.MEAT_BALLS,
                    props -> props.food(Foods.MEAT_BALLS, Consumables.MEAT_BALLS));
    public static final DeferredItem<BlockItem> MONSTER_LASAGNA =
            registerFood("monster_lasagna", ModBlocks.MONSTER_LASAGNA,
                    props -> props.food(Foods.MONSTER_LASAGNA, Consumables.MONSTER_LASAGNA));
    public static final DeferredItem<BlockItem> MONSTER_TARTARE =
            registerFood("monster_tartare", ModBlocks.MONSTER_TARTARE,
                    props -> props.food(Foods.MONSTER_TARTARE, Consumables.MONSTER_TARTARE)
                            .rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> MOQUECA =
            registerFood("moqueca", ModBlocks.MOQUECA,
                    props -> props.food(Foods.MOQUECA, Consumables.MOQUECA).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> MUSHY_CAKE =
            registerFood("mushy_cake", ModBlocks.MUSHY_CAKE,
                    props -> props.food(Foods.MUSHY_CAKE, Consumables.MUSHY_CAKE).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> NETHEROSIA =
            registerBlock("netherosia", ModBlocks.NETHEROSIA, SneakPlaceBlockItem::new,
                    prop -> prop.component(
                            ModDataComponents.ITEM_TOOLTIPS,
                            ItemTooltips.EMPTY.withLineAdded(
                                    I18nUtil.tooltip("netherosia").withStyle(ChatFormatting.DARK_AQUA)
                            )
                    ));
    public static final DeferredItem<BlockItem> PEPPER_POPPER =
            registerFood("pepper_popper", ModBlocks.PEPPER_POPPER,
                    props -> props.food(Foods.PEPPER_POPPER, Consumables.PEPPER_POPPER));
    public static final DeferredItem<BlockItem> PEROGIES =
            registerFood("perogies", ModBlocks.PEROGIES,
                    props -> props.food(Foods.PEROGIES, Consumables.PEROGIES));
    public static final DeferredItem<BlockItem> PLAIN_OMELETTE =
            registerFood("plain_omelette", ModBlocks.PLAIN_OMELETTE,
                    props -> props.food(Foods.PLAIN_OMELETTE, Consumables.PLAIN_OMELETTE));
    public static final DeferredItem<BlockItem> POTATO_SOUFFLE =
            registerFood("potato_souffle", ModBlocks.POTATO_SOUFFLE,
                    props -> props.food(Foods.POTATO_SOUFFLE, Consumables.POTATO_SOUFFLE)
                            .rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> POTATO_TORNADO =
            registerFood("potato_tornado", ModBlocks.POTATO_TORNADO,
                    props -> props.food(Foods.POTATO_TORNADO, Consumables.POTATO_TORNADO));
    public static final DeferredItem<BlockItem> POW_CAKE =
            registerFood("pow_cake", ModBlocks.POW_CAKE,
                    props -> props.food(Foods.POW_CAKE, Consumables.POW_CAKE));
    public static final DeferredItem<BlockItem> PUMPKIN_COOKIE =
            registerFood("pumpkin_cookie", ModBlocks.PUMPKIN_COOKIE,
                    props -> props.food(Foods.PUMPKIN_COOKIE, Consumables.PUMPKIN_COOKIE));
    public static final DeferredItem<BlockItem> RATATOUILLE =
            registerFood("ratatouille", ModBlocks.RATATOUILLE,
                    props -> props.food(Foods.RATATOUILLE, Consumables.RATATOUILLE));
    public static final DeferredItem<BlockItem> SALMON_SUSHI =
            registerFood("salmon_sushi", ModBlocks.SALMON_SUSHI,
                    props -> props.food(Foods.SALMON_SUSHI, Consumables.SALMON_SUSHI));
    public static final DeferredItem<BlockItem> SALSA =
            registerFood("salsa", ModBlocks.SALSA,
                    props -> props.food(Foods.SALSA, Consumables.SALSA));
    public static final DeferredItem<BlockItem> SCOTCH_EGG =
            registerFood("scotch_egg", ModBlocks.SCOTCH_EGG,
                    props -> props.food(Foods.SCOTCH_EGG, Consumables.SCOTCH_EGG));
    public static final DeferredItem<BlockItem> SEAFOOD_GUMBO =
            registerFood("seafood_gumbo", ModBlocks.SEAFOOD_GUMBO,
                    props -> props.food(Foods.SEAFOOD_GUMBO, Consumables.SEAFOOD_GUMBO));
    public static final DeferredItem<BlockItem> SNAKE_BONE_SOUP =
            registerFood("snake_bone_soup", ModBlocks.SNAKE_BONE_SOUP,
                    props -> props.food(Foods.SNAKE_BONE_SOUP, Consumables.SNAKE_BONE_SOUP));
    public static final DeferredItem<BlockItem> STEAMED_HAM_SANDWICH =
            registerFood("steamed_ham_sandwich", ModBlocks.STEAMED_HAM_SANDWICH,
                    props -> props.food(Foods.STEAMED_HAM_SANDWICH, Consumables.STEAMED_HAM_SANDWICH));
    public static final DeferredItem<BlockItem> STEAMED_STICKS =
            registerBlock("steamed_sticks", ModBlocks.STEAMED_STICKS, SneakPlaceBlockItem::new,
                    props -> props.component(
                            ModDataComponents.ITEM_TOOLTIPS,
                            ItemTooltips.EMPTY.withLineAdded(
                                    I18nUtil.tooltip("steamed_sticks").withStyle(ChatFormatting.DARK_AQUA)
                            )
                    ));
    public static final DeferredItem<BlockItem> STUFFED_EGGPLANT =
            registerFood("stuffed_eggplant", ModBlocks.STUFFED_EGGPLANT,
                    props -> props.food(Foods.STUFFED_EGGPLANT, Consumables.STUFFED_EGGPLANT));
    public static final DeferredItem<BlockItem> SURF_N_TURF =
            registerFood("surf_n_turf", ModBlocks.SURF_N_TURF,
                    props -> props.food(Foods.SURF_N_TURF, Consumables.SURF_N_TURF));
    public static final DeferredItem<BlockItem> TAFFY =
            registerFood("taffy", ModBlocks.TAFFY,
                    props -> props.food(Foods.TAFFY, Consumables.TAFFY));
    public static final DeferredItem<BlockItem> TEA =
            registerFood("tea", ModBlocks.TEA,
                    props -> props.food(Foods.TEA, Consumables.TEA));
    public static final DeferredItem<BlockItem> TROPICAL_BOUILLABAISSE =
            registerFood("tropical_bouillabaisse", ModBlocks.TROPICAL_BOUILLABAISSE,
                    props -> props.food(Foods.TROPICAL_BOUILLABAISSE, Consumables.TROPICAL_BOUILLABAISSE));
    public static final DeferredItem<BlockItem> TURKEY_DINNER =
            registerFood("turkey_dinner", ModBlocks.TURKEY_DINNER,
                    props -> props.food(Foods.TURKEY_DINNER, Consumables.TURKEY_DINNER));
    public static final DeferredItem<BlockItem> VEG_STINGER =
            registerFood("veg_stinger", ModBlocks.VEG_STINGER,
                    props -> props.food(Foods.VEG_STINGER, Consumables.VEG_STINGER));
    public static final DeferredItem<BlockItem> VOLT_GOAT_JELLY =
            registerFood("volt_goat_jelly", ModBlocks.VOLT_GOAT_JELLY,
                    props -> props.food(Foods.VOLT_GOAT_JELLY, Consumables.VOLT_GOAT_JELLY)
                            .rarity(Rarity.UNCOMMON));
    public static final DeferredItem<BlockItem> WATERMELON_ICLE =
            registerFood("watermelon_icle", ModBlocks.WATERMELON_ICLE,
                    props -> props.food(Foods.WATERMELON_ICLE, Consumables.WATERMELON_ICLE));
    public static final DeferredItem<BlockItem> WET_GOOP =
            registerFood("wet_goop", ModBlocks.WET_GOOP,
                    props -> props.food(Foods.WET_GOOP, Consumables.WET_GOOP)
                            .component(ModDataComponents.ITEM_TOOLTIPS,
                                    ItemTooltips.EMPTY.withLineAdded(
                                            I18nUtil.tooltip("wet_goop").withStyle(ChatFormatting.DARK_AQUA)
                                    )));

    public static final DeferredItem<SpawnEggItem> VOLT_GOAT_SPAWN_EGG =
            registerItem("volt_goat_spawn_egg", SpawnEggItem::new,
                    props -> props.spawnEgg(ModEntities.VOLT_GOAT.get()));

    private static <B extends Block> DeferredItem<BlockItem> registerFood(
            String name,
            Supplier<? extends B> block,
            UnaryOperator<Item.Properties> properties) {
        return registerBlock(name, block, CrockPotFoodBlockItem::new, properties);
    }

    private static DeferredItem<Item> registerFood(String name, UnaryOperator<Item.Properties> properties) {
        return registerItem(name, CrockPotFoodItem::new, properties);
    }

    private static <I extends BlockItem, B extends Block> DeferredItem<I> registerBlock(
            String name,
            Supplier<? extends B> block,
            BiFunction<B, Item.Properties, ? extends I> factory) {
        return registerBlock(name, block, factory, UnaryOperator.identity());
    }

    private static <I extends BlockItem> DeferredItem<I> registerBlock(
            String name,
            Function<Item.Properties, ? extends I> factory) {
        return registerBlock(name, factory, UnaryOperator.identity());
    }

    private static <I extends BlockItem, B extends Block> DeferredItem<I> registerBlock(
            String name,
            Supplier<? extends B> block,
            BiFunction<B, Item.Properties, ? extends I> factory,
            UnaryOperator<Item.Properties> properties) {
        return registerBlock(name, props -> factory.apply(block.get(), props), properties);
    }

    private static <I extends BlockItem> DeferredItem<I> registerBlock(
            String name,
            Function<Item.Properties, ? extends I> factory,
            UnaryOperator<Item.Properties> properties) {
        return registerItem(name, factory, props -> properties.apply(props.useBlockDescriptionPrefix()));
    }

    private static <B extends Block> DeferredItem<BlockItem> registerItem(String name, Supplier<? extends B> block) {
        return registerItem(name, props -> new BlockItem(block.get(), props));
    }

    private static <B extends Block> DeferredItem<BlockItem> registerItem(
            String name,
            Supplier<? extends B> block,
            UnaryOperator<Item.Properties> properties) {
        return registerItem(name, props -> new BlockItem(block.get(), props), properties);
    }

    private static <I extends Item> DeferredItem<I> registerItem(
            String name,
            Function<Item.Properties, ? extends I> factory) {
        return ITEMS.registerItem(name, factory);
    }

    private static <I extends Item> DeferredItem<I> registerItem(
            String name,
            Function<Item.Properties, ? extends I> factory,
            UnaryOperator<Item.Properties> properties) {
        return ITEMS.registerItem(name, factory, properties);
    }
}
