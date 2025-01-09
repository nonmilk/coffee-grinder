package io.github.nonmilk.coffee.grinder.render;

import java.util.*;

import io.github.alphameo.linear_algebra.vec.Vector2;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.render.triangle.Lighting;
import io.github.shimeoki.jfx.rasterization.Colorf;
import io.github.shimeoki.jfx.rasterization.HTMLColorf;
import io.github.shimeoki.jfx.rasterization.Point2i;
import io.github.shimeoki.jfx.rasterization.triangle.Filler;
import io.github.shimeoki.jfx.rasterization.triangle.Barycentrics;
import io.github.shimeoki.jshaper.obj.Triplet;
import io.github.shimeoki.jshaper.obj.Vertex;
import javafx.scene.effect.Light;

public class TexturedFiller implements Filler {
    private final ZBuffer zBuffer;
    private Lighting lighting;
    private Texture texture;
    private ScreenTransform transform;
    private final Map<Triplet, Vec3f> renderedVertices = new HashMap<>();
    public Set<Vertex> selected = new HashSet<>();

    private boolean useTexture = true;
    private boolean useLighting = true;

    private RenderedFace renderedFace;

    public TexturedFiller(final ZBuffer zBuffer) {
        this.zBuffer = Objects.requireNonNull(zBuffer);
    }

    public void setUseTexture(boolean useTexture) {
        this.useTexture = useTexture;
    }

    public void setUseLighting(boolean useLighting) {
        this.useLighting = useLighting;
    }

    public void setTexture(Texture texture) {
        this.texture = Objects.requireNonNull(texture);
    }

    public void setLighting(Lighting lighting) {
        this.lighting = Objects.requireNonNull(lighting);
    }

    public void setTransform(ScreenTransform transform) {
        this.transform = Objects.requireNonNull(transform);
    }

    public void resetVertices() {
        renderedVertices.clear();
    }

    public Map<Triplet, Vec3f> renderedVertices() {
        return renderedVertices;
    }

    public void setSelected(Set<Vertex> selected) {
        this.selected = selected;
    }

    private boolean canDraw(final Barycentrics triangleBarycentrics, final Point2i p) {
        final int x = p.x();
        final int y = p.y();
        final float z = renderedFace.shape().barycentricZ(triangleBarycentrics);
        if (z > 1 || z < -1 || x > transform.width() || x < 0 || y > transform.height() || y < 0) {
            return false;
        }

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

        if (!canDraw(b, p) || !useTexture) {
            return null;
        }

        // show vertices
        if (b.lambda1() > 0.9) {
            final Triplet triplet = renderedFace.shape().face().v1();
            renderedVertices.putIfAbsent(triplet, renderedFace.shape().v1());
            if (selected.contains(triplet.vertex())) {
                return HTMLColorf.YELLOW;
            }
        }

        if (b.lambda2() > 0.9) {
            final Triplet triplet = renderedFace.shape().face().v2();
            renderedVertices.putIfAbsent(triplet, renderedFace.shape().v2());
            if (selected.contains(triplet.vertex())) {
                return HTMLColorf.YELLOW;
            }
        }

        if (b.lambda3() > 0.9) {
            final Triplet triplet = renderedFace.shape().face().v3();
            renderedVertices.putIfAbsent(triplet, renderedFace.shape().v3());
            if (selected.contains(triplet.vertex())) {
                return HTMLColorf.YELLOW;
            }
        }

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
