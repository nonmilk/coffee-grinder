package io.github.nonmilk.coffee.grinder.math.affine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;

/**
 * @author {@link https://github.com/dd-buntar}
 */
public class TestTransformMatrices {

    @Test
    public void testScaleMatrix() {
        Matrix4 expectedMatrix = new Mat4(
                2, 0, 0, 0,
                0, 3, 0, 0,
                0, 0, 5, 0,
                0, 0, 0, 1);

        Scaling s = new Scaling(2, 3, 5);
        Assertions.assertTrue(Mat4Math.equalsEpsilon(expectedMatrix, s.getMatrix(), 0.00001F));
    }

    @Test
    public void testRotateMatrix() {
        Matrix4 expectedMatrix = new Mat4(
                0, 0, 1, 0,
                0, -1, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 1);

        AffineTransformation affineTransformation = new Transformation(
                new Rotator(90, Rotator.Axis.X),
                new Rotator(90, Rotator.Axis.Y),
                new Rotator(90, Rotator.Axis.Z));
        Assertions.assertTrue(Mat4Math.equalsEpsilon(expectedMatrix, affineTransformation.getMatrix(), 0.00001F));
    }

    @Test
    public void testTranslateMatrix() {
        Matrix4 expectedMatrix = new Mat4(
                1, 0, 0, 5,
                0, 1, 0, -2,
                0, 0, 1, 3,
                0, 0, 0, 1);

        Translator t = new Translator(5, -2, 3);
        Assertions.assertTrue(Mat4Math.equalsEpsilon(expectedMatrix, t.getMatrix(), 0.00001F));
    }
}
