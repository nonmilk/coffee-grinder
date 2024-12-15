package io.github.nonmilk.coffee.grinder.camera;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.camera.view.OrthographicView;

/**
 * Represents an orthographic camera.
 * <p>
 * This camera is defined by the width and height of the produced image,
 * enabling it
 * to render objects with parallel projection.
 */
public final class OrthographicCamera extends Camera {

    private float width;
    private float height;

    /**
     * Initializes a new {@code OrthographicCamera} with the specified orientation,
     * screen size and clipping box.
     *
     * @param orientation  the camera's orientation, defining its position and
     *                     target in 3D space
     * @param orthographic the screen settings: width and height
     * @param clipping     the clipping box defining the near and far clipping
     *                     planes
     */
    public OrthographicCamera(CameraOrientation orientation, OrthographicView orthographic, ClippingBox clipping) {
        super(orientation, clipping);
        this.width = orthographic.width();
        this.height = orthographic.height();
    }

    /**
     * Gets the camera view width.
     *
     * @return {@code float} view width
     */
    public float width() {
        return width;
    }

    /**
     * Sets the camera view width.
     *
     * @param newWidth {@code float} view width
     */
    public void setWidth(final float newWidth) {
        if (newWidth <= 0) {
            return;
        }
        this.width = newWidth;
    }

    /**
     * Gets the camera view height.
     *
     * @return {@code float} view height
     */
    public float height() {
        return height;
    }

    /**
     * Sets the camera field of view.
     *
     * @param newHeight {@code float} view height
     */
    public void setHeight(final float newHeight) {
        if (newHeight <= 0) {
            return;
        }
        this.height = newHeight;
    };

    @Override
    public Matrix4 getProjectionMatrix() {
        // TODO: cache? updates with changes to width, height, clipping planes
        return CameraMatrix.orthographicProjection(this);
    }
}
