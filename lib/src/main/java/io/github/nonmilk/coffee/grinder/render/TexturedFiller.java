package io.github.nonmilk.coffee.grinder.render;

import java.util.Objects;

import io.github.shimeoki.jfx.rasterization.color.Colorf;
import io.github.shimeoki.jfx.rasterization.color.HTMLColorf;
import io.github.shimeoki.jfx.rasterization.triangle.color.TriangleFiller;
import io.github.shimeoki.jfx.rasterization.triangle.geom.TriangleBarycentrics;

public class TexturedFiller implements TriangleFiller {
    private final Colorf color1;
    private final Colorf color2;
    private final Colorf color3;

    private final ZBuffer zBuffer;
    private float baseBrightness;

    private float lambda1;
    private float lambda2;
    private float lambda3;

    private float red1;
    private float red2;
    private float red3;

    private float green1;
    private float green2;
    private float green3;

    private float blue1;
    private float blue2;
    private float blue3;

    private float alpha1;
    private float alpha2;
    private float alpha3;
    private TriangleBarycentrics triangleBarycentrics;

    private RenderedFace renderedFace;

    /**
     * Creates a new {@link TexturedFiller} instance.
     *
     * @param zBuffer z-buffer
     *
     * @throws NullPointerException if z-buffer is {@code null}
     */
    public TexturedFiller(final ZBuffer zBuffer, final float baseBrightness) {
        // FIXME use triangle textures
        this.zBuffer = Objects.requireNonNull(zBuffer);
        color1 = HTMLColorf.RED;
        color2 = HTMLColorf.LIME;
        color3 = HTMLColorf.BLUE;
        setBrightness(baseBrightness);
    }

    public float baseBrightness() {
        return baseBrightness;
    }

    public void setBrightness(float brightness) {
        if (brightness < 0 || brightness > 1) {
            throw new IllegalArgumentException("Brightness has to be in [0, 1]");
        }

        this.baseBrightness = brightness;
    }

    private float red() {
        red1 = lambda1 * color1.red();
        red2 = lambda2 * color2.red();
        red3 = lambda3 * color3.red();

        return red1 + red2 + red3;
    }

    private float green() {
        green1 = lambda1 * color1.green();
        green2 = lambda2 * color2.green();
        green3 = lambda3 * color3.green();

        return green1 + green2 + green3;
    }

    private float blue() {
        blue1 = lambda1 * color1.blue();
        blue2 = lambda2 * color2.blue();
        blue3 = lambda3 * color3.blue();

        return blue1 + blue2 + blue3;
    }

    private float alpha() {
        int x = (int) Math.round(renderedFace.positioning().barycentricX(triangleBarycentrics));
        int y = (int) Math.round(renderedFace.positioning().barycentricY(triangleBarycentrics));
        float z = renderedFace.positioning().barycentricZ(triangleBarycentrics);

        if (!zBuffer.draw(x, y, z)) {
            return -1;
        }

        alpha1 = lambda1 * color1.alpha();
        alpha2 = lambda2 * color2.alpha();
        alpha3 = lambda3 * color3.alpha();

        return alpha1 + alpha2 + alpha3;
    }

    public void setTriangle(final RenderedFace renderedFace) {
        this.renderedFace = Objects.requireNonNull(renderedFace);
    }

    @Override
    public Colorf color(final TriangleBarycentrics b) {
        Objects.requireNonNull(b);

        lambda1 = b.lambda1();
        lambda2 = b.lambda2();
        lambda3 = b.lambda3();
        triangleBarycentrics = b;

        float alpha = alpha();
        if (alpha < 0) {
            return null;
        }

        // black outline for debugging
        // if (lambda1 < 0.02 || lambda2 < 0.02 || lambda3 < 0.02) {
        //     return HTMLColorf.BLACK;
        // }

        float normalLightness = renderedFace.normal().lightness(b, null);
        float lightness = baseBrightness + (1 - baseBrightness) * normalLightness;

        return new Colorf(red() * lightness, green() * lightness, blue() * lightness, alpha);
    }
}
