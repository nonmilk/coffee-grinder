package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.Objects;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.shimeoki.jfx.rasterization.math.Floats;
import io.github.shimeoki.jfx.rasterization.triangle.geom.TriangleBarycentrics;

public class Normal {
    private Vector3 v1n;
    private Vector3 v2n;
    private Vector3 v3n;

    public Normal(final Vector3 v1n, final Vector3 v2n, final Vector3 v3n) {
        checkNormalized(v1n);
        checkNormalized(v2n);
        checkNormalized(v3n);

        this.v1n = v1n;
        this.v2n = v2n;
        this.v3n = v3n;
    }

    public float lightness(final TriangleBarycentrics barycentrics, final Vector3 ray) {
        checkNormalized(ray);
        Objects.requireNonNull(barycentrics);
        
        final float l1 = barycentrics.lambda1();
        final float l2 = barycentrics.lambda2();
        final float l3 = barycentrics.lambda3();

        final float x = v1n.x() * l1 + v2n.x() * l2 + v3n.x() * l3;
        final float y = v1n.y() * l1 + v2n.y() * l2 + v3n.y() * l3;
        final float z = v1n.z() * l1 + v2n.z() * l2 + v3n.z() * l3;

        // manual dot product to avoid Vec3f creation
        return -ray.x() * x - ray.y() * y - ray.z() * z;
    }

    public Vector3 v1n() {
        return v1n;
    }

    public Vector3 v2n() {
        return v2n;
    }

    public Vector3 v3n() {
        return v3n;
    }

    public void setV1n(Vector3 v1n) {
        checkNormalized(v1n);
        this.v1n = v1n;
    }

    public void setV2n(Vector3 v2n) {
        checkNormalized(v2n);
        this.v2n = v2n;
    }

    public void setV3n(Vector3 v3n) {
        checkNormalized(v3n);
        this.v3n = v3n;
    }

    private static void checkNormalized(Vector3 normal) {
        Objects.requireNonNull(normal);

        // length check, likely hurts performance, uncomment if needed
        /*
        if (!Floats.equals(1, Vec3Math.len2(normal))) {
            throw new IllegalArgumentException("Input is not normalized");
        }
        */
    }
}
