package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.List;
import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.vec.Vec4Math;
import io.github.alphameo.linear_algebra.vec.Vector4;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.math.Vec4f;
import io.github.nonmilk.coffee.grinder.render.ScreenTransform;
import io.github.shimeoki.jfx.rasterization.triangle.geom.TriangleBarycentrics;
import io.github.shimeoki.jshaper.obj.data.ObjTriplet;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;

public class Positioning {
    private Vec3f v1;
    private Vec3f v2;
    private Vec3f v3;

    public Positioning(final Vec3f v1, final Vec3f v2, final Vec3f v3) {
        this.v1 = Objects.requireNonNull(v1);
        this.v2 = Objects.requireNonNull(v2);
        this.v3 = Objects.requireNonNull(v3);
    }

    public Vec3f v1() {
        return v1;
    }

    public Vec3f v2() {
        return v2;
    }

    public Vec3f v3() {
        return v3;
    }

    public static Positioning makePositioningFromFace(final ObjFace face, final ScreenTransform transform) {
        Objects.requireNonNull(face);
        Objects.requireNonNull(transform);
        final List<ObjTriplet> triplets = face.triplets();
        final ObjTriplet triplet1 = triplets.get(0);
        final ObjTriplet triplet2 = triplets.get(1);
        final ObjTriplet triplet3 = triplets.get(2);

        final Vec3f v1 = vertexToScreen(triplet1.vertex(), transform);
        final Vec3f v2 = vertexToScreen(triplet2.vertex(), transform);
        final Vec3f v3 = vertexToScreen(triplet3.vertex(), transform);
        return new Positioning(v1, v2, v3);
    }

    private static Vec3f vertexToScreen(final ObjVertex objVertex, final ScreenTransform transform) {
        Vector4 vertex = new Vec4f(objVertex);
        vertex = Mat4Math.prod(transform.model(), vertex);
        vertex = Mat4Math.prod(transform.viewProjection(), vertex);
        Vec4Math.divide(vertex, vertex.w());

        float width = transform.width();
        float height = transform.height();
        return new Vec3f(
                vertex.x() * width + width / 2,
                -vertex.y() * height + height / 2,
                vertex.z());
    }

    public float barycentricX(final TriangleBarycentrics barycentrics) {
        Objects.requireNonNull(barycentrics);
        final float l1 = barycentrics.lambda1();
        final float l2 = barycentrics.lambda2();
        final float l3 = barycentrics.lambda3();

        return v1.x() * l1 + v2.x() * l2 + v3.x() * l3;
    }

    public float barycentricY(final TriangleBarycentrics barycentrics) {
        Objects.requireNonNull(barycentrics);
        final float l1 = barycentrics.lambda1();
        final float l2 = barycentrics.lambda2();
        final float l3 = barycentrics.lambda3();

        return v1.y() * l1 + v2.y() * l2 + v3.y() * l3;
    }

    public float barycentricZ(final TriangleBarycentrics barycentrics) {
        Objects.requireNonNull(barycentrics);
        final float l1 = barycentrics.lambda1();
        final float l2 = barycentrics.lambda2();
        final float l3 = barycentrics.lambda3();

        return v1.z() * l1 + v2.z() * l2 + v3.z() * l3;
    }
}
