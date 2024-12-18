package io.github.nonmilk.coffee.grinder.render;

import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import java.util.ArrayList;
import java.util.List;

// FIXME write scene
public class Scene {
    
    private final List<Camera> cameras = new ArrayList<>();
    private Camera selectedCamera;
    private final List<Model> models = new ArrayList<>();

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
