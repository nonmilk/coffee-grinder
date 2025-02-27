package io.github.nonmilk.coffee.grinder.camera;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.mat.Matrix4Col;
import io.github.alphameo.linear_algebra.mat.Matrix4Row;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.camera.view.PerspectiveView;

public class TestPerspectiveCamera {

    public Matrix4 matrixManual(float fovDeg, float ar, float n, float f) {
        float fov = (float) Math.toRadians(fovDeg);
        Matrix4 m = new Mat4();
        m.set(Matrix4Row.R0, Matrix4Col.C0, 1 / ((float) Math.tan(fov)));
        m.set(Matrix4Row.R1, Matrix4Col.C1, 1 / ((float) Math.tan(fov)) / ar);
        m.set(Matrix4Row.R2, Matrix4Col.C2, (f + n) / (f - n));
        m.set(Matrix4Row.R2, Matrix4Col.C3, (2 * f * n) / (n - f));
        m.set(Matrix4Row.R3, Matrix4Col.C2, 1);

        return m;
    }

    public Matrix4 matrixViaCamera(float fovDeg, float ar, float n, float f) {
        float fov = (float) Math.toRadians(fovDeg);
        Vector3 position = new Vec3();
        Vector3 target = new Vec3(1, 1, 1);
        Orientation orientation = new Orientation(position, target);
        PerspectiveView view = new PerspectiveView(fov, ar);
        ClippingBox clipping = new ClippingBox(n, f);
        PerspectiveCamera pc = new PerspectiveCamera(orientation, view, clipping);

        return pc.projection();
    }

    @Test
    public void testProjectionMatrix1() {
        float fieldOfViewDeg = 45;
        float aspectRatio = 1;
        float nearPlane = 12;
        float farPlane = 50;

        Matrix4 expected = matrixManual(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testProjectionMatrix2() {
        float fieldOfViewDeg = 90;
        float aspectRatio = 0.3f;
        float nearPlane = 12;
        float farPlane = 500;

        Matrix4 expected = matrixManual(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testProjectionMatrix3() {
        float fieldOfViewDeg = 120;
        float aspectRatio = 14;
        float nearPlane = 1;
        float farPlane = 5;

        Matrix4 expected = matrixManual(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testProjectionMatrix4() {
        float fieldOfViewDeg = 1;
        float aspectRatio = 1;
        float nearPlane = 1;
        float farPlane = 1.1f;

        Matrix4 expected = matrixManual(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(fieldOfViewDeg, aspectRatio, nearPlane, farPlane);

        Assertions.assertTrue(Mat4Math.equalsEpsilon(expected, actual, 1e-4f));
    }
}
