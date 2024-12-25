package io.github.nonmilk.coffee.grinder.math;

/**
 * A utility class for float comparisons.
 */
public final class Floats {

    /**
     * A value used for float comparisons.
     */
    public static final float EPSILON = 1E-10f;
    /**
     * A value used for less accurate float comparisons.
     */
    public static final float LAX_EPSILON = 1E-5f;

    /**
     * Prevents class instantiation.
     * 
     * @throws UnsupportedOperationException when called
     */
    private Floats() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * Returns {@code true}, if the difference between values is less
     * than {@link Floats#EPSILON}, effectively making them equal.
     *
     * @param v1 value 1
     * @param v2 value 2
     *
     * @return {@code true}, if the difference is less
     *         than {@link Floats#EPSILON}; {@code false} otherwise
     */
    public static boolean equals(final float v1, final float v2) {
        return Math.abs(v1 - v2) < EPSILON;
    }

    /**
     * Returns {@code true}, if the difference between values is less
     * than {@link Floats#LAX_EPSILON}, making them close enough.
     *
     * @param v1 value 1
     * @param v2 value 2
     *
     * @return {@code true}, if the difference is less
     *         than {@link Floats#LAX_EPSILON}; {@code false} otherwise
     */
    public static boolean laxEquals(final float v1, final float v2) {
        return Math.abs(v1 - v2) < LAX_EPSILON;
    }
}
