package io.github.nonmilk.coffee.grinder.camera.view;

// FIXME javadoc

/**
 * "Lens" parameters for a perspective camera.
 */
public final class PerspectiveView {

    private float fov;
    private float aspectRatio;

    /**
     * Constructs a {@code PerspectiveView} with the specified field of view
     * and aspect ratio.
     *
     * @param fov         field of view in radians. Must be in (0; 2pi)
     * @param aspectRatio width / height ratio. Must be positive
     * @throws IllegalArgumentException If {@code fov} is not in (0; 2pi) or
     *                                  {@code aspectRatio} is not positive
     */
    public PerspectiveView(final float fov, final float aspectRatio) {
        setFOV(fov);
        setAspectRatio(aspectRatio);
    }

    public float fov() {
        return fov;
    }

    public void setFOV(final float fov) {
        if (fov <= 0 || fov >= Math.TAU) {
            throw new IllegalArgumentException("fov has to be in (0; 2pi)");
        }

        this.fov = fov;
    }

    public float aspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(final float aspectRatio) {
        if (aspectRatio <= 0) {
            throw new IllegalArgumentException("aspect ratio has to be positive");
        }

        this.aspectRatio = aspectRatio;
    }
}
