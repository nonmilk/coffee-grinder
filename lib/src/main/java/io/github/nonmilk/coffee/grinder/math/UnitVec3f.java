package io.github.nonmilk.coffee.grinder.math;

import io.github.alphameo.linear_algebra.vec.Vector3;

public class UnitVec3f implements Vector3 {

    private float x;
    private float y;
    private float z;

    public UnitVec3f(float x, float y, float z) {
        if (!Floats.laxEquals(1, x * x + y * y + z * z)) {
            throw new IllegalArgumentException("Unit vector has to have length 1");
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public float z() {
        return z;
    }

    @Override
    public void setX(float x) {
        throw new UnsupportedOperationException("Setting is not supported");
    }

    @Override
    public void setY(float y) {
        throw new UnsupportedOperationException("Setting is not supported");
    }

    @Override
    public void setZ(float z) {
        throw new UnsupportedOperationException("Setting is not supported");
    }

    @Override
    public float get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
        }

        throw new IllegalArgumentException(String.format("Index %d is out of Vec3f bounds", i));
    };

    @Override
    public void set(int i, final float value) {
        throw new UnsupportedOperationException("Setting is not supported");
    };

    @Override
    public int size() {
        return 3;
    };

    @Override
    public UnitVec3f clone() {
        return new UnitVec3f(this.x, this.y, this.z);
    }
}
