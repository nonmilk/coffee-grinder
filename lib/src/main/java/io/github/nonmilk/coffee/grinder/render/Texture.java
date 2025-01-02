package io.github.nonmilk.coffee.grinder.render;

import io.github.shimeoki.jfx.rasterization.Colorf;

public interface Texture {
    public Colorf pixelColor(final float x, final float y);
}
