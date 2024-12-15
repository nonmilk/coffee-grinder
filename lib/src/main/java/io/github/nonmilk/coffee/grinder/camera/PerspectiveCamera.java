package io.github.nonmilk.coffee.grinder.camera;

// FIXME javadoc

import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.camera.view.PerspectiveView;

/**
 * Represents a perspective projection camera.
 * <p>
 * This camera is defined by a field of view and an aspect ratio, enabling it
 * to render objects with perspective projection.
 */
public final class PerspectiveCamera extends Camera {

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
    public PerspectiveCamera(CameraOrientation orientation, PerspectiveView view, ClippingBox clipping) {
        super(orientation, clipping);
        this.view = Objects.requireNonNull(view);
    }

    public PerspectiveView view() {
        return view;
    }

    @Override
    public Matrix4 getProjectionMatrix() {
        // TODO: cache? updates with changes to fov, ar, clipping planes
        return CameraMatrix.perspectiveProjection(this);
    }
}
