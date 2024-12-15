package io.github.nonmilk.coffee.grinder.camera.view;

// FIXME javadoc

/**
 * The dimensions of an orthographic projection.
 */
public final class OrthographicView {

    private float width;
    private float height;

    /**
     * Constructs an {@code OrthographicView} with the specified width and
     * height.
     *
     * @param width  screen width. Must be positive
     * @param height screen height. Must be positive
     * @throws IllegalArgumentException If width or height is not positive
     */
    public OrthographicView(final float width, final float height) {
        setWidth(width);
        setHeight(height);
    }

    public float width() {
        return width;
    }

    public void setWidth(final float width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width has to be more than zero");
        }

        this.width = width;
    }

    public float height() {
        return height;
    }

    public void setHeight(final float height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height has to be more than zero");
        }

        this.height = height;
    }
}
