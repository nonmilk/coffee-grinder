package io.github.nonmilk.coffee.grinder.camera;

import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.nonmilk.coffee.grinder.camera.view.PerspectiveView;

/**
 * Represents a perspective projection camera.
 * <p>
 * This camera is defined by a field of view and an aspect ratio, enabling it
 * to render objects with perspective projection.
 */
public final class PerspectiveCamera implements Camera {

    private ClippingBox clipping;
    private Orientation orientation;
    private PerspectiveView view;

    /**
     * Initializes a new {@code PerspectiveCamera} with the specified orientation,
     * perspective and clipping box.
     *
     * @param orientation the camera's orientation, defining its position and target
     *                    in 3D space
     * @param view        the perspective view settings: field of view and aspect
     *                    ratio
     * @param clipping    the clipping box defining the near and far clipping planes
     */
    public PerspectiveCamera(final Orientation orientation, final PerspectiveView view,
            final ClippingBox clipping) {
        this.clipping = Objects.requireNonNull(clipping);
        this.orientation = Objects.requireNonNull(orientation);
        this.view = Objects.requireNonNull(view);
    }

    /**
     * Returns a camera's render params.
     * 
     * @return {@code PerspectiveView} camera's render params
     */
    public PerspectiveView view() {
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
     * Returns a perspective camera's projection matrix.
     *
     * @param camera {@code PerspectiveCamera}
     * @return a camera's look at matrix
     */
    @Override
    public Matrix4 projection() {
        final float invFovTan = (float) (1f / Math.tan(view.fov()));

        final ClippingBox box = clipping;
        final float diff = box.diff();

        return new Mat4(
                invFovTan, 0, 0, 0,
                0, invFovTan / view.aspectRatio(), 0, 0,
                0, 0, box.sum() / diff, 2 * box.prod() / -diff,
                0, 0, 1, 0);
    }
}
