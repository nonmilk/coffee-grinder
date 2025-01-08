package io.github.nonmilk.coffee.grinder.transformations;

import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.mat.Matrix4Col;
import io.github.alphameo.linear_algebra.mat.Matrix4Row;
import io.github.nonmilk.coffee.grinder.math.affine.Rotator;
import io.github.nonmilk.coffee.grinder.math.affine.Scaling;
import io.github.nonmilk.coffee.grinder.math.affine.Transformation;
import io.github.nonmilk.coffee.grinder.math.affine.Translator;

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
     * @param sx new multiplier for X axis
     * @param sy new multiplier for Y axis
     * @param sz new multiplier for Z axis
     */
    public void setScaling(final float sx, final float sy, final float sz) {
        scaling.set(sx, sy, sz);
        calculated = false;
    }

    /**
     * Returns scaling multiplier for X axis
     * 
     * @return multiplier for X axis
     */
    public float getScalingX() {
        return scaling.getMatrix().get(Matrix4Row.R0, Matrix4Col.C0);
    }

    /**
     * Sets new scaling multipliers for X axis.
     * 
     * @param sx new multiplier for X axis
     */
    public void setScalingX(final float sx) {
        scaling.setX(sx);
        calculated = false;
    }

    /**
     * Returns scaling multiplier for Y axis
     * 
     * @return multiplier for Y axis
     */
    public float getScalingY() {
        return scaling.getMatrix().get(Matrix4Row.R1, Matrix4Col.C1);
    }

    /**
     * Sets new scaling multipliers for Y axis.
     * 
     * @param sy new multiplier for Y axis
     */
    public void setScalingY(final float sy) {
        scaling.setY(sy);
        calculated = false;
    }

    /**
     * Returns scaling multiplier for Z axis
     * 
     * @return multiplier for Z axis
     */
    public float getScalingZ() {
        return scaling.getMatrix().get(Matrix4Row.R2, Matrix4Col.C2);
    }

    /**
     * Sets new scaling multipliers for Y axis.
     * 
     * @param sz new multiplier for Z axis
     */
    public void setScalingZ(final float sz) {
        scaling.setZ(sz);
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
     * @param tx new X axis offset
     * @param ty new Y axis offset
     * @param tz new Z axis offset
     */
    public void setTranslation(final float tx, final float ty, final float tz) {
        translator.set(tx, ty, tz);
        calculated = false;
    }

    /**
     * Returns offset for X axis.
     * 
     * @return X axis offset
     */
    public float getTranslationX() {
        return translator.getMatrix().get(Matrix4Row.R0, Matrix4Col.C3);
    }

    /**
     * Sets new offset for X axis.
     * 
     * @param tx new X axis offset
     */
    public void setTranslationX(final float tx) {
        translator.setX(tx);
        calculated = false;
    }

    /**
     * Returns offset for Y axis.
     * 
     * @return Y axis offset
     */
    public float getTranslationY() {
        return translator.getMatrix().get(Matrix4Row.R1, Matrix4Col.C3);
    }

    /**
     * Sets new offset for Y axis.
     * 
     * @param ty new Y axis offset
     */
    public void setTranslationY(final float ty) {
        translator.setY(ty);
        calculated = false;
    }

    /**
     * Returns offset for Z axis.
     * 
     * @return Z axis offset
     */
    public float getTranslationZ() {
        return translator.getMatrix().get(Matrix4Row.R2, Matrix4Col.C3);
    }

    /**
     * Sets new offset for Z axis.
     * 
     * @param tz new Z axis offset
     */
    public void setTranslationZ(final float tz) {
        translator.setZ(tz);
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
}
