package io.guthub.nonmilk.coffee.camera;

/**
 * The dimensions of an orthographic projection.
 * @param width screen width. Must be positive
 * @param height screen height. Must be positive
 */
public record OrthographicView(float width, float height) {

    /**
     * Constructs an {@code OrthographicView} record with the specified width and height.
     * @param width screen width. Must be positive
     * @param height screen height. Must be positive
     * @throws IllegalArgumentException If width or height is not positive
     */
    public OrthographicView {
        if (width <= 0) {
            throw new IllegalArgumentException("Width has to be more than zero");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("Height has to be more than zero");
        }
    }

}
