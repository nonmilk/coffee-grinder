package io.github.nonmilk.coffee.grinder.render;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.render.triangle.Lighting;

import java.util.ArrayList;
import java.util.List;

// FIXME write scene
public class Scene {
    
    private final List<Camera> cameras = new ArrayList<>();
    private Camera selectedCamera;
    private final Lighting lighting = new Lighting(0.5f);
    private final List<Model> models = new ArrayList<>();

    public Lighting lighting() {
        return lighting;
    }

    public void lightFromCamera() {
        Vector3 lookDir = selectedCamera.orientation().lookDir();
        lighting.setRay(Vec3Math.normalized(lookDir));
    }

    public void selectCamera(final int cameraIndex) {
        selectedCamera = cameras.get(cameraIndex);
    }

    public Camera selectedCamera() {
        return selectedCamera;
    }

    public List<Camera> cameras() {
        return cameras;
    }

    public List<Model> models() {
        return models;
    }
}
