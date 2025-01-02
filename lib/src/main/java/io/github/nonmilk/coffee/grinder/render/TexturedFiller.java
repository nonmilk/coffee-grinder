package io.github.nonmilk.coffee.grinder.render;

import java.util.Objects;

import io.github.alphameo.linear_algebra.vec.Vector2;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.render.triangle.Lighting;
import io.github.shimeoki.jfx.rasterization.Colorf;
import io.github.shimeoki.jfx.rasterization.Point2i;
import io.github.shimeoki.jfx.rasterization.triangle.Filler;
import io.github.shimeoki.jfx.rasterization.triangle.Barycentrics;
import javafx.scene.effect.Light;

public class TexturedFiller implements Filler {
    private final ZBuffer zBuffer;
    private Lighting lighting;
    private Texture texture;

    private boolean useTexture = true;
    private boolean useLighting = true;

    private RenderedFace renderedFace;

    public TexturedFiller(final ZBuffer zBuffer) {
        this.zBuffer = Objects.requireNonNull(zBuffer);
    }

    public void setTexture(Texture texture) {
        this.texture = Objects.requireNonNull(texture);
    }

    public void setLighting(Lighting lighting) {
        this.lighting = Objects.requireNonNull(lighting);
    }

    private boolean canDraw(final Barycentrics triangleBarycentrics) {
        final int x = (int) Math.round(renderedFace.shape().barycentricX(triangleBarycentrics));
        final int y = (int) Math.round(renderedFace.shape().barycentricY(triangleBarycentrics));
        final float z = renderedFace.shape().barycentricZ(triangleBarycentrics);

        return zBuffer.draw(x, y, z);
    }

    private Colorf colorAtBarycentric(final Barycentrics triangleBarycentrics) {
        // FIXME handle null uv-s
        final Vector2 uv = renderedFace.uv().barycentricUV(triangleBarycentrics);
        final float x = uv.x();
        final float y = uv.y();

        return texture.pixelColor(x, y);
    }

    public void setTriangle(final RenderedFace renderedFace) {
        this.renderedFace = Objects.requireNonNull(renderedFace);
    }

    @Override
    public Colorf color(final Barycentrics b, final Point2i p) {
        Objects.requireNonNull(b);

        if (!canDraw(b) || !useTexture) {
            return null;
        }

        // black outline for debugging
        // if (lambda1 < 0.02 || lambda2 < 0.02 || lambda3 < 0.02) {
        // return HTMLColorf.BLACK;
        // }

        final Colorf resultColor;
        final Colorf colorAtBarycentric = colorAtBarycentric(b);

        if (useLighting && lighting != null) {
            final Vector3 normal = renderedFace.normal().barycentricNormal(b);
            float lightness = lighting.lightness(normal);
            resultColor = new Colorf(
                    colorAtBarycentric.red() * lightness,
                    colorAtBarycentric.green() * lightness,
                    colorAtBarycentric.blue() * lightness,
                    colorAtBarycentric.alpha());
        } else {
            resultColor = colorAtBarycentric;
        }

        return resultColor;
    }
}
