package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.ObjFile;
import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.render.Texture;

import java.util.Objects;

public class Model {

    private final ObjFile model;
    private final Matrix4 modelMatrix = new Mat4(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
    private Texture texture;

    public Model(final ObjFile obj, final Texture texture) {
        model = cloneObj(obj);
        this.texture = texture;
    }

    public ObjFile model() {
        return model;
    }

    public Matrix4 modelMatrix() {
        return modelMatrix;
    }

    public Texture texture() {
        return texture;
    }

    public void setTexture(final Texture texture) {
        this.texture = Objects.requireNonNull(texture);
    }

    private ObjFile cloneObj(ObjFile obj) {
        // FIXME write this
        return Objects.requireNonNull(obj);        
    }
}
