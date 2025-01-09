package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.Objects;

import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.camera.Camera;
import io.github.nonmilk.coffee.grinder.math.UnitVec3f;

public class Lighting {
    private float baseBrightness;
    private Vector3 ray;
    private Camera camera;

    public Lighting(final float baseBrightness) {
        this(baseBrightness, new UnitVec3f(1, 0, 0));
    }

    public Lighting(final float baseBrightness, final UnitVec3f ray) {
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

    public void setCamera(Camera camera) {
        this.camera = Objects.requireNonNull(camera);
    }

    public Vector3 ray() {
        return ray;
    }

    public void setRay(final Vector3 ray) {
        this.ray = Objects.requireNonNull(ray);
    }

    public float lightness(Vector3 normal) {
        float normalLightness = -Vec3Math.dot(normal, ray);
        Vector3 reflection = reflect(normal, ray);
        Vector3 lookDir = Vec3Math.normalized(camera.orientation().lookDir());
        float specularLightness = (float) Math.pow(Math.max(0, Vec3Math.dot(lookDir, reflection)), 16);
        return baseBrightness + (1 - baseBrightness) * normalLightness + specularLightness * 0.2f;
    }

    private Vector3 reflect(Vector3 normal, Vector3 ray) {
        return Vec3Math.subtracted(ray, Vec3Math.multiplied(normal, 2 * Vec3Math.dot(ray, normal)));
    }
}
