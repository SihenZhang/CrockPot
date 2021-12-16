package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.util.random.WeightedEntry;

import java.text.DecimalFormat;

public final class StringUtils {
    public static String format(final double d, final String pattern) {
        return new DecimalFormat(pattern).format(d);
    }

    public static String format(final float f, final String pattern) {
        return new DecimalFormat(pattern).format(f);
    }

    public static String formatCountAndChance(WeightedEntry.Wrapper<RangedItem> weightedRangedItem, int totalWeight) {
        RangedItem rangedItem = weightedRangedItem.getData();
        float chance = (float) weightedRangedItem.getWeight().asInt() / totalWeight;
        StringBuilder chanceTooltip = new StringBuilder();
        if (rangedItem.isRanged()) {
            chanceTooltip.append(rangedItem.min).append("-").append(rangedItem.max);
        } else {
            chanceTooltip.append(rangedItem.min);
        }
        chanceTooltip.append(" (").append(StringUtils.format(chance, "0.00%")).append(")");
        return chanceTooltip.toString();
    }
}
