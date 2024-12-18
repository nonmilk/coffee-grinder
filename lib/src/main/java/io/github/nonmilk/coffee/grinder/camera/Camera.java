package io.github.nonmilk.coffee.grinder.camera;

import io.github.alphameo.linear_algebra.mat.Matrix4;

/**
 * An interface describing a camera in 3D space. It can return it's projection
 * matrix.
 */
public interface Camera extends Clippable {

    /**
     * Returns a camera's projection matrix.
     * 
     * @return {@code Matrix4} camera's projection matrix
     */
    public Matrix4 projection();

    /**
     * Returns a camera's view matrix.
     * 
     * @return {@code Matrix4} camera's view matrix
     */
    public Matrix4 viewMatrix();

    /**
     * Returns a camera's orientation.
     * 
     * @return {@code Orientation} camera's orientation
     */
    public Orientation orientation();
}
