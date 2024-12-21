package io.github.nonmilk.coffee.grinder.render;

import java.util.Objects;

import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.shimeoki.jfx.rasterization.geom.Point2f;
import io.github.shimeoki.jfx.rasterization.triangle.geom.Triangle;

public class TexturedTriangle implements Triangle {

    private final Vec3f v1;
    private final Vec3f v2;
    private final Vec3f v3;

    /**
     * Creates a new {@code TexturedTriangle} instance.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @param v3 the third vertex
     *
     * @throws NullPointerException if at least one parameter is {@code null}
     *
     */
    public TexturedTriangle(final Vec3f v1, final Vec3f v2, final Vec3f v3) {
        this.v1 = Objects.requireNonNull(v1);
        this.v2 = Objects.requireNonNull(v2);
        this.v3 = Objects.requireNonNull(v3);
    }

    public float barycentricX(float lambda1, float lambda2, float lambda3) {
        return v1.x() * lambda1 + v2.x() * lambda2 + v3().x() * lambda3;
    }

    public float barycentricY(float lambda1, float lambda2, float lambda3) {
        return v1.y() * lambda1 + v2.y() * lambda2 + v3().y() * lambda3;
    }

    public float barycentricZ(float lambda1, float lambda2, float lambda3) {
        return v1.z() * lambda1 + v2.z() * lambda2 + v3.z() * lambda3;
    }

    @Override
    public Point2f v1() {
        return v1;
    }

    @Override
    public Point2f v2() {
        return v2;
    }

    @Override
    public Point2f v3() {
        return v3;
    }
}