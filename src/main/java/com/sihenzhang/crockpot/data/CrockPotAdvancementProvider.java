package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.advancement.EatFoodTrigger;
import com.sihenzhang.crockpot.advancement.PiglinBarteringTrigger;
import com.sihenzhang.crockpot.item.ModItems;
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
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            var root = Advancement.Builder.advancement()
                    .display(ModItems.CROCK_POT.get(), getTranslatableAdvancementTitle("root"), getTranslatableAdvancementDescription("root"), RLUtils.mod("textures/gui/advancements/background.png"), AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.CROCK_POT.get()), has(ModItems.CROCK_POT.get()))
                    .save(saver, getSimpleAdvancementName("root"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.CANDY.get(), getTranslatableAdvancementTitle("candy"), getTranslatableAdvancementDescription("candy"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.CANDY.get()), use(ModItems.CANDY.get()))
                    .save(saver, getSimpleAdvancementName("candy"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.MEAT_BALLS.get(), getTranslatableAdvancementTitle("meat_balls"), getTranslatableAdvancementDescription("meat_balls"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.MEAT_BALLS.get()), eat(ModItems.MEAT_BALLS.get(), MinMaxBounds.Ints.atLeast(40)))
                    .save(saver, getSimpleAdvancementName("meat_balls"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.MILK_BOTTLE.get(), getTranslatableAdvancementTitle("milk_bottle"), getTranslatableAdvancementDescription("milk_bottle"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.MILK_BOTTLE.get()), has(ModItems.MILK_BOTTLE.get()))
                    .save(saver, getSimpleAdvancementName("milk_bottle"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.SYRUP.get(), getTranslatableAdvancementTitle("syrup"), getTranslatableAdvancementDescription("syrup"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.SYRUP.get()), has(ModItems.SYRUP.get()))
                    .save(saver, getSimpleAdvancementName("syrup"));
            Advancement.Builder.advancement().parent(root)
                    .display(ModItems.WET_GOOP.get(), getTranslatableAdvancementTitle("wet_goop"), getTranslatableAdvancementDescription("wet_goop"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.WET_GOOP.get()), has(ModItems.WET_GOOP.get()))
                    .save(saver, getSimpleAdvancementName("wet_goop"));
            var advancedPot = Advancement.Builder.advancement().parent(root)
                    .display(ModItems.PORTABLE_CROCK_POT.get(), getTranslatableAdvancementTitle("upgrade_pot"), getTranslatableAdvancementDescription("upgrade_pot"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion(getItemName(ModItems.PORTABLE_CROCK_POT.get()), has(ModItems.PORTABLE_CROCK_POT.get()))
                    .save(saver, getSimpleAdvancementName("upgrade_pot"));
            Advancement.Builder.advancement().parent(advancedPot)
                    .display(ModItems.AVAJ.get(), getTranslatableAdvancementTitle("avaj"), getTranslatableAdvancementDescription("avaj"), null, AdvancementType.CHALLENGE, true, true, true)
                    .addCriterion(getItemName(ModItems.AVAJ.get()), has(ModItems.AVAJ.get()))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(saver, getSimpleAdvancementName("avaj"));
            var adultPiglin = ContextAwarePredicate.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false))).build());
            var piglinBartering = Advancement.Builder.advancement().parent(advancedPot)
                    .display(ModItems.NETHEROSIA.get(), getTranslatableAdvancementTitle("piglin_bartering"), getTranslatableAdvancementDescription("piglin_bartering"), null, AdvancementType.TASK, true, true, false)
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .addCriterion("piglin_bartering", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(ModItems.NETHEROSIA.get()).build()), Optional.of(adultPiglin)))
                    .addCriterion("piglin_bartering_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(ModItems.NETHEROSIA.get()), Optional.of(adultPiglin)))
                    .save(saver, getSimpleAdvancementName("piglin_bartering"));
            Advancement.Builder.advancement().parent(piglinBartering)
                    .display(Items.WITHER_SKELETON_SKULL, getTranslatableAdvancementTitle("wither_skeleton_skull"), getTranslatableAdvancementDescription("wither_skeleton_skull"), null, AdvancementType.CHALLENGE, true, true, true)
                    .addCriterion(getItemName(Items.WITHER_SKELETON_SKULL), piglinBarter(Items.WITHER_SKELETON_SKULL))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(saver, getSimpleAdvancementName("wither_skeleton_skull"));

            var gnawWillBeHappyBuilder = Advancement.Builder.advancement().parent(advancedPot)
                    .display(ModItems.GNAWS_COIN.get(), getTranslatableAdvancementTitle("gnaw_will_be_happy"), getTranslatableAdvancementDescription("gnaw_will_be_happy"), null, AdvancementType.CHALLENGE, true, true, false)
                    .rewards(AdvancementRewards.Builder.experience(200).addLootTable(ResourceKey.create(Registries.LOOT_TABLE, RLUtils.mod("gnaws_coin"))));
            ModItems.FOODS_WITHOUT_AVAJ.get().forEach(food -> gnawWillBeHappyBuilder.addCriterion(getItemName(food), use(food)));
            gnawWillBeHappyBuilder.save(saver, getSimpleAdvancementName("gnaw_will_be_happy"));
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
        return consumeTrigger(ItemPredicate.Builder.item().of(pItemLike));
    }

    protected static Criterion<ConsumeItemTrigger.TriggerInstance> use(TagKey<Item> pTag) {
        return consumeTrigger(ItemPredicate.Builder.item().of(pTag));
    }

    protected static Criterion<ConsumeItemTrigger.TriggerInstance> consumeTrigger(ItemPredicate.Builder pPredicate) {
        return ConsumeItemTrigger.TriggerInstance.usedItem(pPredicate);
    }

    protected static Criterion<EatFoodTrigger.TriggerInstance> eat(ItemLike pItemLike, MinMaxBounds.Ints pCount) {
        return eatFoodTrigger(ItemPredicate.Builder.item().of(pItemLike), pCount);
    }

    protected static Criterion<EatFoodTrigger.TriggerInstance> eatFoodTrigger(ItemPredicate.Builder pPredicate, MinMaxBounds.Ints pCount) {
        return EatFoodTrigger.TriggerInstance.eatenItem(pPredicate, pCount);
    }

    protected static Criterion<PiglinBarteringTrigger.TriggerInstance> piglinBarter(ItemLike pItemLike) {
        return piglinBarteringTrigger(ItemPredicate.Builder.item().of(pItemLike));
    }

    protected static Criterion<PiglinBarteringTrigger.TriggerInstance> piglinBarteringTrigger(ItemPredicate.Builder pPredicate) {
        return PiglinBarteringTrigger.TriggerInstance.itemPiglinBartered(pPredicate);
    }

    protected static String getItemName(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem()).getPath();
    }

    protected static String getSimpleAdvancementName(String name) {
        return CrockPot.MOD_ID + ":" + name;
    }
}
