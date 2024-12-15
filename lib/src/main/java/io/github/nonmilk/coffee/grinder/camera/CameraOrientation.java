package io.github.nonmilk.coffee.grinder.camera;

import java.util.Objects;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Floats;

/**
 * A class holding camera's position in 3D space and the direction it is
 * pointing towards.
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
    public CameraOrientation(final Vector3 position, final Vector3 target) {
        checkOverlap(position, target);
        this.position = position;
        this.target = target;
    }

    public Vector3 position() {
        return position;
    }

    public void setPosition(final Vector3 position) {
        checkOverlap(position, target);
        this.position = Objects.requireNonNull(position);
    }

    public Vector3 target() {
        return target;
    }

    public void setTarget(final Vector3 target) {
        checkOverlap(position, target);
        this.target = Objects.requireNonNull(target);
    }

    public Vector3 lookDir() {
        return Vec3Math.subtracted(target, position);
    }

    private void checkOverlap(final Vector3 position, final Vector3 target) {
        if (position == null || target == null) {
            return;
        }

        final Vector3 delta = Vec3Math.subtracted(position, target);
        if (Floats.equals(Vec3Math.len2(delta), 0)) {
            throw new IllegalArgumentException("Camera target vector equals camera position");
        }
    }
}
