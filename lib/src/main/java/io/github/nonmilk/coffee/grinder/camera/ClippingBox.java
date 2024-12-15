package io.github.nonmilk.coffee.grinder.camera;

/**
 * Near and far clipping planes.
 *
 * @param nearPlane distance to near plane. Must be positive
 * @param farPlane  distance to far plane. Must be farther than near plane
 */
public record ClippingBox(float nearPlane, float farPlane) {

    /**
     * Constructs a {@code ClippingBox} record with the specified distances to near
     * and far plane.
     *
     * @param nearPlane distance to near plane. Must be positive
     * @param farPlane  distance to far plane. Must be farther than near plane
     * @throws IllegalArgumentException If distance to near plane is not positive
     *                                  or far plane is closer to camera than the
     *                                  near plane
     */
    public ClippingBox {
        if (nearPlane < 0) {
            throw new RuntimeException("Near plane is behind camera");
        }
        if (farPlane <= nearPlane) {
            throw new RuntimeException("Far plane is behind near plane");
        }
    }
}
