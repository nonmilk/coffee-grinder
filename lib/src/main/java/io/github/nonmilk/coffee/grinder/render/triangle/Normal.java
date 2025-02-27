package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.List;
import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.vec.Vec4Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.alphameo.linear_algebra.vec.Vector4;
import io.github.nonmilk.coffee.grinder.Mesh;
import io.github.nonmilk.coffee.grinder.MeshFace;
import io.github.nonmilk.coffee.grinder.math.UnitVec3f;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.math.Vec4f;
import io.github.nonmilk.coffee.grinder.render.ScreenTransform;
import io.github.shimeoki.jshaper.obj.Triplet;
import io.github.shimeoki.jshaper.obj.Face;
import io.github.shimeoki.jshaper.obj.VertexNormal;
import io.github.shimeoki.jfx.rasterization.triangle.Barycentrics;

public class Normal {
    private Vector3 v1n;
    private Vector3 v2n;
    private Vector3 v3n;

    public Normal(final UnitVec3f v1n, final UnitVec3f v2n, final UnitVec3f v3n) {
        this.v1n = Objects.requireNonNull(v1n);
        this.v2n = Objects.requireNonNull(v2n);
        this.v3n = Objects.requireNonNull(v3n);
    }

    public Vector3 barycentricNormal(final Barycentrics barycentrics) {
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

    public static Normal makeNormalFromMeshFace(final MeshFace face, final ScreenTransform transform) {
        Objects.requireNonNull(face);
        Objects.requireNonNull(transform);

        final Triplet triplet1 = face.v1();
        final Triplet triplet2 = face.v2();
        final Triplet triplet3 = face.v3();

        final UnitVec3f v1n = normalToWorld(triplet1.vertexNormal(), transform);
        final UnitVec3f v2n = normalToWorld(triplet2.vertexNormal(), transform);
        final UnitVec3f v3n = normalToWorld(triplet3.vertexNormal(), transform);

        return new Normal(v1n, v2n, v3n);
    }

    private static UnitVec3f normalToWorld(final VertexNormal objNormal, final ScreenTransform transform) {
        Vector4 v = new Vec4f(objNormal);
        v = Mat4Math.prod(transform.model(), v);
        // do we need to convert them to camera?
        // vertex = Mat4Math.prod(transform.view(), vertex);
        v.setW(0);
        Vec4Math.normalize(v);

        return new UnitVec3f(v.x(), v.y(), v.z());
    }
}
