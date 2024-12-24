package io.github.nonmilk.coffee.grinder.render;

import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ScreenTransform {
    private final Matrix4 view;
    private final Matrix4 projection;
    private final Matrix4 viewProjection;
    private final Matrix4 model;
    private final float width;
    private final float height;

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

        Canvas canvas = Objects.requireNonNull(ctx).getCanvas();
        this.width = (float) canvas.getWidth();
        this.height = (float) canvas.getHeight();
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

    public float width() {
        return width;
    }

    public float heigth() {
        return height;
    }
}
