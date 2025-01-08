package io.github.nonmilk.coffee.grinder.render;

import java.awt.*;
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

    public ImageTexture(final BufferedImage img) {
        Objects.requireNonNull(img);

        width = img.getWidth();
        height = img.getHeight();

        BufferedImage inverted = new BufferedImage(width, height, img.getType());

        Graphics2D g = inverted.createGraphics();
        // Flip image horizontally and vertically
        g.drawImage(img, 0, 0, width, height, width, height, 0, 0, null);
        g.dispose();

        this.img = inverted;
    }

    @Override
    public Colorf pixelColor(final float x, final float y) {
        if (x < -1 || y < -1 || x > 1 || y > 1) {
            throw new IllegalArgumentException("x, y has to be in [-1, 1]");
        }

        final int imgX = (int) (Math.abs(x) * width);
        final int imgY = (int) (Math.abs(y) * height);

        final int argb = img.getRGB(imgX, imgY);

        final float alpha = (float) ((argb & 0xff000000) >>> 24) / 255f;
        final float red = (float) ((argb & 0x00ff0000) >> 16) / 255f;
        final float green = (float) ((argb & 0x0000ff00) >> 8) / 255f;
        final float blue = (float) (argb & 0x000000ff) / 255f;

        return new Colorf(red, green, blue, alpha);
    }

    public static ImageTexture fromFile(final File f) {
        final BufferedImage img;
        try {
            img = ImageIO.read(Objects.requireNonNull(f));
        } catch (final IOException e) {
            throw new IllegalArgumentException("file is not a valid image");
        }

        return new ImageTexture(img);
    }
}
