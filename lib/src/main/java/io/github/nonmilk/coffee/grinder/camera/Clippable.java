package io.github.nonmilk.coffee.grinder.camera;

/**
 * An interface describing an entity with a clipping box (usually a camera)
 */
public interface Clippable {
    /**
     * Returns a clipping box.
     * 
     * @return {@ClippingBox} clipping box
     */
    public ClippingBox box();
}
