package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.render.*;
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
    private final GraphicsContext ctx;

    public RenderingPipeline(final GraphicsContext ctx) {
        this.ctx = Objects.requireNonNull(ctx);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        this.zBuffer = new ZBuffer(screenWidth, screenHeight);
        triangler = new IntBresenhamTriangler(ctx);

        this.texturedFiller = new TexturedFiller(zBuffer);
        triangler.setFiller(texturedFiller);
    }

    public void renderScene(Scene scene) {
        final Camera selectedCamera = scene.selectedCamera();
        // updates every frame, can be done externally
        // I'll leave it here until we find the best strategy
        // for updating it
        selectedCamera.orientation().lookAt();

        zBuffer.clear();
        scene.lightFromCamera();
        texturedFiller.setLighting(scene.lighting());

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
