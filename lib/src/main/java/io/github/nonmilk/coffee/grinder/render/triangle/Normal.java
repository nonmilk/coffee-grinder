package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.Objects;

// import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
// import io.github.shimeoki.jfx.rasterization.math.Floats;
import io.github.shimeoki.jfx.rasterization.triangle.geom.TriangleBarycentrics;

public class Normal {
    private final Vector3 v1n;
    private final Vector3 v2n;
    private final Vector3 v3n;

    public Normal(final Vector3 v1n, final Vector3 v2n, final Vector3 v3n) {
        Objects.requireNonNull(v1n);
        Objects.requireNonNull(v2n);
        Objects.requireNonNull(v3n);

        // length check, likely hurts performance, uncomment if needed
        /*
        if (!Floats.equals(1, Vec3Math.len2(v1n))) {
            throw new IllegalArgumentException("Normal v1n is not of length 1");
        }
        if (!Floats.equals(1, Vec3Math.len2(v2n))) {
            throw new IllegalArgumentException("Normal v2n is not of length 1");
        }
        if (!Floats.equals(1, Vec3Math.len2(v3n))) {
            throw new IllegalArgumentException("Normal v3n is not of length 1");
        }
        */

        this.v1n = v1n;
        this.v2n = v2n;
        this.v3n = v3n;
    }

    public float lightness(final TriangleBarycentrics barycentrics, final Vector3 ray) {
        Objects.requireNonNull(barycentrics);
        Objects.requireNonNull(ray);

        // length check, likely hurts performance, uncomment if needed
        /*
        if (!Floats.equals(1, Vec3Math.len2(v1n))) {
            throw new IllegalArgumentException("Light ray is not of length 1");
        }
        */
        
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
}
