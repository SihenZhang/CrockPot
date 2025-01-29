package com.sihenzhang.crockpot.util;

import java.text.DecimalFormat;

public final class StringUtils {
    public static String format(final double d, final String pattern) {
        return new DecimalFormat(pattern).format(d);
    }

    public static String format(final float f, final String pattern) {
        return new DecimalFormat(pattern).format(f);
    }
}
