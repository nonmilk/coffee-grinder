package io.github.nonmilk.coffee.grinder.math;

import io.github.alphameo.linear_algebra.vec.Vector2;
import io.github.traunin.triangulation.Vector2f;

public class Vec2f implements Vector2f, Vector2 {

    private float x;
    private float y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
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
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
        }

        throw new IllegalArgumentException(String.format("Index %d is out of Vec2f bounds", i));
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
        }

        throw new IllegalArgumentException(String.format("Index %d is out of Vec2f bounds", i));
    };

    @Override
    public int size() {
        return 2;
    };

    @Override
    public Vec2f clone() {
        return new Vec2f(this.x, this.y);
    }
}
