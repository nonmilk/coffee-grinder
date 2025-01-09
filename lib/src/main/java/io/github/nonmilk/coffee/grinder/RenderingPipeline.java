package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.render.*;
import io.github.shimeoki.jfx.rasterization.IntBresenhamTriangler;
import io.github.shimeoki.jfx.rasterization.Triangler;
import io.github.shimeoki.jshaper.obj.Vertex;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
        texturedFiller.resetVertices();

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

    public List<Vertex> select(int x, int y, int width, int height) {
        final Map<Vec3f, Vertex> renderedVertices = texturedFiller.renderedVertices();

        int xMax = x + width;
        int yMax = y + height;

        // Filter the vertices and collect them into a list
        List<Vertex> selected = renderedVertices.entrySet().stream()
                .filter(entry -> {
                    Vec3f position = entry.getKey();
                    return position.x() >= x && position.x() <= xMax &&
                            position.y() >= y && position.y() <= yMax;
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        texturedFiller.setSelected(new HashSet<>(selected));
        return selected;
    }
}
