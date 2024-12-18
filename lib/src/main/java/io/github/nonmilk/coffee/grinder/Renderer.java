package io.github.nonmilk.coffee.grinder;

import java.util.ArrayList;
import java.util.List;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vec4Math;
import io.github.alphameo.linear_algebra.vec.Vector4;
import io.github.alphameo.linear_algebra.vec.Vector2;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.math.Vec2f;
import io.github.nonmilk.coffee.grinder.math.Vec4f;
import io.github.nonmilk.coffee.grinder.render.Scene;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    private GraphicsContext ctx;
    private Scene scene;

    public Renderer(GraphicsContext ctx) {
        this.ctx = ctx;
    }

    public Scene scene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void render() {
        Camera selectedCamera = scene.selectedCamera();
        Matrix4 viewProjMatrix = Mat4Math.prod(
                selectedCamera.projection(),
                selectedCamera.orientation().view());

        for (Model model : scene.models()) {
            renderModel(model, viewProjMatrix);
        }
    }

    // assumes a model was triangulated
    private void renderModel(Model model, Matrix4 viewProjMatrix) {
        for (ObjFace face : model.faces()) {
            List<Vector2> projectedPoints = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                Vector4 vertex = new Vec4f(face.triplets().get(i).vertex());
                vertex = Mat4Math.prod(model.modelMatrix(), vertex);
                vertex = Mat4Math.prod(viewProjMatrix, vertex);
                Vec4Math.divide(vertex, vertex.w());
                projectedPoints.add(vertexToScreen(vertex));
            }

            for (int i = 0; i < 3; i++) {
                Vector2 p1 = projectedPoints.get(i);
                Vector2 p2 = projectedPoints.get((i + 1) % 3);
                ctx.strokeLine(p1.x(), p1.y(), p2.x(), p2.y());
            }
        }
    }

    private Vector2 vertexToScreen(Vector4 vertex) {
        float width = (float) ctx.getCanvas().getWidth();
        float height = (float) ctx.getCanvas().getHeight();
        return new Vec2f(
                vertex.x() * width + width / 2,
                -vertex.y() * height + height / 2);
    }
}