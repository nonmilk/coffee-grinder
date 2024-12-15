package io.guthub.nonmilk.coffee.camera;

import io.github.alphameo.linear_algebra.mat.Matrix4;

/**
 * Represents a perspective projection camera.
 * <p>This camera is defined by a field of view and an aspect ratio, enabling it
 * to render objects with perspective projection.
 */
public final class PerspectiveCamera extends Camera {

    private float fov;
    private float aspectRatio;

    /**
     * Initializes a new {@code PerspectiveCamera} with the specified orientation, perspective and clipping box.
     *
     * @param orientation the camera's orientation, defining its position and target in 3D space
     * @param perspective the perspective view settings: field of view and aspect ratio
     * @param clipping the clipping box defining the near and far clipping planes
     */
    public PerspectiveCamera(CameraOrientation orientation, PerspectiveView perspective, ClippingBox clipping) {
        super(orientation, clipping);
        this.fov = perspective.fov();
        this.aspectRatio = perspective.aspectRatio();
    }

    /**
     * Gets the camera aspect ratio.
     *
     * @return {@code float} aspect ratio, width / height
     */
    public float ar() {
        return aspectRatio;
    }

    /**
     * Sets the camera aspect ratio.
     *
     * @param newAr {@code float} aspect ratio, width / height
     */
    public void setAr(final float newAr) {
        // might work without this check, but I'll leave it here just to be safe
        if (newAr <= 0) {
            return;
        }
        this.aspectRatio = newAr;
    }

    /**
     * Gets the camera field of view.
     *
     * @return {@code float} field of view, in radians
     */
    public float fov() {
        return fov;
    }

    /**
     * Sets the camera field of view.
     *
     * @param newFov {@code float} field of view, in radians
     */
    public void setFov(final float newFov) {
        // might work without this check, but I'll leave it here just to be safe
        if (newFov <= 0 || newFov >= Math.TAU) {
            return;
        }
        this.fov = newFov;
    }

    @Override
    public Matrix4 getProjectionMatrix() {
        // TODO: cache? updates with changes to fov, ar, clipping planes
        return CameraMatrix.perspectiveProjection(this);
    }
}
