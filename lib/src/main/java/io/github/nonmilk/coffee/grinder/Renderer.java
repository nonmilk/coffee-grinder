package io.github.nonmilk.coffee.grinder;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.render.Scene;
import io.github.nonmilk.coffee.grinder.render.TexturedFiller;
import io.github.nonmilk.coffee.grinder.render.RenderedFace;
import io.github.nonmilk.coffee.grinder.render.ScreenTransform;
import io.github.nonmilk.coffee.grinder.render.ZBuffer;
import io.github.nonmilk.coffee.grinder.render.ColorTexture;
import io.github.nonmilk.coffee.grinder.render.triangle.Lighting;
import io.github.shimeoki.jfx.rasterization.triangle.IntBresenhamTriangler;
import io.github.shimeoki.jfx.rasterization.triangle.Triangler;
import io.github.shimeoki.jfx.rasterization.color.Colorf;
import io.github.shimeoki.jshaper.obj.Face;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    private GraphicsContext ctx;
    private Scene scene;
    private Triangler triangler;
    private TexturedFiller texturedFiller;
    private ZBuffer zBuffer;
    private Lighting lighting;

    public Renderer(GraphicsContext ctx) {
        this.ctx = ctx;
        triangler = new IntBresenhamTriangler(ctx);
        // FIXME get screen dimensions?
        this.zBuffer = new ZBuffer(1920, 1080);
        this.lighting = new Lighting(0.3f);
        this.texturedFiller = new TexturedFiller(zBuffer, lighting, new ColorTexture(new Colorf(0.5f, 0.5f, 0.5f, 1f)));
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
        texturedFiller.setTexture(model.texture());
        for (Face face : model.faces()) {
            Camera camera = scene.selectedCamera();
            ScreenTransform transform = new ScreenTransform(model, camera, ctx);
            lighting.setRay(Vec3Math.normalized(camera.orientation().lookDir()));
            RenderedFace triangle = new RenderedFace(face, transform);
            texturedFiller.setTriangle(triangle);

            triangler.draw(triangle);
        }
    }
}
