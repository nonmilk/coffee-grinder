package io.github.nonmilk.coffee.grinder.camera;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Floats;

/**
 * A class holding camera's position in 3D space and the direction it is pointing towards.
 */
public class CameraOrientation {

    private Vector3 position;
    private Vector3 target;

    /**
     * Initializes a {@code ClippingBox} record with the specified distances to near
     * and far plane.
     *
     * @param position {@code Vector3} representing camera position
     * @param target   {@code Vector3} a position looked at by the camera
     * @throws IllegalArgumentException If target and position match
     */
    public CameraOrientation (final Vector3 position, final Vector3 target) {
        checkOverlap(position, target);
        this.position = position;
        this.target = target;
    }

    public Vector3 position() {
        return position;
    }

    public void setPosition(final Vector3 position) {
        this.position = position;
    }

    public Vector3 target() {
        return target;
    }

    public Vector3 lookDir() {
        return Vec3Math.subtracted(target, position);
    }

    public void setTarget(final Vector3 target) {
        this.target = target;
    }

    private void checkOverlap(final Vector3 position, final Vector3 target) {
        if (Floats.equals(Vec3Math.len2(Vec3Math.subtracted(position, target)), 0)) {
            throw new IllegalArgumentException("Camera target vector equals camera position");
        }
    }
}
