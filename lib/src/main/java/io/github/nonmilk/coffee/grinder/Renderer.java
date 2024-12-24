package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.render.Scene;
import io.github.nonmilk.coffee.grinder.render.TexturedFiller;
import io.github.nonmilk.coffee.grinder.render.TexturedTriangle;
import io.github.nonmilk.coffee.grinder.render.ScreenTransform;
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

        zBuffer.clear();
        for (Model model : scene.models()) {
            renderModel(model);
        }
    }

    // assumes a model was triangulated
    private void renderModel(Model model) {
        for (ObjFace face : model.faces()) {
            ScreenTransform transform = new ScreenTransform(
                model, scene.selectedCamera(), ctx);
            TexturedTriangle triangle = new TexturedTriangle(face, transform);
            texturedFiller.setTriangle(triangle);

            triangler.draw(triangle);
        }
    }
}