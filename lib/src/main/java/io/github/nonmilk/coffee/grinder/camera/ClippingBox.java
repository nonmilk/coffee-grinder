package io.github.nonmilk.coffee.grinder.camera;

/**
 * A utility class for working with camera clipping planes.
 */
public class ClippingBox {

    private float nearPlane;
    private float farPlane;

    /**
     * Initializes a {@code ClippingBox} class with the specified distances to near
     * and far plane.
     *
     * @param nearPlane distance to near plane. Must be positive
     * @param farPlane  distance to far plane. Must be farther than near plane
     * @throws IllegalArgumentException If distance to near plane is not positive
     *                                  or far plane is closer to camera than the
     *                                  near plane
     */
    public ClippingBox(final float nearPlane, final float farPlane) {
        setNearPlane(nearPlane);
        setFarPlane(farPlane);
    }

    public ClippingBox(final ClippingBox clippingBox) {
        setNearPlane(clippingBox.nearPlane);
        setFarPlane(clippingBox.farPlane);
    }

    public float nearPlane() {
        return nearPlane;
    }

    public void setNearPlane(final float nearPlane) {
        if (nearPlane < 0) {
            throw new RuntimeException("Near plane is behind camera");
        }

        this.nearPlane = nearPlane;
    }

    public float farPlane() {
        return farPlane;
    }

    public void setFarPlane(final float farPlane) {
        if (farPlane <= nearPlane) {
            throw new RuntimeException("Far plane is behind near plane");
        }

        this.farPlane = farPlane;
    }

    public float sum() {
        return nearPlane + farPlane;
    }

    public float diff() {
        return farPlane - nearPlane;
    }

    public float prod() {
        return farPlane * nearPlane;
    }
}
