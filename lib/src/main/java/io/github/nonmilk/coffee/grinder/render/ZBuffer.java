package io.github.nonmilk.coffee.grinder.render;

import java.util.Arrays;

public class ZBuffer {
    private float[] depths;
    private int width;
    private int height;

    public ZBuffer(final int width, final int height) {
        setDimensions(width, height);
    }

    public void setDimensions(final int width, final int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions invalid");
        }

        this.width = width;
        this.height = height;

        // Assumes data doesn't need to be copied
        depths = new float[width * height];
    }

    public boolean draw(int x, int y, float z) {
        if (x >= width || y >= height || x < 0 || y < 0) {
            throw new IllegalArgumentException("Accessing coordinate outside of dimensions");
        }

        int posInArray = y * width + x;

        if (depths[posInArray] <= z) {
            return false;
        }

        depths[posInArray] = z;
        return true;
    }

    public void clear() {
        Arrays.fill(depths, 2);
    }
}
