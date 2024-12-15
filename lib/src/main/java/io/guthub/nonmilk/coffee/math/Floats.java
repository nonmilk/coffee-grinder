package io.guthub.nonmilk.coffee.math;

/**
 * A utility class for float comparisons.
 */
public final class Floats {
    public static final float EPSILON = 1E-10f;

    /**
     * Prevents class instantiation.
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
     * than {@link Floats#EPSILON}; {@code false} otherwise
     */
    public static boolean equals(final float v1, final float v2) {
        return Math.abs(v1 - v2) < EPSILON;
    }
}
