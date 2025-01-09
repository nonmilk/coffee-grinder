package io.github.nonmilk.coffee.grinder.camera;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.mat.Matrix4Col;
import io.github.alphameo.linear_algebra.mat.Matrix4Row;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.camera.view.OrthographicView;

public class TestOrthographicCamera {

    public Matrix4 matrixManual(float w, float h, float n, float f) {
        Matrix4 m = new Mat4();
        m.set(Matrix4Row.R0, Matrix4Col.C0, 2 / w);
        m.set(Matrix4Row.R1, Matrix4Col.C1, 2 / h);
        m.set(Matrix4Row.R2, Matrix4Col.C2, -2 / (f - n));
        m.set(Matrix4Row.R3, Matrix4Col.C2, -(f + n) / (f - n));
        m.set(Matrix4Row.R3, Matrix4Col.C3, 1);

        return m;
    }

    public Matrix4 matrixViaCamera(float w, float h, float n, float f) {
        Vector3 position = new Vec3();
        Vector3 target = new Vec3(1, 1, 1);
        Orientation orientation = new Orientation(position, target);
        OrthographicView view = new OrthographicView(w, h);
        ClippingBox clipping = new ClippingBox(n, f);
        OrthographicCamera oc = new OrthographicCamera(orientation, view, clipping);

        return oc.projection();
    }

    @Test
    public void testProjectionMatrix1() {
        float width = 1920;
        float height = 1080;
        float nearPlane = 12;
        float farPlane = 50;

        Matrix4 expected = matrixManual(width, height, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(width, height, nearPlane, farPlane);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testProjectionMatrix2() {
        float width = 1;
        float height = 108;
        float nearPlane = 12;
        float farPlane = 5000;

        Matrix4 expected = matrixManual(width, height, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(width, height, nearPlane, farPlane);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testProjectionMatrix3() {
        float width = 1.1f;
        float height = 10.8f;
        float nearPlane = 1;
        float farPlane = 200;

        Matrix4 expected = matrixManual(width, height, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(width, height, nearPlane, farPlane);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testProjectionMatrix4() {
        float width = 55555.5f;
        float height = 44440.1f;
        float nearPlane = 1;
        float farPlane = 2;

        Matrix4 expected = matrixManual(width, height, nearPlane, farPlane);
        Matrix4 actual = matrixViaCamera(width, height, nearPlane, farPlane);
        Assertions.assertEquals(expected, actual);
    }
}
