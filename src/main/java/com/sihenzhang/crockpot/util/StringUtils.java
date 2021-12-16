package com.sihenzhang.crockpot.util;

import java.text.DecimalFormat;

public final class StringUtils {
    public static String formatTickDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds %= 60;
        return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
    }

    public static String format(final double d, final String pattern) {
        return new DecimalFormat(pattern).format(d);
    }

    public static String format(final float f, final String pattern) {
        return new DecimalFormat(pattern).format(f);
    }
}
