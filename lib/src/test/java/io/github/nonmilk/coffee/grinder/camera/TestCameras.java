package io.github.nonmilk.coffee.grinder.camera;

import org.junit.jupiter.api.Test;

import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.camera.view.OrthographicView;
import io.github.nonmilk.coffee.grinder.camera.view.PerspectiveView;

public class TestCameras {

    @Test
    public void testPerspectiveCamera() {
        Vector3 position = new Vec3();
        Vector3 target = new Vec3();
        Orientation orientation = new Orientation(position, target);

        float fov = 45;
        float aspectRatio = 1;
        PerspectiveView view = new PerspectiveView(fov, aspectRatio);

        float nearPlane = 12;
        float farPlane = 50;
        ClippingBox clipping = new ClippingBox(nearPlane, farPlane);

        PerspectiveCamera pc = new PerspectiveCamera(orientation, view, clipping);
    }

    public void testOrthographicCamera() {
        Vector3 position = new Vec3();
        Vector3 target = new Vec3();
        Orientation orientation = new Orientation(position, target);

        float width = 1920f;
        float height = 1080f;
        OrthographicView view = new OrthographicView(width, height);

        float nearPlane = 12;
        float farPlane = 50;
        ClippingBox clipping = new ClippingBox(nearPlane, farPlane);

        OrthographicCamera oc = new OrthographicCamera(orientation, view, clipping);
    }
}
