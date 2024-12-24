package io.github.nonmilk.coffee.grinder.render;

import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ScreenTransform {
    private final Matrix4 view;
    private final Matrix4 projection;
    private final Matrix4 viewProjection;
    private final Matrix4 model;
    private final Canvas canvas;
    private final Vector3 lightRay;

    public ScreenTransform(
            final Model model,
            final Camera camera,
            final GraphicsContext ctx) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(camera);

        this.model = model.modelMatrix();
        this.view = camera.orientation().view();
        this.projection = camera.projection();
        this.viewProjection = Mat4Math.prod(projection, view);
        this.lightRay = Vec3Math.normalized(camera.orientation().lookDir());

        this.canvas = Objects.requireNonNull(ctx).getCanvas();
    }

    public Matrix4 view() {
        return view;
    }

    public Matrix4 projection() {
        return projection;
    }

    public Matrix4 viewProjection() {
        return viewProjection;
    }

    public Matrix4 model() {
        return model;
    }

    public Vector3 lightRay() {
        return lightRay;
    }

    public float width() {
        return (float) canvas.getWidth();
    }

    public float heigth() {
        return (float) canvas.getHeight();
    }
}
