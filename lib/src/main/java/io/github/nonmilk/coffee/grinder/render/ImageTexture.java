package io.github.nonmilk.coffee.grinder.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import io.github.shimeoki.jfx.rasterization.Colorf;

public final class ImageTexture implements Texture {

    private final BufferedImage img;

    private final int width;
    private final int height;

    private int argb;
    private int x;
    private int y;

    private float alpha;
    private float red;
    private float green;
    private float blue;

    public ImageTexture(final BufferedImage img) {
        this.img = Objects.requireNonNull(img);

        width = img.getWidth();
        height = img.getHeight();
    }

    @Override
    public Colorf pixelColor(final float x, final float y) {
        if (x < -1 || y < -1 || x > 1 || y > 1) {
            throw new IllegalArgumentException("x, y has to be in [-1, 1]");
        }

        this.x = (int) Math.floor(Math.abs(x) * width);
        this.y = (int) Math.floor(Math.abs(y) * height);

        argb = img.getRGB(this.x, this.y);

        alpha = (float) ((argb & 0xff000000) >>> 24) / 255f;
        red = (float) ((argb & 0x00ff0000) >> 16) / 255f;
        green = (float) ((argb & 0x0000ff00) >> 8) / 255f;
        blue = (float) (argb & 0x000000ff) / 255f;

        return new Colorf(red, green, blue, alpha);
    }

    public ImageTexture fromFile(final File f) {
        final BufferedImage img;
        try {
            img = ImageIO.read(Objects.requireNonNull(f));
        } catch (final IOException e) {
            throw new IllegalArgumentException("file is not a valid image");
        }

        return new ImageTexture(img);
    }
}
