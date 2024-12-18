package io.github.nonmilk.coffee.grinder.math;

import io.github.alphameo.linear_algebra.vec.Vector4;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;

public class Vec4f implements Vector4 {

    private float x;
    private float y;
    private float z;
    private float w;

    public Vec4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4f(ObjVertex vertex) {
        this.x = vertex.x();
        this.y = vertex.y();
        this.z = vertex.z();
        this.w = 1;
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
    public float w() {
        return w;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public void setW(float z) {
        this.z = z;
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
            case 3:
                return w;
        }

        throw new IllegalArgumentException(String.format("Index %d is out of Vec4f bounds", i));
    };

    @Override
    public void set(int i, final float value) {
        switch (i) {
            case 0:
                this.x = value;
                break;
            case 1:
                this.y = value;
                break;
            case 2:
                this.z = value;
                break;
            case 3:
                this.w = value;
                break;
        }

        throw new IllegalArgumentException(String.format("Index %d is out of Vec4f bounds", i));
    };

    @Override
    public int size() {
        return 4;
    };
}