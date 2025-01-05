package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.obj.Triplet;

import java.util.Objects;

public final class MeshFace {
    private final Triplet v1;
    private final Triplet v2;
    private final Triplet v3;

    public MeshFace(Triplet v1, Triplet v2, Triplet v3) {
        this.v1 = Objects.requireNonNull(v1);
        this.v2 = Objects.requireNonNull(v2);
        this.v3 = Objects.requireNonNull(v3);
    }

    public Triplet v1() {
        return v1;
    }

    public Triplet v2() {
        return v2;
    }

    public Triplet v3() {
        return v3;
    }
}
