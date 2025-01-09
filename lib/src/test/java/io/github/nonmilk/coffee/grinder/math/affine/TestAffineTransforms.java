package io.github.nonmilk.coffee.grinder.math.affine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;

/**
 * @author {@link https://github.com/dd-buntar}
 */
public class TestAffineTransforms {
    @Test
    public void testScale() {
        Vector3 expectedVec = new Vec3(2, 3, 5);

        AffineTransformation affineTransformation = new Scaling(2, 3, 5);
        Vector3 vec = new Vec3(1, 1, 1);

        Assertions.assertTrue(Vec3Math.equalsEpsilon(expectedVec, affineTransformation.transform(vec), 0.00001F));
    }

    @Test
    public void testTranslate() {
        Vector3 expectedVec = new Vec3(9, -5, 2);

        AffineTransformation affineTransformation = new Translator(9, -5, 2);
        Vector3 vec = new Vec3(0, 0, 0);

        Assertions.assertTrue(Vec3Math.equalsEpsilon(expectedVec, affineTransformation.transform(vec), 0.00001F));
    }

    @Test
    public void testDefaultTransform() {
        Vector3 expectedVec = new Vec3(9, -5, 2);

        AffineTransformation affineTransformation = new Transformation();
        Vector3 vec = new Vec3(9, -5, 2);

        Assertions.assertTrue(Vec3Math.equalsEpsilon(expectedVec, affineTransformation.transform(vec), 0.00001F));
    }

    @Test
    public void testScaleTransform() {
        Vector3 expectedVec = new Vec3(9, -5, 2);

        AffineTransformation affineTransformation = new Transformation(new Scaling(9, -5, 2));
        Vector3 vec = new Vec3(1, 1, 1);

        Assertions.assertTrue(Vec3Math.equalsEpsilon(expectedVec, affineTransformation.transform(vec), 0.00001F));
    }

    @Test
    public void testRotateTransform() {
        Vector3 expectedVec = new Vec3(1, -1, 1);

        AffineTransformation affineTransformation = new Transformation(
                new Rotator(90, Rotator.Axis.X),
                new Rotator(90, Rotator.Axis.Y),
                new Rotator(90, Rotator.Axis.Z));
        Vector3 vec = new Vec3(1, 1, 1);

        Assertions.assertTrue(Vec3Math.equalsEpsilon(expectedVec, affineTransformation.transform(vec), 0.00001F));
    }

    @Test
    public void testTranslateTransform() {
        Vector3 expectedVec = new Vec3(9, -5, 2);

        AffineTransformation affineTransformation = new Transformation(new Translator(9, -5, 2));
        Vector3 vec = new Vec3(0, 0, 0);

        Assertions.assertTrue(Vec3Math.equalsEpsilon(expectedVec, affineTransformation.transform(vec), 0.00001F));
    }
}
