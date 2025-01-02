package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.render.Scene;
import javafx.scene.canvas.GraphicsContext;

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
}
