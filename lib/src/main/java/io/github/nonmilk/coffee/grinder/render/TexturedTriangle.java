package io.github.nonmilk.coffee.grinder.render;

import java.util.List;
import java.util.Objects;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.vec.Vec4Math;
import io.github.alphameo.linear_algebra.vec.Vector4;
import io.github.nonmilk.coffee.grinder.math.Vec3f;
import io.github.nonmilk.coffee.grinder.math.Vec4f;
import io.github.shimeoki.jfx.rasterization.geom.Point2f;
import io.github.shimeoki.jfx.rasterization.triangle.geom.Triangle;
import io.github.shimeoki.jshaper.obj.data.ObjTriplet;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;

public class TexturedTriangle implements Triangle {

    private final ScreenTransform transform;

    private final Vec3f v1;
    private final Vec3f v2;
    private final Vec3f v3;

    // private final Vec3f v1n;
    // private final Vec3f v2n;
    // private final Vec3f v3n;

    public TexturedTriangle(final ObjFace face, final ScreenTransform transform) {
        Objects.requireNonNull(face);
        this.transform = Objects.requireNonNull(transform);

        List<ObjTriplet> triplets = face.triplets();
        ObjTriplet triplet1 = triplets.get(0);
        ObjTriplet triplet2 = triplets.get(1);
        ObjTriplet triplet3 = triplets.get(2);

        this.v1 = vertexToScreen(triplet1.vertex());
        this.v2 = vertexToScreen(triplet2.vertex());
        this.v3 = vertexToScreen(triplet3.vertex());
    }

    private Vec3f vertexToScreen(ObjVertex objVertex) {
        Vector4 vertex = new Vec4f(objVertex);
        vertex = Mat4Math.prod(transform.model(), vertex);
        vertex = Mat4Math.prod(transform.viewProjection(), vertex);
        Vec4Math.divide(vertex, vertex.w());

        float width = transform.width();
        float height = transform.heigth();
        return new Vec3f(
                vertex.x() * width + width / 2,
                -vertex.y() * height + height / 2,
                vertex.z());
    }

    public float barycentricX(float lambda1, float lambda2, float lambda3) {
        return v1.x() * lambda1 + v2.x() * lambda2 + v3.x() * lambda3;
    }

    public float barycentricY(float lambda1, float lambda2, float lambda3) {
        return v1.y() * lambda1 + v2.y() * lambda2 + v3.y() * lambda3;
    }

    public float barycentricZ(float lambda1, float lambda2, float lambda3) {
        return v1.z() * lambda1 + v2.z() * lambda2 + v3.z() * lambda3;
    }

    @Override
    public Point2f v1() {
        return v1;
    }

    @Override
    public Point2f v2() {
        return v2;
    }

    @Override
    public Point2f v3() {
        return v3;
    }
}