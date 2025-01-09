package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.render.*;
import io.github.shimeoki.jfx.rasterization.IntBresenhamTriangler;
import io.github.shimeoki.jfx.rasterization.Triangler;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Objects;

public class RenderingPipeline {

    private final Triangler triangler;
    private final TexturedFiller texturedFiller;
    private final ZBuffer zBuffer;
    private final GraphicsContext ctx;

    private int prevWidth;
    private int prevHeight;

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
        final Camera selectedCamera = scene.camera();
        // updates every frame, can be done externally
        // I'll leave it here until we find the best strategy
        // for updating it
        selectedCamera.orientation().lookAt();

        final ScreenTransform transform = new ScreenTransform(selectedCamera, ctx);
        final var width = (int) transform.width();
        final var height = (int) transform.height();

        if (width <= 0 || height <= 0) {
            return;
        }

        if (prevWidth != width || prevHeight != height) {
            zBuffer.setDimensions(width, height);
            prevWidth = width;
            prevHeight = height;
        }

        zBuffer.clear();
        scene.lightFromCamera();
        texturedFiller.setLighting(scene.lighting());

        for (Model model : scene.models()) {
            transform.setModel(model.matrix());
            texturedFiller.setTexture(model.texture());
            texturedFiller.setTransform(transform);
            renderModel(model, transform);
        }
    }

    private void renderModel(Model model, ScreenTransform transform) {
        for (MeshFace meshFace : model.meshFaces()) {
            RenderedFace triangle = new RenderedFace(meshFace, transform);
            texturedFiller.setTriangle(triangle);

            triangler.draw(triangle);
        }
    }
}
