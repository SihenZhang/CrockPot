package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.advancement.EatFoodTrigger;
import com.sihenzhang.crockpot.advancement.PiglinBarteringTrigger;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class CrockPotAdvancementProvider extends AdvancementProvider {
    public CrockPotAdvancementProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, existingFileHelper);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        var root = Advancement.Builder.advancement()
                .display(CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get(), getTranslatableAdvancementTitle("root"), getTranslatableAdvancementDescription("root"), RLUtils.createRL("textures/gui/advancements/background.png"), FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get()), has(CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get()))
                .save(consumer, getSimpleAdvancementName("root"));
        Advancement.Builder.advancement().parent(root)
                .display(CrockPotRegistry.CANDY.get(), getTranslatableAdvancementTitle("candy"), getTranslatableAdvancementDescription("candy"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.CANDY.get()), use(CrockPotRegistry.CANDY.get()))
                .save(consumer, getSimpleAdvancementName("candy"));
        Advancement.Builder.advancement().parent(root)
                .display(CrockPotRegistry.MEAT_BALLS.get(), getTranslatableAdvancementTitle("meat_balls"), getTranslatableAdvancementDescription("meat_balls"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.MEAT_BALLS.get()), eat(CrockPotRegistry.MEAT_BALLS.get(), MinMaxBounds.Ints.atLeast(40)))
                .save(consumer, getSimpleAdvancementName("meat_balls"));
        Advancement.Builder.advancement().parent(root)
                .display(CrockPotRegistry.MILK_BOTTLE.get(), getTranslatableAdvancementTitle("milk_bottle"), getTranslatableAdvancementDescription("milk_bottle"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.MILK_BOTTLE.get()), has(CrockPotRegistry.MILK_BOTTLE.get()))
                .save(consumer, getSimpleAdvancementName("milk_bottle"));
        Advancement.Builder.advancement().parent(root)
                .display(CrockPotRegistry.SYRUP.get(), getTranslatableAdvancementTitle("syrup"), getTranslatableAdvancementDescription("syrup"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.SYRUP.get()), has(CrockPotRegistry.SYRUP.get()))
                .save(consumer, getSimpleAdvancementName("syrup"));
        Advancement.Builder.advancement().parent(root)
                .display(CrockPotRegistry.WET_GOOP.get(), getTranslatableAdvancementTitle("wet_goop"), getTranslatableAdvancementDescription("wet_goop"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.WET_GOOP.get()), has(CrockPotRegistry.WET_GOOP.get()))
                .save(consumer, getSimpleAdvancementName("wet_goop"));
        var advancedPot = Advancement.Builder.advancement().parent(root)
                .display(CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK.get(), getTranslatableAdvancementTitle("upgrade_pot"), getTranslatableAdvancementDescription("upgrade_pot"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK_ITEM.get()), has(CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK_ITEM.get()))
                .save(consumer, getSimpleAdvancementName("upgrade_pot"));
        var ultimatePot = Advancement.Builder.advancement().parent(advancedPot)
                .display(CrockPotRegistry.ULTIMATE_CROCK_POT_BLOCK.get(), getTranslatableAdvancementTitle("ultimate_pot"), getTranslatableAdvancementDescription("ultimate_pot"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(CrockPotRegistry.ULTIMATE_CROCK_POT_BLOCK_ITEM.get()), has(CrockPotRegistry.ULTIMATE_CROCK_POT_BLOCK_ITEM.get()))
                .save(consumer, getSimpleAdvancementName("ultimate_pot"));
        Advancement.Builder.advancement().parent(ultimatePot)
                .display(CrockPotRegistry.AVAJ.get(), getTranslatableAdvancementTitle("avaj"), getTranslatableAdvancementDescription("avaj"), null, FrameType.CHALLENGE, true, true, true)
                .addCriterion(getItemName(CrockPotRegistry.AVAJ.get()), has(CrockPotRegistry.AVAJ.get()))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(consumer, getSimpleAdvancementName("avaj"));
        var adultPiglin = EntityPredicate.Composite.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(EntityType.PIGLIN).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false).build())).build());
        var piglinBartering = Advancement.Builder.advancement().parent(ultimatePot)
                .display(CrockPotRegistry.NETHEROSIA.get(), getTranslatableAdvancementTitle("piglin_bartering"), getTranslatableAdvancementDescription("piglin_bartering"), null, FrameType.TASK, true, true, false)
                .addCriterion("piglin_bartering", ItemPickedUpByEntityTrigger.TriggerInstance.itemPickedUpByEntity(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(CrockPotRegistry.NETHEROSIA.get()), adultPiglin))
                .addCriterion("piglin_bartering_directly", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(CrockPotRegistry.NETHEROSIA.get()), adultPiglin))
                .requirements(RequirementsStrategy.OR)
                .save(consumer, getSimpleAdvancementName("piglin_bartering"));
        Advancement.Builder.advancement().parent(piglinBartering)
                .display(Items.NETHERITE_SCRAP, getTranslatableAdvancementTitle("netherite_scrap"), getTranslatableAdvancementDescription("netherite_scrap"), null, FrameType.TASK, true, true, false)
                .addCriterion(getItemName(Items.NETHERITE_SCRAP), new PiglinBarteringTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(Items.NETHERITE_SCRAP).build()))
                .save(consumer, getSimpleAdvancementName("netherite_scrap"));
        Advancement.Builder.advancement().parent(piglinBartering)
                .display(Items.WITHER_SKELETON_SKULL, getTranslatableAdvancementTitle("wither_skeleton_skull"), getTranslatableAdvancementDescription("wither_skeleton_skull"), null, FrameType.CHALLENGE, true, true, true)
                .addCriterion(getItemName(Items.WITHER_SKELETON_SKULL), new PiglinBarteringTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(Items.WITHER_SKELETON_SKULL).build()))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(consumer, getSimpleAdvancementName("wither_skeleton_skull"));
    }

    protected static TranslatableComponent getTranslatableAdvancementTitle(String name) {
        return I18nUtils.createComponent("advancement", name);
    }

    protected static TranslatableComponent getTranslatableAdvancementDescription(String name) {
        return I18nUtils.createComponent("advancement", name + ".desc");
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike pItem, MinMaxBounds.Ints pCount) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItem).withCount(pCount).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> pTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(pPredicates);
    }

    protected static ConsumeItemTrigger.TriggerInstance use(ItemLike pItemLike) {
        return consumeTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    protected static ConsumeItemTrigger.TriggerInstance use(TagKey<Item> pTag) {
        return consumeTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    protected static ConsumeItemTrigger.TriggerInstance consumeTrigger(ItemPredicate pPredicate) {
        return ConsumeItemTrigger.TriggerInstance.usedItem(pPredicate);
    }

    protected static EatFoodTrigger.Instance eat(ItemLike pItemLike, MinMaxBounds.Ints pCount) {
        return eatFoodTrigger(ItemPredicate.Builder.item().of(pItemLike).build(), pCount);
    }

    protected static EatFoodTrigger.Instance eatFoodTrigger(ItemPredicate pPredicate, MinMaxBounds.Ints pCount) {
        return new EatFoodTrigger.Instance(EntityPredicate.Composite.ANY, pPredicate, pCount);
    }

    protected static String getItemName(ItemLike pItemLike) {
        return ForgeRegistries.ITEMS.getKey(pItemLike.asItem()).getPath();
    }

    protected static String getSimpleAdvancementName(String name) {
        return CrockPot.MOD_ID + ":" + name;
    }

    @Override
    public String getName() {
        return "CrockPot Advancements";
    }
}
