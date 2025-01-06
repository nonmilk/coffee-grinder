package io.github.nonmilk.coffee.grinder.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import io.github.shimeoki.jfx.rasterization.Colorf;

public class ImageTexture implements Texture {

    private final BufferedImage img;

    private final int width;
    private final int height;

    public ImageTexture(final File f) {
        try {
            img = ImageIO.read(Objects.requireNonNull(f));
        } catch (final IOException e) {
            throw new IllegalArgumentException("file is not a valid image");
        }

        width = img.getWidth();
        height = img.getHeight();
    }

    @Override
    public Colorf pixelColor(final float x, final float y) {
        if (x < -1 || y < -1 || x > 1 || y > 1) {
            throw new IllegalArgumentException("x, y has to be in [-1, 1]");
        }

        final int clr = img.getRGB((int) Math.floor(Math.abs(x) * width), (int) Math.floor(Math.abs(y) * height));

        final float alpha = (float) ((clr & 0xff000000) >>> 24) / 255f;
        final float red = (float) ((clr & 0x00ff0000) >> 16) / 255f;
        final float green = (float) ((clr & 0x0000ff00) >> 8) / 255f;
        final float blue = (float) (clr & 0x000000ff) / 255f;

        return new Colorf(red, green, blue, alpha);
    }
}
