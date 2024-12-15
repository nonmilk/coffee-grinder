package io.github.nonmilk.coffee.grinder.camera;

import io.github.alphameo.linear_algebra.mat.Matrix4;

public abstract class Camera {

    private CameraOrientation cameraOrientation;
    private ClippingBox clippingBox;

    /**
     * Initializes a {@code Camera} object with the specified orientation and
     * clipping box parameters.
     *
     * @param cameraOrientation An instance of {@code CameraOrientation} that
     *                          defines the camera's
     *                          position in 3D space and the direction it is
     *                          pointing towards.
     * @param clippingBox       {@code ClippingBox} that specifies the near and far
     *                          clipping planes.
     */
    Camera(final CameraOrientation cameraOrientation, final ClippingBox clippingBox) {
        this.cameraOrientation = cameraOrientation;
        this.clippingBox = clippingBox;
    }

    /**
     * Gets the camera orientation.
     *
     * @return {@code CameraOrientation} representing camera
     * position and target direction in 3D space
     */
    public CameraOrientation cameraOrientation() {
        return cameraOrientation;
    }

    /**
     * Sets the camera orientation.
     *
     * @param newOrientation {@code CameraOrientation} representing camera
     * position and target direction in 3D space
     */
    public void setCameraOrientation(final CameraOrientation newOrientation) {
        this.cameraOrientation = newOrientation;
    }

    public ClippingBox clippingBox() {
        return clippingBox;
    }

    public void setClippingBox(final ClippingBox newClippingBox) {
        this.clippingBox = newClippingBox;
    }

    /**
     * Gets the camera view matrix,
     * which transforms world coordinates into camera or view space.
     *
     * @return {@code Matrix} representing camera view matrix
     */
    public Matrix4 getViewMatrix() {
        // TODO: cache? updates with changes to position, target
        return CameraMatrix.lookAt(this);
    };

    /**
     * Gets the camera projection matrix,
     * which transforms 3D camera or view space coordinates into 2D screen
     * coordinates
     *
     * @return {@code Matrix} representing camera projection matrix
     */
    public abstract Matrix4 getProjectionMatrix();
}
