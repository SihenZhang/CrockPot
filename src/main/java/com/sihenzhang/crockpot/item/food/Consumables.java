package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.attachment.ModAttachmentTypes;
import com.sihenzhang.crockpot.core.ModDamageTypes;
import com.sihenzhang.crockpot.effect.ModEffects;
import com.sihenzhang.crockpot.item.consume_effects.CandyConsumeEffect;
import com.sihenzhang.crockpot.item.consume_effects.HealConsumeEffect;
import com.sihenzhang.crockpot.item.consume_effects.HurtConsumeEffect;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.NumberUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.world.item.component.Consumables.*;

public final class Consumables {
    private Consumables() {}

    public static final Consumable ASPARAGUS = DEFAULT_FOOD;
    public static final Consumable CORN = DEFAULT_FOOD;
    public static final Consumable POPCORN = defaultFood().consumeSeconds(ConsumeDuration.FAST.consumeSeconds).build();
    public static final Consumable EGGPLANT = DEFAULT_FOOD;
    public static final Consumable COOKED_EGGPLANT = DEFAULT_FOOD;
    public static final Consumable GARLIC = DEFAULT_FOOD;
    public static final Consumable ONION = DEFAULT_FOOD;
    public static final Consumable PEPPER = defaultFood().onConsume(new HurtConsumeEffect(ModDamageTypes.SPICY, 1.0F)).build();
    public static final Consumable TOMATO = DEFAULT_FOOD;
    public static final Consumable DRIED_FOOD = defaultFood().consumeSeconds(ConsumeDuration.FAST.consumeSeconds).build();
    public static final Consumable COOKED_EGG = DEFAULT_FOOD;
    public static final Consumable FROG_LEGS = DEFAULT_FOOD;
    public static final Consumable COOKED_FROG_LEGS = DEFAULT_FOOD;
    public static final Consumable HOGLIN_NOSE = DEFAULT_FOOD;
    public static final Consumable COOKED_HOGLIN_NOSE = DEFAULT_FOOD;
    public static final Consumable MILK_BOTTLE = DEFAULT_DRINK;
    public static final Consumable SYRUP = defaultDrink().sound(SoundEvents.HONEY_DRINK).build();
    public static final Consumable ASPARAGUS_SOUP = defaultDrink()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.WEAKNESS))
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.MINING_FATIGUE))
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.BLINDNESS))
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.BAD_OMEN))
            .build();
    public static final Consumable AVAJ = defaultDrink()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SPEED, (32 * 60 + 20) * 20, 2)))
            .build();
    public static final Consumable BACON_EGGS = defaultFood().onConsume(new HealConsumeEffect(4.0F)).build();
    public static final Consumable BONE_SOUP = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2 * 60 * 20, 1)))
            .build();
    public static final Consumable BONE_STEW = defaultFood()
            .consumeSeconds(ConsumeDuration.SUPER_SLOW.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 1)))
            .build();
    public static final Consumable BREAKFAST_SKILLET = DEFAULT_FOOD;
    public static final Consumable BUNNY_STEW = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.REGENERATION, 5 * 20),
                    new MobEffectInstance(ModEffects.WELL_FED, 2 * 60 * 20)
            )))
            .build();
    public static final Consumable CALIFORNIA_ROLL = defaultFood()
            .onConsume(new HealConsumeEffect(4.0F))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 * 20)))
            .build();
    public static final Consumable CANDY = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(CandyConsumeEffect.INSTANCE)
            .build();
    public static final Consumable CEVICHE = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.RESISTANCE, 20 * 20, 1),
                    new MobEffectInstance(MobEffects.ABSORPTION, 20 * 20, 1)
            )))
            .build();
    public static final Consumable FISH_STICKS = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 30 * 20)))
            .build();
    public static final Consumable FISH_TACOS = defaultFood().onConsume(new HealConsumeEffect(2.0F)).build();
    public static final Consumable FLOWER_SALAD = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20)))
            .onConsume(new HealConsumeEffect(4.0F))
            .onConsume(new TeleportRandomlyConsumeEffect())
            .build();
    public static final Consumable FROGGLE_BUNWICH = DEFAULT_FOOD;
    public static final Consumable FRUIT_MEDLEY = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SPEED, 3 * 60 * 20)))
            .build();
    public static final Consumable GAZPACHO = defaultDrink()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10 * 60 * 20)))
            .build();
    public static final Consumable GLOW_BERRY_MOUSSE = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.GLOWING, 10 * 20)))
            .build();
    public static final Consumable GUMMY_CAKE = defaultFood()
            .consumeSeconds(ConsumeDuration.SLOW.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SLOWNESS, 10 * 20), 0.8F))
            .build();
    public static final Consumable HONEY_HAM = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.REGENERATION, 20 * 20),
                    new MobEffectInstance(MobEffects.ABSORPTION, 60 * 20, 1)
            )))
            .onConsume(new HealConsumeEffect(6.0F))
            .build();
    public static final Consumable HONEY_NUGGETS = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.REGENERATION, 10 * 20),
                    new MobEffectInstance(MobEffects.ABSORPTION, 60 * 20)
            )))
            .onConsume(new HealConsumeEffect(4.0F))
            .build();
    public static final Consumable HOT_CHILI = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.STRENGTH, (60 + 30) * 20),
                    new MobEffectInstance(MobEffects.HASTE, (60 + 30) * 20)
            )))
            .build();
    public static final Consumable HOT_COCOA = defaultDrink()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SPEED, 8 * 60 * 20, 1)))
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.SLOWNESS))
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.MINING_FATIGUE))
            .build();
    public static final Consumable ICE_CREAM = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(ClearAllStatusEffectsConsumeEffect.INSTANCE)
            .build();
    public static final Consumable ICED_TEA = defaultDrink()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.SPEED, 10 * 60 * 20, 1),
                    new MobEffectInstance(MobEffects.JUMP_BOOST, 5 * 60 * 20, 1)
            )))
            .build();
    public static final Consumable JAMMY_PRESERVES = defaultFood().consumeSeconds(ConsumeDuration.FAST.consumeSeconds).build();
    public static final Consumable KABOBS = DEFAULT_FOOD;
    public static final Consumable MASHED_POTATOES = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE, 4 * 60 * 20)))
            .build();
    public static final Consumable MEAT_BALLS = DEFAULT_FOOD;
    public static final Consumable MONSTER_LASAGNA = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.HUNGER, 15 * 20),
                    new MobEffectInstance(MobEffects.POISON, 2 * 20)
            )))
            .onConsume(new HurtConsumeEffect(ModDamageTypes.MONSTER_FOOD, 6.0F))
            .build();
    public static final Consumable MONSTER_TARTARE = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH, 2 * 60 * 20, 1)))
            .build();
    public static final Consumable MOQUECA = defaultFood()
            .consumeSeconds(ConsumeDuration.SLOW.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, (60 + 30) * 20, 2)))
            .onConsume(new HealConsumeEffect(6.0F))
            .build();
    public static final Consumable MUSHY_CAKE = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.WITHER_RESISTANCE, 60 * 20)))
            .build();
    public static final Consumable PEPPER_POPPER = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.STRENGTH, 60 * 20, 1)))
            .build();
    public static final Consumable PEROGIES = defaultFood().onConsume(new HealConsumeEffect(6.0F)).build();
    public static final Consumable PLAIN_OMELETTE = DEFAULT_FOOD;
    public static final Consumable POTATO_SOUFFLE = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE, (60 + 30) * 20, 1)))
            .build();
    public static final Consumable POTATO_TORNADO = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.HUNGER))
            .build();
    public static final Consumable POW_CAKE = defaultFood().onConsume(new HurtConsumeEffect(ModDamageTypes.POW_CAKE, 1.0F)).build();
    public static final Consumable PUMPKIN_COOKIE = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.HUNGER))
            .build();
    public static final Consumable RATATOUILLE = defaultFood().consumeSeconds(ConsumeDuration.FAST.consumeSeconds).build();
    public static final Consumable SALMON_SUSHI = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new HealConsumeEffect(1.0F))
            .build();
    public static final Consumable SALSA = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HASTE, 6 * 60 * 20)))
            .build();
    public static final Consumable SCOTCH_EGG = DEFAULT_FOOD;
    public static final Consumable SEAFOOD_GUMBO = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 2 * 60 * 20)))
            .build();
    public static final Consumable SNAKE_BONE_SOUP = defaultFood().onConsume(new HealConsumeEffect(6.0F)).build();
    public static final Consumable STEAMED_HAM_SANDWICH = defaultFood().onConsume(new HealConsumeEffect(4.0F)).build();
    public static final Consumable STUFFED_EGGPLANT = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new HealConsumeEffect(2.0F))
            .build();
    public static final Consumable SURF_N_TURF = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 30 * 20, 1)))
            .onConsume(new HealConsumeEffect(8.0F))
            .build();
    public static final Consumable TAFFY = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.LUCK, 8 * 60 * 20)))
            .onConsume(new HurtConsumeEffect(ModDamageTypes.TAFFY, 1.0F))
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.POISON))
            .build();
    public static final Consumable TEA = defaultDrink()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.SPEED, 10 * 60 * 20, 1),
                    new MobEffectInstance(MobEffects.HASTE, 5 * 60 * 20, 1)
            )))
            .build();
    public static final Consumable TROPICAL_BOUILLABAISSE = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.OCEAN_AFFINITY, (2 * 60 + 30) * 20)))
            .build();
    public static final Consumable TURKEY_DINNER = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 3 * 60 * 20)))
            .build();
    public static final Consumable VEG_STINGER = defaultDrink()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 10 * 60 * 20)))
            .build();
    public static final Consumable VOLT_GOAT_JELLY = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModEffects.CHARGE, 6 * 60 * 20)))
            .build();
    public static final Consumable WATERMELON_ICLE = defaultFood()
            .consumeSeconds(ConsumeDuration.FAST.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.SPEED, 3 * 60 * 20),
                    new MobEffectInstance(MobEffects.JUMP_BOOST, 3 * 60 * 20)
            )))
            .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.SLOWNESS))
            .build();
    public static final Consumable WET_GOOP = defaultFood()
            .consumeSeconds(ConsumeDuration.SUPER_SLOW.consumeSeconds)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.NAUSEA, 10 * 20)))
            .build();

    /**
     * Adds the learned consumable-effect tooltip lines for a Crock Pot food or drink.
     *
     * <p>The tooltip is intentionally player-dependent: if the tooltip context has no player,
     * or the stack has no {@link DataComponents#CONSUMABLE} component, nothing is added. If the
     * current player's {@link ModAttachmentTypes#FOOD_COUNTER} has not recorded this item, only the
     * "not eaten yet" hint is shown and all real effect details stay hidden.
     *
     * @param itemStack the stack whose {@link DataComponents#CONSUMABLE} effects should be described
     * @param context tooltip context providing the viewing player and tick rate used for duration formatting
     * @param consumer receives each tooltip line in display order
     * @param flag vanilla tooltip flag forwarded to nested {@link TooltipProvider} effects
     */
    public static void addToTooltip(ItemStack itemStack, Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag) {
        var player = context.player();
        var consumable = itemStack.get(DataComponents.CONSUMABLE);
        if (player == null || consumable == null) {
            return;
        }
        var foodCounter = player.getData(ModAttachmentTypes.FOOD_COUNTER);
        if (!foodCounter.hasEaten(itemStack.getItem())) {
            consumer.accept(I18nUtil.tooltip("effect.not_eat").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            return;
        }
        var consumeEffects = consumable.onConsumeEffects();
        var isDrink = consumable.animation() == ItemUseAnimation.DRINK;
        if (consumeEffects.isEmpty()) {
            consumer.accept(I18nUtil.tooltip("effect.no_effect").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }
        var statusEffectTooltips = new ArrayList<Component>();
        var consumeEffectTooltips = new ArrayList<Component>();
        for (var consumeEffect : consumeEffects) {
            switch (consumeEffect) {
                case ApplyStatusEffectsConsumeEffect applyEffect -> {
                    var probability = applyEffect.probability();
                    applyEffect.effects().forEach(effect -> {
                        var mobEffect = effect.getEffect();
                        var potionDescription = PotionContents.getPotionDescription(mobEffect, effect.getAmplifier());
                        if (!effect.endsWithin(20)) {
                            potionDescription = Component.translatable("potion.withDuration", potionDescription, MobEffectUtil.formatDuration(effect, 1.0F, context.tickRate()));
                        }
                        if (probability < 1.0F) {
                            potionDescription = I18nUtil.tooltip("effect.with_probability", NumberUtil.formatPercent(probability, 2), potionDescription);
                        }
                        statusEffectTooltips.add(potionDescription.withStyle(mobEffect.value().getCategory().getTooltipFormatting()));
                    });
                }
                case TooltipProvider tooltipProvider ->
                        tooltipProvider.addToTooltip(context, consumeEffectTooltips::add, flag, itemStack.getComponents());
                case RemoveStatusEffectsConsumeEffect removeEffect ->
                        removeEffect.effects().forEach(mobEffect -> consumeEffectTooltips.add(I18nUtil.tooltip("effect.remove", Component.translatable(mobEffect.value().getDescriptionId())).withStyle(ChatFormatting.GOLD)));
                case ClearAllStatusEffectsConsumeEffect ignored ->
                        consumeEffectTooltips.add(I18nUtil.tooltip("effect.clear_all").withStyle(ChatFormatting.AQUA));
                case TeleportRandomlyConsumeEffect ignored ->
                        consumeEffectTooltips.add(I18nUtil.tooltip("effect.random_teleport").withStyle(ChatFormatting.LIGHT_PURPLE));
                default -> {}
            }
        }
        statusEffectTooltips.forEach(consumer);
        if (!consumeEffectTooltips.isEmpty()) {
            consumer.accept(Component.empty());
            consumer.accept(I18nUtil.tooltip("effect.when_" + (isDrink ? "drunk" : "eaten")).withStyle(ChatFormatting.DARK_PURPLE));
            consumeEffectTooltips.forEach(consumer);
        }
    }
}
