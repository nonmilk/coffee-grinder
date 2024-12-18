package io.github.nonmilk.coffee.grinder.camera;

import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.camera.view.OrthographicView;

/**
 * Represents an orthographic camera.
 * <p>
 * This camera is defined by the width and height of the produced image,
 * enabling it
 * to render objects with parallel projection.
 */
public final class OrthographicCamera implements Camera {

    private ClippingBox clipping;
    private Orientation orientation;
    private OrthographicView view;

    /**
     * Initializes a new {@code OrthographicCamera} with the specified orientation,
     * perspective and clipping box.
     *
     * @param orientation the camera's orientation, defining its position and target
     *                    in 3D space
     * @param view        the perspective view settings: field of view and aspect
     *                    ratio
     * @param clipping    the clipping box defining the near and far clipping planes
     */
    public OrthographicCamera(final Orientation orientation, final OrthographicView view,
            final ClippingBox clipping) {
        this.clipping = Objects.requireNonNull(clipping);
        this.orientation = Objects.requireNonNull(orientation);
        this.view = Objects.requireNonNull(view);
    }

    /**
     * Returns a camera's lens params.
     * 
     * @return {@code OrthographicView} camera's lens params
     */
    public OrthographicView view() {
        return view;
    }

    @Override
    public Orientation orientation() {
        return orientation;
    }

    @Override
    public ClippingBox box() {
        return clipping;
    }

    /**
     * Returns an orthographic camera's projection matrix.
     */
    @Override
    public Matrix4 projection() {
        final float clippingSum = clipping.sum();
        final float clippingDif = clipping.diff();

        return new Mat4(
                2 / view.width(), 0, 0, 0,
                0, 2 / view.height(), 0, 0,
                0, 0, -2f / clippingDif, 0,
                0, 0, -clippingSum / clippingDif, 1);
    }
}
