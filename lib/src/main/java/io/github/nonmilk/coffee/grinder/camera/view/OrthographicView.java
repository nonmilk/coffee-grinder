package io.github.nonmilk.coffee.grinder.camera.view;

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

    /**
     * Returns orthographic camera's projection width.
     * 
     * @return {@code float} orthographic camera's projection width
     */
    public float width() {
        return width;
    }

    /**
     * Sets orthographic camera's projection width. Has to be positive.
     * 
     * @param width orthographic camera's projection width
     * @throws IllegalArgumentException if width is not positive
     */
    public void setWidth(final float width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width has to be more than zero");
        }

        this.width = width;
    }

    /**
     * Returns orthographic camera's projection height.
     * 
     * @return {@code float} orthographic camera's projection height
     */
    public float height() {
        return height;
    }

    /**
     * Sets orthographic camera's projection height. Has to be positive.
     * 
     * @param height orthographic camera's projection height
     * @throws IllegalArgumentException if height is not positive
     */
    public void setHeight(final float height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height has to be more than zero");
        }

        this.height = height;
    }
}
