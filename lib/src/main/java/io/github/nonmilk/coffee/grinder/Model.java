package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.ObjFile;
import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.render.Texture;
import io.github.nonmilk.coffee.grinder.transformations.ModelTransformer;

import java.util.List;
import java.util.Objects;

public class Model {

    private final ObjFile obj;
    private final Mesh mesh;
    private Matrix4 matrix = new Mat4(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
    private Texture texture;

    public Model(final ObjFile obj, final Texture texture) {
        this.obj = cloneObj(obj);
        this.texture = texture;
        // might move it somewhere else
        this.mesh = Mesh.makeFromObjFile(obj);
    }

    public ObjFile obj() {
        return obj;
    }

    public List<MeshFace> meshFaces() {
        return mesh.meshFaces();
    }

    public Matrix4 matrix() {
        return matrix;
    }

    public Texture texture() {
        return texture;
    }

    public void setTexture(final Texture texture) {
        this.texture = Objects.requireNonNull(texture);
    }

    public void setTransform(final ModelTransformer modelTransformer) {
        Objects.requireNonNull(modelTransformer);
        matrix = modelTransformer.matrix();
    }

    private ObjFile cloneObj(final ObjFile obj) {
        // FIXME write this
        return Objects.requireNonNull(obj);
    }
}
