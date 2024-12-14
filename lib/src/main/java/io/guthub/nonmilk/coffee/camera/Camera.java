package io.guthub.nonmilk.coffee.camera;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vector3;

/**
 * An interface that represents a camera in 3D space.
 * It holds position, target direction, and is able to provide view and projection matrices.
 */
public interface Camera {

    /**
     * Gets the camera position in 3D space.
     * @return {@code Vector3} representing camera position in 3D space
     */
    public Vector3 position();

    /**
     * Sets the camera position in 3D space.
     * @param newPosition new {@code Vector3} position
     */
    public void setPosition(Vector3 newPosition);

    /**
     * Offsets the camera position in 3D space.
     * @param positionOffset new {@code Vector3} position
     */
    public void offsetPosition(Vector3 positionOffset);


    /**
     * Gets the direction camera is looking at.
     * @return {@code Vector3} representing camera target
     */
    public Vector3 target();

    /**
     * Sets the direction camera is looking at.
     * @param newTarget {@code Vector3} representing camera target
     */
    public void setTarget(Vector3 newTarget);

    /**
     * Offsets the direction camera is looking at.
     * @param targetOffset {@code Vector3} representing camera target offset
     */
    public void offsetTarget(Vector3 targetOffset);

    /**
     * Gets the camera view matrix,
     * which transforms world coordinates into camera or view space.
     * @return {@code Matrix} representing camera view matrix
     */
    public Matrix4 getViewMatrix();

    /**
     * Gets the camera projection matrix,
     * which transforms 3D camera or view space coordinates into 2D screen coordinates
     * @return {@code Matrix} representing camera projection matrix
     */
    public Matrix4 getProjectionMatrix();
}
