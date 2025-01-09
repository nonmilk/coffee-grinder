package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.render.Scene;
import io.github.shimeoki.jshaper.obj.Triplet;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class Renderer {
    private RenderingPipeline renderingPipeline;
    private Scene scene;

    public Renderer(GraphicsContext ctx) {
        renderingPipeline = new RenderingPipeline(ctx);
    }

    public Scene scene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void render() {
        renderingPipeline.renderScene(scene);
    }

    public List<Triplet> select(int x, int y, int width, int height) {
        return renderingPipeline.select(x, y, width, height);
    }

    public void setDrawWireframe(boolean drawWireframe) {
        renderingPipeline.setDrawWireframe(drawWireframe);
    }

    public void setDrawTexture(boolean drawTexture) {
        renderingPipeline.setDrawTexture(drawTexture);
    }

    public void setDrawLighting(boolean drawLighting) {
        renderingPipeline.setDrawLighting(drawLighting);
    }
}
