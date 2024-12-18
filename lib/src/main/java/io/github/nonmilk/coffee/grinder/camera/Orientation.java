package io.github.nonmilk.coffee.grinder.camera;

import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.mat.Matrix4Col;
import io.github.alphameo.linear_algebra.mat.Matrix4Row;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Floats;
import io.github.nonmilk.coffee.grinder.math.Vec3f;

/**
 * A class holding camera's position in 3D space and the direction it is
 * pointing towards.
 */
public class Orientation {

    private Vector3 position;
    private Vector3 target;

    private final Matrix4 translation = new Mat4();

    {
        translation.set(Matrix4Row.R3, Matrix4Col.C3, 1);
    }

    /**
     * Initializes a {@code ClippingBox} record with the specified distances to near
     * and far plane.
     *
     * @param position {@code Vector3} representing camera position
     * @param target   {@code Vector3} a position looked at by the camera
     * @throws IllegalArgumentException If target and position match
     */
    public Orientation(final Vector3 position, final Vector3 target) {
        checkOverlap(Objects.requireNonNull(position), Objects.requireNonNull(target));
        this.position = position;
        this.target = target;
    }

    public Vector3 position() {
        return position;
    }

    public void setPosition(final Vector3 position) {
        checkOverlap(Objects.requireNonNull(position), target);
        this.position = position;
    }

    public Vector3 target() {
        return target;
    }

    public void setTarget(final Vector3 target) {
        checkOverlap(position, Objects.requireNonNull(target));
        this.target = target;
    }

    public Vector3 lookDir() {
        return Vec3Math.subtracted(target, position);
    }

    private void checkOverlap(final Vector3 position, final Vector3 target) {
        final Vector3 delta = Vec3Math.subtracted(position, target);
        if (Floats.equals(Vec3Math.len2(delta), 0)) {
            throw new IllegalArgumentException("Camera target vector equals camera position");
        }
    }

    /**
     * Updates the view matrix.
     *
     * @param camera {@code Camera} child class
     * @return a camera's look at matrix
     */
    public void translate() {
        final Vector3 cameraZ = lookDir();

        final float cameraZLen = Vec3Math.len(cameraZ);
        if (Floats.equals(cameraZLen, 0)) {
            throw new RuntimeException("Camera target vector equals camera position");
        }

        final Vector3 cameraX;
        final Vector3 xCandidate = Vec3Math.cross(cameraZ, Vec3f.VECTOR_J);
        if (Floats.equals(Vec3Math.len(xCandidate) / cameraZLen, 0)) {
            cameraX = Vec3Math.cross(cameraZ, Vec3f.VECTOR_I);
        } else {
            cameraX = xCandidate;
        }

        final Vector3 cameraY = Vec3Math.cross(cameraZ, cameraX);

        Vec3Math.normalize(cameraX);
        Vec3Math.normalize(cameraY);
        Vec3Math.normalize(cameraZ);

        Vector3 cameraPos = position;
        float cameraXProj = -Vec3Math.dot(cameraX, cameraPos);
        float cameraYProj = -Vec3Math.dot(cameraY, cameraPos);
        float cameraZProj = -Vec3Math.dot(cameraZ, cameraPos);

        // YOLO

        translation.set(Matrix4Row.R0, Matrix4Col.C0, cameraX.x());
        translation.set(Matrix4Row.R0, Matrix4Col.C1, cameraY.x());
        translation.set(Matrix4Row.R0, Matrix4Col.C2, cameraZ.x());

        translation.set(Matrix4Row.R1, Matrix4Col.C0, cameraX.y());
        translation.set(Matrix4Row.R1, Matrix4Col.C1, cameraY.y());
        translation.set(Matrix4Row.R1, Matrix4Col.C2, cameraZ.y());

        translation.set(Matrix4Row.R2, Matrix4Col.C0, cameraX.z());
        translation.set(Matrix4Row.R2, Matrix4Col.C1, cameraY.z());
        translation.set(Matrix4Row.R2, Matrix4Col.C2, cameraZ.z());

        translation.set(Matrix4Row.R3, Matrix4Col.C0, cameraXProj);
        translation.set(Matrix4Row.R3, Matrix4Col.C1, cameraYProj);
        translation.set(Matrix4Row.R3, Matrix4Col.C2, cameraZProj);
    }

    public Matrix4 translation() {
        return translation;
    }
}
