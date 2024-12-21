package io.github.nonmilk.coffee.grinder;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vec4Math;
import io.github.alphameo.linear_algebra.vec.Vector4;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.math.Vec4f;
import io.github.nonmilk.coffee.grinder.render.Scene;
import io.github.nonmilk.coffee.grinder.render.TexturedFiller;
import io.github.nonmilk.coffee.grinder.render.TexturedTriangle;
import io.github.nonmilk.coffee.grinder.render.ZBuffer;
import io.github.shimeoki.jfx.rasterization.triangle.IntBresenhamTriangler;
import io.github.shimeoki.jfx.rasterization.triangle.Triangler;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    private GraphicsContext ctx;
    private Scene scene;
    private Triangler triangler;
    private TexturedFiller texturedFiller;
    private ZBuffer zBuffer;

    public Renderer(GraphicsContext ctx) {
        this.ctx = ctx;
        triangler = new IntBresenhamTriangler(ctx);
        // FIXME get screen dimensions?
        this.zBuffer = new ZBuffer(1920, 1080);
        this.texturedFiller = new TexturedFiller(zBuffer);
        triangler.setFiller(texturedFiller);
    }

    public Scene scene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void render() {
        Camera selectedCamera = scene.selectedCamera();
        // updates every frame, can be done externally
        // I'll leave it here untill we find the best strategy
        // for updating it
        selectedCamera.orientation().lookAt();
        Matrix4 viewProjMatrix = Mat4Math.prod(
                selectedCamera.projection(),
                selectedCamera.orientation().view());

        zBuffer.clear();
        for (Model model : scene.models()) {
            renderModel(model, viewProjMatrix);
        }
    }

    // assumes a model was triangulated
    private void renderModel(Model model, Matrix4 viewProjMatrix) {
        for (ObjFace face : model.faces()) {
            Vec3f[] projectedPoints = new Vec3f[3];
            for (int i = 0; i < 3; i++) {
                Vector4 vertex = new Vec4f(face.triplets().get(i).vertex());
                vertex = Mat4Math.prod(model.modelMatrix(), vertex);
                vertex = Mat4Math.prod(viewProjMatrix, vertex);
                Vec4Math.divide(vertex, vertex.w());
                projectedPoints[i] = vertexToScreen(vertex);
            }
            TexturedTriangle triangle = new TexturedTriangle(
                    projectedPoints[0],
                    projectedPoints[1],
                    projectedPoints[2]);
            texturedFiller.setTriangle(triangle);

            triangler.draw(triangle);
        }
    }

    private Vec3f vertexToScreen(Vector4 vertex) {
        float width = (float) ctx.getCanvas().getWidth();
        float height = (float) ctx.getCanvas().getHeight();
        return new Vec3f(
                vertex.x() * width + width / 2,
                -vertex.y() * height + height / 2,
                vertex.z());
    }
}