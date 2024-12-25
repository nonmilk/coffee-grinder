package io.github.nonmilk.coffee.grinder.transformations;

import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.Model;
import ru.vsu.cs.konygina_d.render_engine.Rotator;
import ru.vsu.cs.konygina_d.render_engine.Scaling;
import ru.vsu.cs.konygina_d.render_engine.Transformation;
import ru.vsu.cs.konygina_d.render_engine.Translator;

public class ModelTransformer {
    private final Model model;
    Matrix4 resultingMatrix;
    private final Scaling scaling = new Scaling();
    RotationOrder rotationOrder = RotationOrder.XYZ;
    private final Rotator rotatorX = new Rotator(Rotator.Axis.X);
    private final Rotator rotatorY = new Rotator(Rotator.Axis.Y);
    private final Rotator rotatorZ = new Rotator(Rotator.Axis.Z);
    private final Translator translator = new Translator();
    private boolean calculated = false;


    public ModelTransformer(Model model) {
        this.model = model;
        resultingMatrix = model.modelMatrix();
    }

    public Matrix4 modelMatrix() {
        if (calculated) {
            return resultingMatrix;
        }

        Transformation at = constructTransformation(rotationOrder);
        resultingMatrix = at.getMatrix();
        calculated = true;

        return resultingMatrix;
    }

    private Transformation constructTransformation(RotationOrder order) {
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
                return constructTransformation(RotationOrder.XYZ);
            }
        }
    }

    public void scale(float x, float y, float z) {
        scaling.set(x, y, z);
        calculated = false;
    }

    public void scaleRelative(float dx, float dy, float dz) {
        scaling.setRelative(dx, dy, dz);
        calculated = false;
    }

    public void translate(float x, float y, float z) {
        translator.set(x, y, z);
        calculated = false;
    }

    public void translateRelative(float dx, float dy, float dz) {
        translator.setRelative(dx, dy, dz);
        calculated = false;
    }

    public void rotateX(float angle) {
        rotatorX.setAngle(angle);
        calculated = false;
    }

    public void rotateXRelative(float dAngle) {
        rotatorX.setAngle(dAngle);
        calculated = false;
    }

    public void rotateY(float angle) {
        rotatorY.setAngle(angle);
        calculated = false;
    }

    public void rotateYRelative(float dAngle) {
        rotatorY.setAngle(dAngle);
        calculated = false;
    }

    public void rotateZ(float angle) {
        rotatorZ.setAngle(angle);
        calculated = false;
    }

    public void rotateZRelative(float dAngle) {
        rotatorZ.setAngle(dAngle);
        calculated = false;
    }

    public void setRotationOrder(RotationOrder order) {
        this.rotationOrder = order;
        calculated = false;
    }
}
