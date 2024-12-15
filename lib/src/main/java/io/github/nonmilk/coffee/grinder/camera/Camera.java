package io.github.nonmilk.coffee.grinder.camera;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;

public abstract class Camera {

    private Vector3 position;
    private Vector3 target;
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
        this.position = cameraOrientation.position();
        this.target = cameraOrientation.target();
        this.clippingBox = clippingBox;
    }

    /**
     * Gets the camera position in 3D space.
     *
     * @return {@code Vector3} representing camera position in 3D space
     */
    public Vector3 position() {
        return new Vec3(position);
    }

    /**
     * Sets the camera position in 3D space.
     *
     * @param newPosition new {@code Vector3} position
     */
    public void setPosition(final Vector3 newPosition) {
        this.position = newPosition;
    }

    /**
     * Offsets the camera position in 3D space.
     *
     * @param positionOffset new {@code Vector3} position
     */
    public void offsetPosition(final Vector3 positionOffset) {
        Vec3Math.add(this.position, positionOffset);
    }

    /**
     * Gets the direction camera is looking at.
     *
     * @return {@code Vector3} representing camera target
     */
    public Vector3 target() {
        return new Vec3(target);
    }

    /**
     * Sets the direction camera is looking at.
     *
     * @param newTarget {@code Vector3} representing camera target
     */
    public void setTarget(final Vector3 newTarget) {
        this.target = newTarget;
    }

    /**
     * Offsets the direction camera is looking at.
     *
     * @param targetOffset {@code Vector3} representing camera target offset
     */
    public void offsetTarget(final Vector3 targetOffset) {
        Vec3Math.add(this.target, targetOffset);
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
