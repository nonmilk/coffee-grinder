package io.github.nonmilk.coffee.grinder.transformations;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.Model;
import io.github.nonmilk.coffee.grinder.math.affine.Rotator;
import io.github.nonmilk.coffee.grinder.math.affine.Scaling;
import io.github.nonmilk.coffee.grinder.math.affine.Transformation;
import io.github.nonmilk.coffee.grinder.math.affine.Translator;

import java.util.Objects;

/**
 * A class that takes model inside and constructs a transformation matrix from
 * the added parameters.
 */
public final class ModelTransformer {
    private final Model model;
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
    public ModelTransformer(Model model) {
        Objects.requireNonNull(model);
        this.model = model;
        resultingMatrix = model.modelMatrix();
    }

    /**
     * Calculates transformation matrix or returns calculated one if it has already
     * been calculated and there were no changes to this parameters.
     * 
     * @return matrix that represents transformation operator for pixel in
     *         coordinate system
     */
    public Matrix4 modelMatrix() {
        if (calculated) {
            return resultingMatrix;
        }

        Transformation at = construct(rotationOrder);
        resultingMatrix = at.getMatrix();
        calculated = true;

        return resultingMatrix;
    }

    private Transformation construct(RotationOrder order) {
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
    public void setScaling(float x, float y, float z) {
        scaling.set(x, y, z);
        calculated = false;
    }

    /**
     * Multiplies existing scaling factors for each axis.
     * 
     * @param dx multiplier for existing X axis multiplier
     * @param dy multiplier for existing Y axis multiplier
     * @param dz multiplier for existing Z axis multiplier
     */
    public void setRelativeScaling(float dx, float dy, float dz) {
        scaling.setRelative(dx, dy, dz);
        calculated = false;
    }

    /**
     * Sets new offsets for each axis.
     * 
     * @param x new X axis offset
     * @param y new Y axis offset
     * @param z new Z axis offset
     */
    public void setTranslation(float x, float y, float z) {
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
    public void setRelativeTranslation(float dx, float dy, float dz) {
        translator.setRelative(dx, dy, dz);
        calculated = false;
    }

    /**
     * Sets new value for the X-axis rotation angle. *
     * 
     * @param angle new angle value of rotation
     */
    public void setRotationX(float angle) {
        rotatorX.setAngle(angle);
        calculated = false;
    }

    /**
     * Adds a value to the X-axis rotation angle.
     * 
     * @param radians change of the rotaion angle value
     */
    public void setRelativeRotationX(float radians) {
        rotatorX.setAngle(radians);
        calculated = false;
    }

    /**
     * Sets new value for the Y-axis rotation angle. *
     * 
     * @param angle new angle value of rotation
     */
    public void setRotationY(float radians) {
        rotatorY.setAngle(radians);
        calculated = false;
    }

    /**
     * Adds a value to the Y-axis rotation angle.
     * 
     * @param radians change of the rotaion angle value
     */
    public void setRelativeRotationY(float radians) {
        rotatorY.setAngle(radians);
        calculated = false;
    }

    /**
     * Sets new value for the Z-axis rotation angle. *
     * 
     * @param angle new angle value of rotation
     */
    public void setRotationZ(float radians) {
        rotatorZ.setAngle(radians);
        calculated = false;
    }

    /**
     * Adds a value to the Z-axis rotation angle.
     * 
     * @param radians change of the rotaion angle value
     */
    public void setRelativeRotationZ(float radians) {
        rotatorZ.setAngle(radians);
        calculated = false;
    }

    /**
     * Sets the rotation order of the axes for calculating the transformation
     * matrix.
     * 
     * @param order axes rotation order
     */
    public void setRotationOrder(RotationOrder order) {
        Objects.requireNonNull(order);
        this.rotationOrder = order;
        calculated = false;
    }
}
