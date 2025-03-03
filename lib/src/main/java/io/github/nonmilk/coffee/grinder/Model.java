package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.ObjFile;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.nonmilk.coffee.grinder.render.Texture;
import io.github.nonmilk.coffee.grinder.transformations.ModelTransformer;
import io.github.shimeoki.jshaper.obj.Face;
import io.github.shimeoki.jshaper.obj.Triplet;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Model {

    private final ObjFile obj;
    private Mesh mesh;
    private final ModelTransformer transformer = new ModelTransformer();
    private Texture texture;

    public Model(final ObjFile obj, final Texture texture) {
        this.obj = cloneObj(obj);
        setTexture(texture);
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
        return transformer.matrix();
    }

    public Texture texture() {
        return texture;
    }

    public void setTexture(final Texture texture) {
        this.texture = Objects.requireNonNull(texture);
    }

    public ModelTransformer transformer() {
        return transformer;
    }

    public void removeTriplets(final List<Triplet> triplets) {
        Iterator<Face> iterator = obj.elements().faces().iterator();
        while (iterator.hasNext()) {
            Face face = iterator.next();
            face.triplets().removeAll(triplets);
            if (face.triplets().size() < 3) {
                iterator.remove(); // Remove the face from the collection
            }
        }

        this.mesh = Mesh.makeFromObjFile(obj);
    }

    private ObjFile cloneObj(final ObjFile obj) {
        // FIXME write this
        return Objects.requireNonNull(obj);
    }
}
