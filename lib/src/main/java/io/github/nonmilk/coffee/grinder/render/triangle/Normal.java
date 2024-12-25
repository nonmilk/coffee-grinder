package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.List;
import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vec4Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.alphameo.linear_algebra.vec.Vector4;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.math.Vec4f;
import io.github.nonmilk.coffee.grinder.render.ScreenTransform;
import io.github.shimeoki.jfx.rasterization.math.Floats;
import io.github.shimeoki.jfx.rasterization.triangle.geom.TriangleBarycentrics;
import io.github.shimeoki.jshaper.obj.data.ObjTriplet;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjVertexNormal;

public class Normal {
    private Vector3 v1n;
    private Vector3 v2n;
    private Vector3 v3n;

    public Normal(final Vector3 v1n, final Vector3 v2n, final Vector3 v3n) {
        checkNormalized(v1n);
        checkNormalized(v2n);
        checkNormalized(v3n);

        this.v1n = v1n;
        this.v2n = v2n;
        this.v3n = v3n;
    }

    public Vector3 barycentricNormal(final TriangleBarycentrics barycentrics) {
        Objects.requireNonNull(barycentrics);

        final float l1 = barycentrics.lambda1();
        final float l2 = barycentrics.lambda2();
        final float l3 = barycentrics.lambda3();

        final float x = v1n.x() * l1 + v2n.x() * l2 + v3n.x() * l3;
        final float y = v1n.y() * l1 + v2n.y() * l2 + v3n.y() * l3;
        final float z = v1n.z() * l1 + v2n.z() * l2 + v3n.z() * l3;

        return new Vec3f(x, y, z);
    }

    public Vector3 v1n() {
        return v1n;
    }

    public Vector3 v2n() {
        return v2n;
    }

    public Vector3 v3n() {
        return v3n;
    }
    
    private static void checkNormalized(Vector3 normal) {
        Objects.requireNonNull(normal);

        // length check, likely hurts performance, uncomment if needed
        /*
        if (!Floats.equals(1, Vec3Math.len2(normal))) {
            throw new IllegalArgumentException("Input is not normalized");
        }
         */
    }

    public static Normal makeNormalFromFace(final ObjFace face, final ScreenTransform transform) {
        Objects.requireNonNull(face);
        Objects.requireNonNull(transform);
        final List<ObjTriplet> triplets = face.triplets();

        final ObjTriplet triplet1 = triplets.get(0);
        final ObjTriplet triplet2 = triplets.get(1);
        final ObjTriplet triplet3 = triplets.get(2);

        final Vector3 v1n = normalToWorld(triplet1.vertexNormal(), transform);
        final Vector3 v2n = normalToWorld(triplet2.vertexNormal(), transform);
        final Vector3 v3n = normalToWorld(triplet3.vertexNormal(), transform);

        return new Normal(v1n, v2n, v3n);
    }

    private static Vec3f normalToWorld(final ObjVertexNormal objNormal, final ScreenTransform transform) {
        Vector4 vertex = new Vec4f(objNormal);
        vertex = Mat4Math.prod(transform.model(), vertex);
        // do we need to convert them to camera?
        // vertex = Mat4Math.prod(transform.view(), vertex);
        Vec4Math.divide(vertex, vertex.w());

        return new Vec3f(
                vertex.x(),
                vertex.y(),
                vertex.z());
    }
}