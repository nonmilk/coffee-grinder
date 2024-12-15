package io.github.nonmilk.coffee.grinder.camera.view;

/**
 * "Lens" parameters for a perspective camera.
 * 
 * @param fov         field of view in radians. Must be in (0; 2pi)
 * @param aspectRatio width / height ratio. Must be positive
 */
public record PerspectiveView(float fov, float aspectRatio) {

    /**
     * Constructs a {@code PerspectiveView} record with the specified field of view
     * and aspect ratio.
     *
     * @param fov         field of view in radians. Must be in (0; 2pi)
     * @param aspectRatio width / height ratio. Must be positive
     * @throws IllegalArgumentException If {@code fov} is not in (0; 2pi) or
     *                                  {@code aspectRatio} is not positive
     */
    public PerspectiveView {
        if (fov <= 0 || fov >= Math.TAU) {
            throw new IllegalArgumentException("fov has to be in (0; 2pi)");
        }
        if (aspectRatio <= 0) {
            throw new IllegalArgumentException("aspect ratio has to be positive");
        }
    }
}
