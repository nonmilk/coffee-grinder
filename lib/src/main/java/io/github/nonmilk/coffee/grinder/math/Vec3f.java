package io.github.nonmilk.coffee.grinder.math;

import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;

// rename?
public class Vec3f implements Vector3 {
    public final static Vec3f VECTOR_I = new Vec3f(1, 0, 0);
    public final static Vec3f VECTOR_J = new Vec3f(0, 1, 0);
    public final static Vec3f VECTOR_K = new Vec3f(0, 0, 1);

    private float x;
    private float y;
    private float z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(ObjVertex vertex) {
        this.x = vertex.x();
        this.y = vertex.y();
        this.z = vertex.z();
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
        }

        throw new IllegalArgumentException(String.format("Index %d is out of Vec3f bounds", i));
    };

    @Override
    public int size() {
        return 3;
    };
}
