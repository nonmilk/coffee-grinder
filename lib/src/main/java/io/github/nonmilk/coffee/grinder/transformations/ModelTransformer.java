package io.github.nonmilk.coffee.grinder.transformations;

import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.Model;
import ru.vsu.cs.konygina_d.affine.Rotator;
import ru.vsu.cs.konygina_d.affine.Scaling;
import ru.vsu.cs.konygina_d.affine.Transformation;
import ru.vsu.cs.konygina_d.affine.Translator;

import java.util.Objects;

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


    public ModelTransformer(Model model) {
        Objects.requireNonNull(model);
        this.model = model;
        resultingMatrix = model.modelMatrix();
    }

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

    public void setScaling(float x, float y, float z) {
        scaling.set(x, y, z);
        calculated = false;
    }

    public void setRelativeScaling(float dx, float dy, float dz) {
        scaling.setRelative(dx, dy, dz);
        calculated = false;
    }

    public void setTranslation(float x, float y, float z) {
        translator.set(x, y, z);
        calculated = false;
    }

    public void setRelativeTranslation(float dx, float dy, float dz) {
        translator.setRelative(dx, dy, dz);
        calculated = false;
    }

    public void setRotationX(float angle) {
        rotatorX.setAngle(angle);
        calculated = false;
    }

    public void setRelativeRotationX(float radians) {
        rotatorX.setAngle(radians);
        calculated = false;
    }

    public void setRotationY(float radians) {
        rotatorY.setAngle(radians);
        calculated = false;
    }

    public void setRelativeRotationY(float radians) {
        rotatorY.setAngle(radians);
        calculated = false;
    }

    public void setRotationZ(float radians) {
        rotatorZ.setAngle(radians);
        calculated = false;
    }

    public void setRelativeRotationZ(float radians) {
        rotatorZ.setAngle(radians);
        calculated = false;
    }

    public void setRotationOrder(RotationOrder order) {
        Objects.requireNonNull(order);
        this.rotationOrder = order;
        calculated = false;
    }
}