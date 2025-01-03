package io.github.nonmilk.coffee.grinder.transformations;

/**
 * Rotation order.
 */
public enum RotationOrder {
    /**
     * Order: X -> Y -> Z.
     */
    XYZ,
    /**
     * Order: X -> Z -> Y.
     */
    XZY,
    /**
     * Order: Y -> X -> Z.
     */
    YXZ,
    /**
     * Order: Y -> Z -> X.
     */
    YZX,
    /**
     * Order: Z -> X -> Y.
     */
    ZXY,
    /**
     * Order: Z -> Y -> X.
     */
    ZYX;
}
