package io.github.nonmilk.coffee.grinder.render;

import io.github.shimeoki.jfx.rasterization.Colorf;

public class ColorTexture implements Texture {
    private final Colorf color;

    public ColorTexture(Colorf color) {
        this.color = color;
    }

    @Override
    public Colorf pixelColor(final float x, final float y) {
        return color;
    }
}
