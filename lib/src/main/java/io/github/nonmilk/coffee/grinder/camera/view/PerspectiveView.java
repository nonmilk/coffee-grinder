package io.github.nonmilk.coffee.grinder.camera.view;

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

    /*
     * Returns field of view, in radians. The value is in (0; 2pi).
     * 
     * @return {@code float} field of view, in radians
     */
    public float fov() {
        return fov;
    }

    /*
     * Sets field of view, in radians. The value has to be in (0; 2pi).
     * 
     * @param {@code float} field of view, in radians
     * 
     * @throws IllegalArgumentException, if fov is outside of (0; 2pi)
     */
    public void setFOV(final float fov) {
        if (fov <= 0 || fov >= Math.TAU) {
            throw new IllegalArgumentException("fov has to be in (0; 2pi)");
        }

        this.fov = fov;
    }

    /*
     * Returns aspect ratio, defined as width / height.
     * 
     * @return {@code float} aspect ratio
     */
    public float aspectRatio() {
        return aspectRatio;
    }

    /*
     * Sets aspect ratio, defined as width / height. Has to be positive.
     * 
     * @param {@code float} aspect ratio
     * 
     * @throws IllegalArgumentException, if aspect ratio is not positive
     */
    public void setAspectRatio(final float aspectRatio) {
        if (aspectRatio <= 0) {
            throw new IllegalArgumentException("aspect ratio has to be positive");
        }

        this.aspectRatio = aspectRatio;
    }
}
