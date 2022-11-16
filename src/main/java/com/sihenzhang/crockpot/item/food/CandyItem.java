package com.sihenzhang.crockpot.item.food;

import com.mojang.blaze3d.platform.InputConstants;
import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class CandyItem extends CrockPotFoodItem {
    private static final Supplier<MutableComponent> SPACE = () -> new TextComponent("  ");
    private static final MutableComponent DELIMITER = new TextComponent(", ").withStyle(ChatFormatting.GRAY);

    public CandyItem() {
        super(CrockPotFoodItem.builder().nutrition(3).saturationMod(0.2F).alwaysEat().duration(FoodUseDuration.FAST)
                .effectTooltip("candy", ChatFormatting.DARK_GREEN)
                .effectTooltip(SPACE.get().append(I18nUtils.createTooltipComponent("effect.no_effect").withStyle(ChatFormatting.GRAY)))
                .effectTooltip(SPACE.get().append(I18nUtils.createTooltipComponent("effect.remove", new TranslatableComponent(MobEffects.MOVEMENT_SLOWDOWN.getDescriptionId())).withStyle(ChatFormatting.GOLD)))
                .effectTooltip(SPACE.get().append(new TranslatableComponent("potion.withAmplifier", new TranslatableComponent(MobEffects.SATURATION.getDescriptionId()), new TranslatableComponent("potion.potency.1")).withStyle(ChatFormatting.BLUE)).append(DELIMITER).append(I18nUtils.createTooltipComponent("effect.remove", new TranslatableComponent(MobEffects.HUNGER.getDescriptionId())).withStyle(ChatFormatting.GOLD)))
                .effectTooltip(SPACE.get().append(new TranslatableComponent("potion.withDuration", new TranslatableComponent(MobEffects.DIG_SPEED.getDescriptionId()), StringUtil.formatTickDuration(400)).withStyle(ChatFormatting.BLUE)).append(DELIMITER).append(I18nUtils.createTooltipComponent("effect.remove", new TranslatableComponent(MobEffects.DIG_SLOWDOWN.getDescriptionId())).withStyle(ChatFormatting.GOLD)))
                .effectTooltip(SPACE.get().append(new TranslatableComponent("potion.withDuration", new TranslatableComponent(MobEffects.WEAKNESS.getDescriptionId()), StringUtil.formatTickDuration(200)).withStyle(ChatFormatting.RED)).append(DELIMITER).append(I18nUtils.createTooltipComponent("effect.damage.single", 1).withStyle(ChatFormatting.RED)))
                .effectTooltip(SPACE.get().append(I18nUtils.createTooltipComponent("effect.damage.multiple", 5).withStyle(ChatFormatting.GRAY, ChatFormatting.OBFUSCATED)))
        );
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            var chance = pLevel.random.nextFloat();
            if (chance < 0.25F) {
                pLivingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            } else if (chance < 0.45F) {
                pLivingEntity.removeEffect(MobEffects.HUNGER);
                pLivingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 1));
            } else if (chance < 0.55F) {
                pLivingEntity.removeEffect(MobEffects.DIG_SLOWDOWN);
                pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 20));
            } else if (chance < 0.6F) {
                pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20));
                pLivingEntity.hurt(CrockPotDamageSource.CANDY, 2.0F);
            } else if (chance < 0.605F) {
                pLivingEntity.hurt(CrockPotDamageSource.CANDY, 10.0F);
            }
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            pTooltipComponents.add(I18nUtils.createTooltipComponent("candy.real").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
        } else {
            pTooltipComponents.add(I18nUtils.createTooltipComponent("candy").withStyle(ChatFormatting.DARK_AQUA));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
