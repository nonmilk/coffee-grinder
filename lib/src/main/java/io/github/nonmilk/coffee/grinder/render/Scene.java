package io.github.nonmilk.coffee.grinder.render;

import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scene {

    private Camera camera;
    private final List<Model> models = new ArrayList<>();

    public Camera camera() {
        return camera;
    }

    public void setCamera(final Camera c) {
        this.camera = Objects.requireNonNull(c);
    }

    public List<Model> models() {
        return models;
    }
}
