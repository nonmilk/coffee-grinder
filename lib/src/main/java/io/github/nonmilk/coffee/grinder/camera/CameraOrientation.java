package io.github.nonmilk.coffee.grinder.camera;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Floats;

/**
 * A camera's position in 3D space and the direction it is pointing towards.
 *
 * @param position {@code Vector3} representing camera position
 * @param target   {@code Vector3} a position looked at by the camera
 */
public record CameraOrientation(Vector3 position, Vector3 target) {

    /**
     * Constructs a {@code ClippingBox} record with the specified distances to near
     * and far plane.
     *
     * @param position {@code Vector3} representing camera position
     * @param target   {@code Vector3} a position looked at by the camera
     * @throws IllegalArgumentException If target and position match
     */
    public CameraOrientation {
        // copy vectors to break the link?
        if (Floats.equals(Vec3Math.len2(Vec3Math.subtracted(position, target)), 0)) {
            throw new RuntimeException("Camera target vector equals camera position");
        }
    }
}
