package io.github.nonmilk.coffee.grinder.math.affine;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;

import java.util.Objects;

/**
 * @author dd-buntar {@link https://github.com/dd-buntar}
 */
public class Rotator implements AffineTransformation {
    private float angle;
    private Axis axis;

    public enum Axis {
        X, Y, Z;
    }

    public Rotator(float rangle, Rotator.Axis axis) {
        this.angle = rangle;
        this.axis = axis;
    }

    public Rotator(int dangle, Rotator.Axis axis) {
        this((float) Math.toRadians(dangle), axis);
    }

    public Rotator(Rotator.Axis axis) {
        this(0f, axis);
    }

    @Override
    public Matrix4 getMatrix() {
        float cosA = (float) Math.cos(angle);
        float sinA = (float) Math.sin(angle);

        switch (axis) {
            case X -> {
                return new Mat4(
                        1, 0, 0, 0,
                        0, cosA, -sinA, 0,
                        0, sinA, cosA, 0,
                        0, 0, 0, 1);
            }
            case Y -> {
                return new Mat4(
                        cosA, 0, sinA, 0,
                        0, 1, 0, 0,
                        -sinA, 0, cosA, 0,
                        0, 0, 0, 1);
            }
            case Z -> {
                return new Mat4(
                        cosA, -sinA, 0, 0,
                        sinA, cosA, 0, 0,
                        0, 0, 1, 0,
                        0, 0, 0, 1);
            }
            default -> {
                return Mat4Math.unitMat();
            }
        }
    }

    public void setAxis(Axis newAxis) {
        this.axis = newAxis;
    }

    public void setAngle(float rad) {
        this.angle = rad;
    }

    public void setAngle(int deg) {
        this.angle = (float) Math.toRadians(deg);
    }

    public void setRelative(float dRad) {
        this.angle += dRad;
    }

    public void setRelative(int dDeg) {
        this.angle += (float) Math.toRadians(dDeg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rotator rotator = (Rotator) o;
        return Float.compare(angle, rotator.angle) == 0 && axis == rotator.axis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angle, axis);
    }
}
