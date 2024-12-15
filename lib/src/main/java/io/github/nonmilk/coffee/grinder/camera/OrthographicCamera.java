package io.github.nonmilk.coffee.grinder.camera;

// FIXME javadoc

import java.util.Objects;

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

    private OrthographicView view;

    /**
     * Initializes a new {@code OrthographicCamera} with the specified orientation,
     * screen size and clipping box.
     *
     * @param orientation the camera's orientation, defining its position and
     *                    target in 3D space
     * @param view        the screen settings: width and height
     * @param clipping    the clipping box defining the near and far clipping
     *                    planes
     */
    public OrthographicCamera(CameraOrientation orientation, OrthographicView view, ClippingBox clipping) {
        super(orientation, clipping);
        this.view = Objects.requireNonNull(view);
    }

    public OrthographicView view() {
        return view;
    }

    @Override
    public Matrix4 getProjectionMatrix() {
        // TODO: cache? updates with changes to width, height, clipping planes
        return CameraMatrix.orthographicProjection(this);
    }
}
