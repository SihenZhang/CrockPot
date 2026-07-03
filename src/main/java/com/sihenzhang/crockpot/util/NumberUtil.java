package com.sihenzhang.crockpot.util;

import com.google.common.base.Preconditions;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class NumberUtil {
    private NumberUtil() {
    }

    /**
     * Formats a finite {@code double} value with the supplied decimal pattern.
     *
     * @param pattern the {@link DecimalFormat} pattern to apply
     * @param value   the value to format
     * @return the formatted value
     * @throws IllegalArgumentException if {@code value} is {@code NaN} or infinite
     */
    public static String decimalFormat(String pattern, double value) {
        Preconditions.checkArgument(isValid(value), "value is NaN or Infinite!");
        return new DecimalFormat(pattern).format(value);
    }

    /**
     * Formats a {@code long} value with the supplied decimal pattern.
     *
     * @param pattern the {@link DecimalFormat} pattern to apply
     * @param value   the value to format
     * @return the formatted value
     */
    public static String decimalFormat(String pattern, long value) {
        return new DecimalFormat(pattern).format(value);
    }

    /**
     * Formats a value with the supplied decimal pattern.
     *
     * @param pattern the {@link DecimalFormat} pattern to apply
     * @param value   the value to format
     * @return the formatted value
     * @throws IllegalArgumentException if {@code value} cannot be formatted as a number, or is a
     *                                  {@code Double} or {@code Float} that is {@code NaN} or infinite
     */
    public static String decimalFormat(String pattern, Object value) {
        return decimalFormat(pattern, value, null);
    }

    /**
     * Formats a value with the supplied decimal pattern and optional rounding mode.
     *
     * @param pattern      the {@link DecimalFormat} pattern to apply
     * @param value        the value to format
     * @param roundingMode the {@link RoundingMode} to apply, or {@code null} to keep the formatter default
     * @return the formatted value
     * @throws IllegalArgumentException if {@code value} cannot be formatted as a number, or is a
     *                                  {@code Double} or {@code Float} that is {@code NaN} or infinite
     */
    public static String decimalFormat(String pattern, Object value, RoundingMode roundingMode) {
        if (value instanceof Number number) {
            Preconditions.checkArgument(isValidNumber(number), "value is NaN or Infinite!");
        }
        final var decimalFormat = new DecimalFormat(pattern);
        if (roundingMode != null) {
            decimalFormat.setRoundingMode(roundingMode);
        }
        return decimalFormat.format(value);
    }

    /**
     * Formats a number as a localized percentage string.
     *
     * @param number the value to format as a percentage
     * @param scale  the maximum number of fraction digits to display
     * @return the formatted percentage string
     */
    public static String formatPercent(double number, int scale) {
        final var format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(scale);
        return format.format(number);
    }

    /**
     * Returns whether two {@code double} values are close to each other using relative and absolute tolerances.
     *
     * <p>The comparison formula is based on Python's {@code math.isclose}:
     * {@code abs(a - b) <= max(relTol * max(abs(a), abs(b)), absTol)}.</p>
     *
     * @param a      the first value to compare
     * @param b      the second value to compare
     * @param relTol the relative tolerance; must be non-negative and less than {@code 1.0}
     * @param absTol the absolute tolerance; must be non-negative
     * @return {@code true} if the values are close to each other; otherwise {@code false}
     * @throws IllegalArgumentException if {@code relTol} or {@code absTol} is outside the allowed range
     * @see <a href="https://docs.python.org/3/library/math.html#math.isclose">Python math.isclose</a>
     */
    public static boolean isClose(double a, double b, double relTol, double absTol) {
        Preconditions.checkArgument(relTol >= 0.0D && relTol < 1.0D, "relTol must be non-negative and less than 1.0!");
        Preconditions.checkArgument(absTol >= 0.0D, "absTol must be non-negative!");
        if (a == b) {
            return true;
        }
        if (!isValid(a) || !isValid(b)) {
            return false;
        }
        return Math.abs(a - b) <= Math.max(relTol * Math.max(Math.abs(a), Math.abs(b)), absTol);
    }

    /**
     * Returns whether two {@code double} values are close to each other using default tolerances.
     *
     * @param a the first value to compare
     * @param b the second value to compare
     * @return {@code true} if the values are close to each other; otherwise {@code false}
     * @see #isClose(double, double, double, double)
     */
    public static boolean isClose(double a, double b) {
        return isClose(a, b, 1E-9D, 1E-12D);
    }

    /**
     * Returns whether two {@code float} values are close to each other using relative and absolute tolerances.
     *
     * <p>The comparison formula is based on Python's {@code math.isclose}:
     * {@code abs(a - b) <= max(relTol * max(abs(a), abs(b)), absTol)}.</p>
     *
     * @param a      the first value to compare
     * @param b      the second value to compare
     * @param relTol the relative tolerance; must be non-negative and less than {@code 1.0}
     * @param absTol the absolute tolerance; must be non-negative
     * @return {@code true} if the values are close to each other; otherwise {@code false}
     * @throws IllegalArgumentException if {@code relTol} or {@code absTol} is outside the allowed range
     * @see <a href="https://docs.python.org/3/library/math.html#math.isclose">Python math.isclose</a>
     */
    public static boolean isClose(float a, float b, float relTol, float absTol) {
        Preconditions.checkArgument(relTol >= 0.0F && relTol < 1.0F, "relTol must be non-negative and less than 1.0!");
        Preconditions.checkArgument(absTol >= 0.0F, "absTol must be non-negative!");
        if (a == b) {
            return true;
        }
        if (!isValid(a) || !isValid(b)) {
            return false;
        }
        return Math.abs(a - b) <= Math.max(relTol * Math.max(Math.abs(a), Math.abs(b)), absTol);
    }

    /**
     * Returns whether two {@code float} values are close to each other using default tolerances.
     *
     * @param a the first value to compare
     * @param b the second value to compare
     * @return {@code true} if the values are close to each other; otherwise {@code false}
     * @see #isClose(float, float, float, float)
     */
    public static boolean isClose(float a, float b) {
        return isClose(a, b, 1E-5F, 1E-6F);
    }

    /**
     * Returns whether a {@link Number} value can be used for finite numeric formatting.
     *
     * @param number the value to validate
     * @return {@code true} if {@code number} is not {@code null} and is not a {@code Double} or {@code Float}
     * that is {@code NaN} or infinite; otherwise {@code false}
     */
    public static boolean isValidNumber(Number number) {
        return switch (number) {
            case null -> false;
            case Double v -> !(v.isNaN() || v.isInfinite());
            case Float v -> !(v.isNaN() || v.isInfinite());
            default -> true;
        };
    }

    /**
     * Returns whether a {@code double} value is neither {@code NaN} nor infinite.
     *
     * @param number the value to validate
     * @return {@code true} if {@code number} is finite; otherwise {@code false}
     */
    public static boolean isValid(double number) {
        return !(Double.isNaN(number) || Double.isInfinite(number));
    }

    /**
     * Returns whether a {@code float} value is neither {@code NaN} nor infinite.
     *
     * @param number the value to validate
     * @return {@code true} if {@code number} is finite; otherwise {@code false}
     */
    public static boolean isValid(float number) {
        return !(Float.isNaN(number) || Float.isInfinite(number));
    }
}
