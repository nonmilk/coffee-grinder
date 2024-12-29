package io.github.nonmilk.coffee.grinder.math.affine;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dd-buntar {@link https://github.com/dd-buntar}
 */
public class Transformation implements AffineTransformation, DataList<AffineTransformation> {
    private final List<AffineTransformation> affineTransformations = new ArrayList<>();
    private boolean isCalculated = false;
    private Matrix4 trsMatrix;

    public Transformation(AffineTransformation... ats) {
        for (AffineTransformation at : ats) {
            affineTransformations.add(at);
        }
    }

    public Transformation() {
    }

    public boolean isCalculated() {
        return isCalculated;
    }

    @Override
    public Matrix4 getMatrix() {
        if (isCalculated()) {
            return trsMatrix;
        }

        trsMatrix = new Mat4(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);

        for (AffineTransformation at : affineTransformations) {
            trsMatrix = Mat4Math.prod(trsMatrix, at.getMatrix());
        }
        isCalculated = true;
        return trsMatrix;
    }

    @Override
    public void add(AffineTransformation at) {
        affineTransformations.add(at);
        isCalculated = false;
    }

    @Override
    public void remove(int index) {
        affineTransformations.remove(index);
        isCalculated = false;
    }

    @Override
    public void remove(AffineTransformation at) {
        affineTransformations.remove(at);
        isCalculated = false;
    }

    @Override
    public void set(int index, AffineTransformation at) {
        affineTransformations.set(index, at);
        isCalculated = false;
    }

    @Override
    public AffineTransformation get(int index) {
        return affineTransformations.get(index);
    }
}
