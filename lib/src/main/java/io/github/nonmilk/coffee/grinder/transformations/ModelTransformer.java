package io.github.nonmilk.coffee.grinder.transformations;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import static io.github.alphameo.linear_algebra.mat.Matrix4Row.*;
import static io.github.alphameo.linear_algebra.mat.Matrix4Col.*;
import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.math.affine.Rotator;
import io.github.nonmilk.coffee.grinder.math.affine.Scaling;
import io.github.nonmilk.coffee.grinder.math.affine.Transformation;
import io.github.nonmilk.coffee.grinder.math.affine.Translator;
import io.github.shimeoki.jshaper.obj.Vertex;
import io.github.shimeoki.jshaper.obj.VertexData;
import io.github.shimeoki.jshaper.obj.VertexNormal;

import java.util.Objects;

/**
 * A class that takes model inside and constructs a transformation matrix from
 * the added parameters.
 */
public final class ModelTransformer {
    private Matrix4 resultingMatrix;

    private final Scaling scaling = new Scaling();
    private RotationOrder rotationOrder = RotationOrder.XYZ;
    private final Rotator rotatorX = new Rotator(Rotator.Axis.X);
    private final Rotator rotatorY = new Rotator(Rotator.Axis.Y);
    private final Rotator rotatorZ = new Rotator(Rotator.Axis.Z);
    private final Translator translator = new Translator();
    private boolean calculated = false;

    /**
     * Simple constructor for creation a transformation matrix.
     * 
     * @param model for constructing transformation matrix
     */
    public ModelTransformer() {
        resultingMatrix = Mat4Math.unitMat();
    }

    /**
     * Calculates transformation matrix or returns calculated one if it has already
     * been calculated and there were no changes to this parameters.
     * 
     * @return matrix that represents transformation operator for pixel in
     *         coordinate system
     */
    public Matrix4 matrix() {
        if (calculated) {
            return resultingMatrix;
        }

        final Transformation at = construct(rotationOrder);
        resultingMatrix = at.getMatrix();
        calculated = true;

        return resultingMatrix;
    }

    private Transformation construct(final RotationOrder order) {
        Objects.requireNonNull(order);
        switch (order) {
            case XYZ -> {
                return new Transformation(translator, rotatorZ, rotatorY, rotatorX, scaling);
            }
            case XZY -> {
                return new Transformation(translator, rotatorY, rotatorZ, rotatorX, scaling);
            }
            case YXZ -> {
                return new Transformation(translator, rotatorZ, rotatorX, rotatorY, scaling);
            }
            case YZX -> {
                return new Transformation(translator, rotatorX, rotatorZ, rotatorY, scaling);
            }
            case ZXY -> {
                return new Transformation(translator, rotatorY, rotatorX, rotatorZ, scaling);
            }
            case ZYX -> {
                return new Transformation(translator, rotatorX, rotatorY, rotatorZ, scaling);
            }
            default -> {
                return construct(RotationOrder.XYZ);
            }
        }
    }

    /**
     * Sets new scaling multipliers for each axis.
     * 
     * @param x new multiplier for X axis
     * @param y new multiplier for Y axis
     * @param z new multiplier for Z axis
     */
    public void setScaling(final float x, final float y, final float z) {
        scaling.set(x, y, z);
        calculated = false;
    }

    /**
     * Multiplies existing scaling factors for each axis.
     * 
     * @param multX multiplier for existing X axis multiplier
     * @param multY multiplier for existing Y axis multiplier
     * @param multZ multiplier for existing Z axis multiplier
     */
    public void setRelativeScaling(final float multX, final float multY, final float multZ) {
        scaling.setRelative(multX, multY, multZ);
        calculated = false;
    }

    /**
     * Sets new offsets for each axis.
     * 
     * @param x new X axis offset
     * @param y new Y axis offset
     * @param z new Z axis offset
     */
    public void setTranslation(final float x, final float y, final float z) {
        translator.set(x, y, z);
        calculated = false;
    }

    /**
     * Adds value to each axis offset.
     * 
     * @param dx change of X axis offset
     * @param dy change of Y axis offset
     * @param dz change of Z axis offset
     */
    public void setRelativeTranslation(final float dx, final float dy, final float dz) {
        translator.setRelative(dx, dy, dz);
        calculated = false;
    }

    /**
     * Sets new value for the X-axis rotation angle in radians.
     * 
     * @param rad new angle value of rotation in radians
     */
    public void setRotationX(final float rad) {
        rotatorX.setAngle(rad);
        calculated = false;
    }

    /**
     * Adds a value to the X-axis rotation angle in radians.
     * 
     * @param dRad change of the rotaion angle value in radians
     */
    public void setRelativeRotationX(final float dRad) {
        rotatorX.setRelative(dRad);
        calculated = false;
    }

    /**
     * Sets new value for the Y-axis rotation angle in radians.
     * 
     * @param rad new angle value of rotation in radians
     */
    public void setRotationY(final float rad) {
        rotatorY.setAngle(rad);
        calculated = false;
    }

    /**
     * Adds a value to the Y-axis rotation angle in radians.
     * 
     * @param dRad change of the rotaion angle value in radians
     */
    public void setRelativeRotationY(final float dRad) {
        rotatorY.setRelative(dRad);
        calculated = false;
    }

    /**
     * Sets new value for the Z-axis rotation angle in radians.
     * 
     * @param rad new angle value of rotation in radians
     */
    public void setRotationZ(final float rad) {
        rotatorZ.setAngle(rad);
        calculated = false;
    }

    /**
     * Adds a value to the Z-axis rotation angle in radians.
     * 
     * @param dRad change of the rotaion angle value in radians
     */
    public void setRelativeRotationZ(final float dRad) {
        rotatorZ.setRelative(dRad);
        calculated = false;
    }

    /**
     * Sets the rotation order of the axes for calculating the transformation
     * matrix.
     * 
     * @param order axes rotation order
     */
    public void setRotationOrder(final RotationOrder order) {
        Objects.requireNonNull(order);
        this.rotationOrder = order;
        calculated = false;
    }

    public void transform(final Model model) {
        Matrix4 m = this.matrix();

        float x, y, z, w;
        for (Vertex v : model.obj().vertexData().vertices()) {

            x = m.get(R0, C0) * v.x()
                    + m.get(R0, C1) * v.y()
                    + m.get(R0, C2) * v.z()
                    + m.get(R0, C3);

            y = m.get(R1, C0) * v.x()
                    + m.get(R1, C1) * v.y()
                    + m.get(R1, C2) * v.z()
                    + m.get(R1, C3);

            z = m.get(R2, C0) * v.x()
                    + m.get(R2, C1) * v.y()
                    + m.get(R2, C2) * v.z()
                    + m.get(R2, C3);

            // virtual 4th coordinate. In initial vertex should be 1, so not used in product
            w = m.get(R3, C0) * v.x()
                    + m.get(R3, C1) * v.y()
                    + m.get(R3, C2) * v.z()
                    + m.get(R3, C3);

            v.setX(x / w);
            v.setY(y / w);
            v.setZ(z / w);
        }
    }
}
