package io.github.nonmilk.coffee.grinder.render.triangle;

import java.util.List;
import java.util.Objects;

import io.github.alphameo.linear_algebra.vec.Vector2;
import io.github.nonmilk.coffee.grinder.math.Vec2f;
import io.github.shimeoki.jfx.rasterization.triangle.geom.TriangleBarycentrics;
import io.github.shimeoki.jshaper.obj.data.ObjTriplet;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjTextureVertex;

public class UV {
    private final Vector2 uv1;
    private final Vector2 uv2;
    private final Vector2 uv3;

    private UV(final Vector2 uv1, final Vector2 uv2, final Vector2 uv3) {
        this.uv1 = Objects.requireNonNull(uv1);
        this.uv2 = Objects.requireNonNull(uv2);
        this.uv3 = Objects.requireNonNull(uv3);
    }

    public Vector2 barycentricUV(final TriangleBarycentrics barycentrics) {
        Objects.requireNonNull(barycentrics);

        final float l1 = barycentrics.lambda1();
        final float l2 = barycentrics.lambda2();
        final float l3 = barycentrics.lambda3();

        final float x = uv1.x() * l1 + uv2.x() * l2 + uv3.x() * l3;
        final float y = uv1.y() * l1 + uv2.y() * l2 + uv3.y() * l3;

        return new Vec2f(x, y);
    }

    public static UV makeUVFromFace(final ObjFace face) {
        Objects.requireNonNull(face);

        final List<ObjTriplet> triplets = face.triplets();

        final ObjTriplet triplet1 = triplets.get(0);
        final ObjTriplet triplet2 = triplets.get(1);
        final ObjTriplet triplet3 = triplets.get(2);

        final Vector2 uv1 = uvFromTextureVertex(triplet1.textureVertex());
        final Vector2 uv2 = uvFromTextureVertex(triplet2.textureVertex());
        final Vector2 uv3 = uvFromTextureVertex(triplet3.textureVertex());

        return new UV(uv1, uv2, uv3);
    }

    private static Vector2 uvFromTextureVertex(final ObjTextureVertex textureVertex) {
        return new Vec2f(textureVertex.u(), textureVertex.v());
    }
}
