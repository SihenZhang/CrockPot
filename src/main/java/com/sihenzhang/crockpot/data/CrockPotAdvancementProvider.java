package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.advancement.CrockPotCriteriaTriggers;
import com.sihenzhang.crockpot.advancement.EatFoodTrigger;
import com.sihenzhang.crockpot.advancement.PiglinBarteringTrigger;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CrockPotAdvancementProvider extends AdvancementProvider {
    public CrockPotAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, existingFileHelper, List.of(new GenImpl()));
    }

    public static final class GenImpl implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper fileHelper) {
            var root = Advancement.Builder.advancement()
                    .display(CrockPotItems.CROCK_POT.get(), getTranslatableAdvancementTitle("root"), getTranslatableAdvancementDescription("root"), RLUtils.createRL("textures/gui/advancements/background.png"), AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(CrockPotItems.CROCK_POT.get()), has(CrockPotItems.CROCK_POT.get()))
                    .save(consumer, getSimpleAdvancementName("root"));
            Advancement.Builder.advancement().parent(root)
                    .display(CrockPotItems.CANDY.get(), getTranslatableAdvancementTitle("candy"), getTranslatableAdvancementDescription("candy"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(CrockPotItems.CANDY.get()), use(CrockPotItems.CANDY.get()))
                    .save(consumer, getSimpleAdvancementName("candy"));
            Advancement.Builder.advancement().parent(root)
                    .display(CrockPotItems.MEAT_BALLS.get(), getTranslatableAdvancementTitle("meat_balls"), getTranslatableAdvancementDescription("meat_balls"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(CrockPotItems.MEAT_BALLS.get()), eat(CrockPotItems.MEAT_BALLS.get(), MinMaxBounds.Ints.atLeast(40)))
                    .save(consumer, getSimpleAdvancementName("meat_balls"));
            Advancement.Builder.advancement().parent(root)
                    .display(CrockPotItems.MILK_BOTTLE.get(), getTranslatableAdvancementTitle("milk_bottle"), getTranslatableAdvancementDescription("milk_bottle"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(CrockPotItems.MILK_BOTTLE.get()), has(CrockPotItems.MILK_BOTTLE.get()))
                    .save(consumer, getSimpleAdvancementName("milk_bottle"));
            Advancement.Builder.advancement().parent(root)
                    .display(CrockPotItems.SYRUP.get(), getTranslatableAdvancementTitle("syrup"), getTranslatableAdvancementDescription("syrup"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(CrockPotItems.SYRUP.get()), has(CrockPotItems.SYRUP.get()))
                    .save(consumer, getSimpleAdvancementName("syrup"));
            Advancement.Builder.advancement().parent(root)
                    .display(CrockPotItems.WET_GOOP.get(), getTranslatableAdvancementTitle("wet_goop"), getTranslatableAdvancementDescription("wet_goop"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(CrockPotItems.WET_GOOP.get()), has(CrockPotItems.WET_GOOP.get()))
                    .save(consumer, getSimpleAdvancementName("wet_goop"));
            var advancedPot = Advancement.Builder.advancement().parent(root)
                    .display(CrockPotItems.PORTABLE_CROCK_POT.get(), getTranslatableAdvancementTitle("upgrade_pot"), getTranslatableAdvancementDescription("upgrade_pot"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(CrockPotItems.PORTABLE_CROCK_POT.get()), has(CrockPotItems.PORTABLE_CROCK_POT.get()))
                    .save(consumer, getSimpleAdvancementName("upgrade_pot"));
            Advancement.Builder.advancement().parent(advancedPot)
                    .display(CrockPotItems.AVAJ.get(), getTranslatableAdvancementTitle("avaj"), getTranslatableAdvancementDescription("avaj"), null, AdvancementType.CHALLENGE, true, true, true)
                    .addCriterion(getItemName(CrockPotItems.AVAJ.get()), has(CrockPotItems.AVAJ.get()))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(consumer, getSimpleAdvancementName("avaj"));
            var adultPiglin = ContextAwarePredicate.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false))).build());
            var piglinBartering = Advancement.Builder.advancement().parent(advancedPot)
                    .display(CrockPotItems.NETHEROSIA.get(), getTranslatableAdvancementTitle("piglin_bartering"), getTranslatableAdvancementDescription("piglin_bartering"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("piglin_bartering", CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.createCriterion(new PickedUpItemTrigger.TriggerInstance(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(CrockPotItems.NETHEROSIA.get()).build()), Optional.of(adultPiglin))))
                    .addCriterion("piglin_bartering_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(Optional.empty(), ItemPredicate.Builder.item().of(CrockPotItems.NETHEROSIA.get()), Optional.of(adultPiglin)))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(consumer, getSimpleAdvancementName("piglin_bartering"));
            Advancement.Builder.advancement().parent(piglinBartering)
                    .display(Items.WITHER_SKELETON_SKULL, getTranslatableAdvancementTitle("wither_skeleton_skull"), getTranslatableAdvancementDescription("wither_skeleton_skull"), null, AdvancementType.CHALLENGE, true, true, true)
                    .addCriterion(getItemName(Items.WITHER_SKELETON_SKULL), CrockPotCriteriaTriggers.PIGLIN_BARTERING_TRIGGER.get().createCriterion(new PiglinBarteringTrigger.Instance(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(Items.WITHER_SKELETON_SKULL).build()))))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(consumer, getSimpleAdvancementName("wither_skeleton_skull"));

            var gnawWillBeHappyBuilder = Advancement.Builder.advancement().parent(advancedPot)
                    .display(CrockPotItems.GNAWS_COIN.get(), getTranslatableAdvancementTitle("gnaw_will_be_happy"), getTranslatableAdvancementDescription("gnaw_will_be_happy"), null, AdvancementType.CHALLENGE, true, true, false)
                    .rewards(AdvancementRewards.Builder.experience(200).addLootTable(ResourceKey.create(Registries.LOOT_TABLE, RLUtils.createRL("gnaws_coin"))));
            CrockPotItems.FOODS_WITHOUT_AVAJ.get().forEach(food -> gnawWillBeHappyBuilder.addCriterion(getItemName(food), use(food)));
            gnawWillBeHappyBuilder.save(consumer, getSimpleAdvancementName("gnaw_will_be_happy"));
        }
    }

    protected static Component getTranslatableAdvancementTitle(String name) {
        return I18nUtils.createComponent("advancement", name);
    }

    protected static Component getTranslatableAdvancementDescription(String name) {
        return I18nUtils.createComponent("advancement", name + ".desc");
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike pItem, MinMaxBounds.Ints pCount) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItem).withCount(pCount).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike pItemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> pTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... pPredicates) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(pPredicates);
    }

    protected static Criterion<ConsumeItemTrigger.TriggerInstance> use(ItemLike pItemLike) {
        return consumeTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    protected static Criterion<ConsumeItemTrigger.TriggerInstance> use(TagKey<Item> pTag) {
        return consumeTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    protected static Criterion<ConsumeItemTrigger.TriggerInstance> consumeTrigger(ItemPredicate pPredicate) {
        return CriteriaTriggers.CONSUME_ITEM.createCriterion(new ConsumeItemTrigger.TriggerInstance(Optional.empty(), Optional.of(pPredicate)));
    }

    protected static Criterion<EatFoodTrigger.Instance> eat(ItemLike pItemLike, MinMaxBounds.Ints pCount) {
        return eatFoodTrigger(ItemPredicate.Builder.item().of(pItemLike).build(), pCount);
    }

    protected static Criterion<EatFoodTrigger.Instance> eatFoodTrigger(ItemPredicate pPredicate, MinMaxBounds.Ints pCount) {
        return CrockPotCriteriaTriggers.EAT_FOOD_TRIGGER.get().createCriterion(new EatFoodTrigger.Instance(Optional.empty(), Optional.of(pPredicate), pCount));
    }

    protected static String getItemName(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem()).getPath();
    }

    protected static String getSimpleAdvancementName(String name) {
        return CrockPot.MOD_ID + ":" + name;
    }

    // AdvancementProvider overrides getName() and denotes it as final, so we cannot use our own name.
    // Previously, we override getName() and return "CrockPot Advancements".
}
