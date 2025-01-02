package io.github.nonmilk.coffee.grinder;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.render.*;
import io.github.nonmilk.coffee.grinder.render.triangle.Lighting;
import io.github.shimeoki.jfx.rasterization.Colorf;
import io.github.shimeoki.jfx.rasterization.IntBresenhamTriangler;
import io.github.shimeoki.jfx.rasterization.Triangler;
import io.github.shimeoki.jshaper.obj.Face;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Objects;

public class RenderingPipeline {
    private final Triangler triangler;
    private final TexturedFiller texturedFiller;
    private final ZBuffer zBuffer;
    private final Lighting lighting;
    private final GraphicsContext ctx;

    public RenderingPipeline(final GraphicsContext ctx) {
        this.ctx = Objects.requireNonNull(ctx);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        this.zBuffer = new ZBuffer(screenWidth, screenHeight);
        triangler = new IntBresenhamTriangler(ctx);
        this.lighting = new Lighting(0.3f);

        this.texturedFiller = new TexturedFiller(zBuffer, lighting, new ColorTexture(new Colorf(0.5f, 0.5f, 0.5f, 1f)));
        triangler.setFiller(texturedFiller);
    }

    public void renderScene(Scene scene) {
        final Camera selectedCamera = scene.selectedCamera();
        // updates every frame, can be done externally
        // I'll leave it here until we find the best strategy
        // for updating it
        selectedCamera.orientation().lookAt();

        zBuffer.clear();
        lighting.setRay(Vec3Math.normalized(selectedCamera.orientation().lookDir()));
        final ScreenTransform transform = new ScreenTransform(selectedCamera, ctx);

        for (Model model : scene.models()) {
            transform.setModel(model.modelMatrix());
            texturedFiller.setTexture(model.texture());
            renderModel(model, transform);
        }
    }


    // assumes a model was triangulated
    // move to mesh
    private void renderModel(Model model, ScreenTransform transform) {
        for (Face face : model.faces()) {
            RenderedFace triangle = new RenderedFace(face, transform);
            texturedFiller.setTriangle(triangle);

            triangler.draw(triangle);
        }
    }
}
