package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.advancement.EatFoodTrigger;
import com.sihenzhang.crockpot.advancement.ModCriterionTriggers;
import com.sihenzhang.crockpot.advancement.PiglinBarteringTrigger;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.PickedUpItemTrigger;
import net.minecraft.advancements.criterion.PlayerInteractTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new GenImpl()));
    }

    public static final class GenImpl implements AdvancementSubProvider {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
            var items = registries.lookupOrThrow(Registries.ITEM);
            var entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);

            var root = Advancement.Builder.advancement()
                    .display(ModItems.CROCK_POT.get(), getTranslatableAdvancementTitle("root"), getTranslatableAdvancementDescription("root"), IdUtil.mod("textures/gui/advancements/background.png"), AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.CROCK_POT.get()), has(items, ModItems.CROCK_POT.get()))
                    .save(output, getSimpleAdvancementName("root"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.CANDY.get(), getTranslatableAdvancementTitle("candy"), getTranslatableAdvancementDescription("candy"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.CANDY.get()), use(items, ModItems.CANDY.get()))
                    .save(output, getSimpleAdvancementName("candy"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.MEAT_BALLS.get(), getTranslatableAdvancementTitle("meat_balls"), getTranslatableAdvancementDescription("meat_balls"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.MEAT_BALLS.get()), eat(items, ModItems.MEAT_BALLS.get(), MinMaxBounds.Ints.atLeast(40)))
                    .save(output, getSimpleAdvancementName("meat_balls"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.MILK_BOTTLE.get(), getTranslatableAdvancementTitle("milk_bottle"), getTranslatableAdvancementDescription("milk_bottle"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.MILK_BOTTLE.get()), has(items, ModItems.MILK_BOTTLE.get()))
                    .save(output, getSimpleAdvancementName("milk_bottle"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.SYRUP.get(), getTranslatableAdvancementTitle("syrup"), getTranslatableAdvancementDescription("syrup"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.SYRUP.get()), has(items, ModItems.SYRUP.get()))
                    .save(output, getSimpleAdvancementName("syrup"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.WET_GOOP.get(), getTranslatableAdvancementTitle("wet_goop"), getTranslatableAdvancementDescription("wet_goop"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.WET_GOOP.get()), has(items, ModItems.WET_GOOP.get()))
                    .save(output, getSimpleAdvancementName("wet_goop"));
            var advancedPot = Advancement.Builder.advancement().parent(root)
                    .display(ModItems.PORTABLE_CROCK_POT.get(), getTranslatableAdvancementTitle("upgrade_pot"), getTranslatableAdvancementDescription("upgrade_pot"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.PORTABLE_CROCK_POT.get()), has(items, ModItems.PORTABLE_CROCK_POT.get()))
                    .save(output, getSimpleAdvancementName("upgrade_pot"));
            Advancement.Builder.advancement().parent(advancedPot)
                    .display(ModItems.AVAJ.get(), getTranslatableAdvancementTitle("avaj"), getTranslatableAdvancementDescription("avaj"), null, AdvancementType.CHALLENGE, true, true, true)
                    .addCriterion(getItemName(ModItems.AVAJ.get()), has(items, ModItems.AVAJ.get()))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(output, getSimpleAdvancementName("avaj"));

            var adultPiglin = ContextAwarePredicate.create(LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.THIS,
                    EntityPredicate.Builder.entity().of(entityTypes, EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false))
            ).build());
            var netherosia = ItemPredicate.Builder.item().of(items, ModItems.NETHEROSIA.get());
            var piglinBartering = Advancement.Builder.advancement().parent(advancedPot)
                    .display(ModItems.NETHEROSIA.get(), getTranslatableAdvancementTitle("piglin_bartering"), getTranslatableAdvancementDescription("piglin_bartering"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("piglin_bartering", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(ContextAwarePredicate.create(), Optional.of(netherosia.build()), Optional.of(adultPiglin)))
                    .addCriterion("piglin_bartering_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(items, ModItems.NETHEROSIA.get()), Optional.of(adultPiglin)))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(output, getSimpleAdvancementName("piglin_bartering"));
            Advancement.Builder.advancement().parent(piglinBartering)
                    .display(Items.WITHER_SKELETON_SKULL, getTranslatableAdvancementTitle("wither_skeleton_skull"), getTranslatableAdvancementDescription("wither_skeleton_skull"), null, AdvancementType.CHALLENGE, true, true, true)
                    .addCriterion(getItemName(Items.WITHER_SKELETON_SKULL), piglinBartering(items, Items.WITHER_SKELETON_SKULL))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(output, getSimpleAdvancementName("wither_skeleton_skull"));

            var gnawWillBeHappyBuilder = Advancement.Builder.advancement().parent(advancedPot)
                    .display(ModItems.GNAWS_COIN.get(), getTranslatableAdvancementTitle("gnaw_will_be_happy"), getTranslatableAdvancementDescription("gnaw_will_be_happy"), null, AdvancementType.CHALLENGE, true, true, false)
                    .rewards(AdvancementRewards.Builder.experience(200).addLootTable(ResourceKey.create(Registries.LOOT_TABLE, IdUtil.mod("gnaws_coin"))));
            ModBlocks.FOODS.get().stream().map(ItemLike::asItem).filter(food -> food != ModItems.AVAJ.get()).forEach(food -> gnawWillBeHappyBuilder.addCriterion(getItemName(food), use(items, food)));
            gnawWillBeHappyBuilder.save(output, getSimpleAdvancementName("gnaw_will_be_happy"));
        }
    }

    protected static Component getTranslatableAdvancementTitle(String name) {
        return I18nUtil.of("advancement", name);
    }

    protected static Component getTranslatableAdvancementDescription(String name) {
        return I18nUtil.of("advancement", name + ".desc");
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(HolderGetter<Item> items, ItemLike item, MinMaxBounds.Ints count) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, item).withCount(count).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(HolderGetter<Item> items, ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, item).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(HolderGetter<Item> items, TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, tag).build());
    }

    protected static Criterion<ConsumeItemTrigger.TriggerInstance> use(HolderGetter<Item> items, ItemLike item) {
        return ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(items, item));
    }

    protected static Criterion<ConsumeItemTrigger.TriggerInstance> use(HolderGetter<Item> items, TagKey<Item> tag) {
        return ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(items, tag));
    }

    protected static Criterion<EatFoodTrigger.TriggerInstance> eat(HolderGetter<Item> items, ItemLike item, MinMaxBounds.Ints count) {
        return EatFoodTrigger.TriggerInstance.eatenItem(ItemPredicate.Builder.item().of(items, item), count);
    }

    protected static Criterion<PiglinBarteringTrigger.TriggerInstance> piglinBartering(HolderGetter<Item> items, ItemLike item) {
        var predicate = ItemPredicate.Builder.item().of(items, item).build();
        return ModCriterionTriggers.PIGLIN_BARTERING_TRIGGER.get().createCriterion(new PiglinBarteringTrigger.TriggerInstance(Optional.empty(), Optional.of(predicate)));
    }

    protected static String getItemName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    protected static String getSimpleAdvancementName(String name) {
        return CrockPot.MOD_ID + ":" + name;
    }
}
