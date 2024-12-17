package io.github.nonmilk.coffee.grinder;

import io.github.nonmilk.coffee.grinder.render.Scene;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    private GraphicsContext ctx;
    private Scene scene;

    public Renderer(GraphicsContext ctx) {
        this.ctx = ctx;
    }

    public Scene scene() {
        return scene;
    }

    public void render() {
        
    }
}