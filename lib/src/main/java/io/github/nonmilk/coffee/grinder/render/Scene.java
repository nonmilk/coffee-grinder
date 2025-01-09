package io.github.nonmilk.coffee.grinder.render;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.render.triangle.Lighting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scene {

    private Camera camera;
    private final Lighting lighting = new Lighting(0.5f);
    private final List<Model> models = new ArrayList<>();

    public Lighting lighting() {
        return lighting;
    }

    public void lightFromCamera() {
        Vector3 lookDir = camera.orientation().lookDir();
        lighting.setRay(Vec3Math.normalized(lookDir));
        lighting.setCamera(camera);
    }

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
