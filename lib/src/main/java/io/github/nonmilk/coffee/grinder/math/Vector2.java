package io.github.nonmilk.coffee.grinder.math;

import io.github.traunin.triangulation.Vector2f;

public class Vector2 implements Vector2f {

    private final float x;
    private final float y;

    public Vector2(float x, float y) {
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
}
