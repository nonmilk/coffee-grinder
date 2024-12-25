package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.Objects;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.shimeoki.jfx.rasterization.math.Floats;

public class Lighting {
    private float baseBrightness;
    private Vector3 ray;

    public Lighting(final float baseBrightness) {
        this(baseBrightness, new Vec3f(1, 0, 0));
    }

    public Lighting(final float baseBrightness, final Vector3 ray) {
        setBrightness(baseBrightness);
        setRay(ray);
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

    public Vector3 ray() {
        return ray;
    }

    public void setRay(final Vector3 ray) {
        Objects.requireNonNull(ray);
        if (!Floats.equals(1, Vec3Math.len2(ray))) {
            throw new IllegalArgumentException("Light direction has to be normalized");
        }

        this.ray = ray;
    }

    public float lightness(Vector3 normal) {
        float normalLightness = -Vec3Math.dot(normal, ray);
        return baseBrightness + (1 - baseBrightness) * normalLightness;
    }
}
