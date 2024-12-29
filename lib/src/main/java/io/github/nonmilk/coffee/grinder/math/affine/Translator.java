package io.github.nonmilk.coffee.grinder.math.affine;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.mat.Matrix4Col;
import io.github.alphameo.linear_algebra.mat.Matrix4Row;

import static io.github.alphameo.linear_algebra.mat.Matrix4Col.*;
import static io.github.alphameo.linear_algebra.mat.Matrix4Row.*;

import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vector3;

import java.util.Objects;

/**
 * @author dd-buntar {@link https://github.com/dd-buntar}
 */
public class Translator implements AffineTransformation {
    private final Matrix4 translateMatrix;

    public Translator(float tx, float ty, float tz) {
        this.translateMatrix = new Mat4(
                1, 0, 0, tx,
                0, 1, 0, ty,
                0, 0, 1, tz,
                0, 0, 0, 1);
    }

    public Translator() {
        this.translateMatrix = new Mat4(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    @Override
    public Matrix4 getMatrix() {
        return translateMatrix;
    }

    @Override
    public Vector3 transform(Vector3 v) {
        return new Vec3(
                translateMatrix.get(R0, C3) + v.x(),
                translateMatrix.get(R1, C3) + v.y(),
                translateMatrix.get(R2, C3) + v.z());
    }

    public void setX(float tx) {
        translateMatrix.set(Matrix4Row.R0, Matrix4Col.C3, tx);
    }

    public void setY(float ty) {
        translateMatrix.set(Matrix4Row.R1, Matrix4Col.C3, ty);
    }

    public void setZ(float tz) {
        translateMatrix.set(Matrix4Row.R2, Matrix4Col.C3, tz);
    }

    public void set(float tx, float ty, float tz) {
        setX(tx);
        setY(ty);
        setZ(tz);
    }

    public void setRelative(float dtx, float dty, float dtz) {
        set(
                translateMatrix.get(Matrix4Row.R0, Matrix4Col.C3) + dtx,
                translateMatrix.get(Matrix4Row.R1, Matrix4Col.C3) + dty,
                translateMatrix.get(Matrix4Row.R2, Matrix4Col.C3) + dtz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Translator that = (Translator) o;
        return Objects.equals(translateMatrix, that.translateMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translateMatrix);
    }
}
