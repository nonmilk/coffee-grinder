package io.guthub.nonmilk.coffee.camera;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;

/**
 * An abstract class that represents a camera in 3D space.
 * It holds position, target direction, near and far planes
 * and is able to provide view and projection matrices.
 */
public abstract class Camera {

    private Vector3 position;
    private Vector3 target;
    private float nearPlane;
    private float farPlane;

    /**
     * Initializes a {@code Camera} object with the specified orientation and clipping box parameters.
     *
     * @param cameraOrientation An instance of {@code CameraOrientation} that defines the camera's
     * position in 3D space and the direction it is pointing towards.
     * @param clippingBox {@code ClippingBox} that specifies the near and far clipping planes.
     */
    Camera (final CameraOrientation cameraOrientation, final ClippingBox clippingBox) {
        this.position = cameraOrientation.position();
        this.target = cameraOrientation.target();
        this.nearPlane = clippingBox.nearPlane();
        this.farPlane = clippingBox.farPlane();
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

    /**
     * Gets the camera distance to near clipping plane.
     *
     * @return {@code float} distance to near clipping plane
     */
    public float nearPlane() {
        return nearPlane;
    }

    /**
     * Sets the camera distance to near clipping plane.
     *
     * @param newNearPlane {@code float} distance to near clipping plane
     */
    public void setNearPlane(final float newNearPlane) {
        if (newNearPlane <= 0) {
            this.nearPlane = 0;
        } else {
            this.nearPlane = newNearPlane;
        }
    }

    /**
     * Gets the camera distance to far clipping plane.
     *
     * @return {@code float} distance to far clipping plane
     */
    public float farPlane() {
        return farPlane;
    }

    /**
     * Sets the camera distance to far clipping plane.
     *
     * @param newFarPlane {@code float} distance to far clipping plane
     */
    public void setFarPlane(final float newFarPlane) {
        // might work without this check, but I'll leave it here just to be safe
        if (newFarPlane <= 0) {
            this.farPlane = 0;
        } else {
            this.farPlane = newFarPlane;
        }
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
     * which transforms 3D camera or view space coordinates into 2D screen coordinates
     *
     * @return {@code Matrix} representing camera projection matrix
     */
    public abstract Matrix4 getProjectionMatrix();
}
