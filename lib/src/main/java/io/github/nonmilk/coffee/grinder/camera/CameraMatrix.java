package io.github.nonmilk.coffee.grinder.camera;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.camera.view.OrthographicView;
import io.github.nonmilk.coffee.grinder.math.Floats;

// can be moved to Camera
/**
 * A utility class containing methods for calculating {@code Camera}'s
 * transformation matrices.
 */
final public class CameraMatrix {

    private static final Vector3 VECTOR_I = new Vec3(1, 0, 0);
    private static final Vector3 VECTOR_J = new Vec3(0, 1, 0);

    /**
     * Returns a camera's look at matrix.
     *
     * @param camera {@code Camera} child class
     * @return a camera's look at matrix
     */
    public static Matrix4 lookAt(final Camera camera) {
        final Vector3 cameraZ = camera.cameraOrientation().lookDir();

        final float cameraZLen = Vec3Math.len(cameraZ);
        if (Floats.equals(cameraZLen, 0)) {
            throw new RuntimeException("Camera target vector equals camera position");
        }

        final Vector3 cameraX;
        final Vector3 xCandidate = Vec3Math.cross(cameraZ, VECTOR_J);
        if (Floats.equals(Vec3Math.len(xCandidate) / cameraZLen, 0)) {
            cameraX = Vec3Math.cross(cameraZ, VECTOR_I);
        } else {
            cameraX = xCandidate;
        }

        final Vector3 cameraY = Vec3Math.cross(cameraZ, cameraX);

        Vec3Math.normalize(cameraX);
        Vec3Math.normalize(cameraY);
        Vec3Math.normalize(cameraZ);

        Vector3 cameraPos = camera.cameraOrientation().position();
        float cameraXProj = -Vec3Math.dot(cameraX, cameraPos);
        float cameraYProj = -Vec3Math.dot(cameraY, cameraPos);
        float cameraZProj = -Vec3Math.dot(cameraZ, cameraPos);

        // forgive me for I have sinned
        return new Mat4(
                cameraX.x(), cameraY.x(), cameraZ.x(), 0,
                cameraX.y(), cameraY.y(), cameraZ.y(), 0,
                cameraX.z(), cameraY.z(), cameraZ.z(), 0,
                cameraXProj, cameraYProj, cameraZProj, 1);
    }

    // might improve readability if an empty matrix
    // is created first and then cells are filled
    /**
     * Returns an orthographic camera's projection matrix.
     *
     * @param camera {@code OrthographicCamera}
     * @return a camera's look at matrix
     */
    public static Matrix4 orthographicProjection(final OrthographicCamera camera) {
        final float clippingSum = camera.clippingBox().sum();
        final float clippingDif = camera.clippingBox().diff();
        final OrthographicView view = camera.view();

        return new Mat4(
                2 / view.width(), 0, 0, 0,
                0, 2 / view.height(), 0, 0,
                0, 0, -2f / clippingDif, 0,
                0, 0, -clippingSum / clippingDif, 1);
    }

    /**
     * Returns a perspective camera's projection matrix.
     *
     * @param camera {@code PerspectiveCamera}
     * @return a camera's look at matrix
     */
    public static Matrix4 perspectiveProjection(final PerspectiveCamera camera) {
        final float invFovTan = (float) (1f / Math.tan(camera.view().fov()));

        final ClippingBox box = camera.clippingBox();
        final float diff = box.diff();

        return new Mat4(
                1 / invFovTan, 0, 0, 0,
                0, invFovTan / camera.view().aspectRatio(), 0, 0,
                0, 0, box.sum() / diff, 2 * box.prod() / -diff,
                0, 0, 1, 0);
    }
}
