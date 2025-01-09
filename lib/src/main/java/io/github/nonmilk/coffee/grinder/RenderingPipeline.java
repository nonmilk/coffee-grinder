package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.render.*;
import io.github.shimeoki.jfx.rasterization.IntBresenhamTriangler;
import io.github.shimeoki.jfx.rasterization.Triangler;
import io.github.shimeoki.jshaper.obj.Triplet;
import io.github.shimeoki.jshaper.obj.Vertex;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RenderingPipeline {

    private final Triangler triangler;
    private final Wireframe wireframe;
    private final TexturedFiller texturedFiller;
    private final ZBuffer zBuffer;
    private final GraphicsContext ctx;
    private boolean drawWireframe = true;

    private int prevWidth;
    private int prevHeight;

    public RenderingPipeline(final GraphicsContext ctx) {
        this.ctx = Objects.requireNonNull(ctx);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        this.zBuffer = new ZBuffer(screenWidth, screenHeight);
        triangler = new IntBresenhamTriangler(ctx);
        wireframe = new Wireframe(ctx, zBuffer);

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

    public void setDrawWireframe(boolean drawWireframe) {
        this.drawWireframe = drawWireframe;
    }

    public void setDrawTexture(boolean drawTexture) {
        texturedFiller.setUseTexture(drawTexture);
    }

    public void setDrawLighting(boolean drawLighting) {
        texturedFiller.setUseLighting(drawLighting);
    }

    private void renderModel(Model model, ScreenTransform transform) {
        for (MeshFace meshFace : model.meshFaces()) {
            RenderedFace triangle = new RenderedFace(meshFace, transform);
            if (drawWireframe) {
                wireframe.renderedFaceWireframe(triangle);
            }
            texturedFiller.setTriangle(triangle);
            triangler.draw(triangle);
        }
    }

    public List<Triplet> select(int x, int y, int width, int height) {
        final Map<Triplet, Vec3f> renderedVertices = texturedFiller.renderedVertices();

        int xMax = x + width;
        int yMax = y + height;

        // Filter the vertices and collect them into a list
        List<Triplet> selected = renderedVertices.entrySet().stream()
                .filter(entry -> {
                    Vec3f position = entry.getValue(); // Access Vec3f from the value now
                    final int vX = Math.round(position.x());
                    final int vY = Math.round(position.y());
                    return vX >= x && vX <= xMax &&
                            vY >= y && vY <= yMax &&
                            Math.abs(position.z() - zBuffer.zAtCoords(vX, vY)) < 0.1;
                })
                .map(Map.Entry::getKey) // Map back to the Vertex (key now)
                .collect(Collectors.toList());


        Set<Vertex> selectedSet = selected.stream()
                .map(Triplet::vertex)
                .collect(Collectors.toSet());

        texturedFiller.setSelected(selectedSet);
        return selected;
    }
}
