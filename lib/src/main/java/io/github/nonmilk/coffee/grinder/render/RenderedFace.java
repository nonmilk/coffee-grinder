package io.github.nonmilk.coffee.grinder.render;

import java.util.Objects;

import io.github.nonmilk.coffee.grinder.MeshFace;
import io.github.nonmilk.coffee.grinder.render.triangle.Normal;
import io.github.nonmilk.coffee.grinder.render.triangle.Shape;
import io.github.nonmilk.coffee.grinder.render.triangle.UV;
import io.github.shimeoki.jfx.rasterization.Point2f;
import io.github.shimeoki.jfx.rasterization.Triangle;
import io.github.shimeoki.jshaper.obj.Face;

public class RenderedFace implements Triangle {

    private final Shape shape;
    private final Normal normal;
    private final UV uv;

    public RenderedFace(final MeshFace meshFace, final ScreenTransform transform) {
        Objects.requireNonNull(meshFace);
        Objects.requireNonNull(transform);

        shape = Shape.makeShapeFromMeshFace(meshFace, transform);
        normal = Normal.makeNormalFromMeshFace(meshFace, transform);
        switch (meshFace.v1().format()) {
            case ALL, TEXTURE_VERTEX:
                uv = UV.makeUVFromMeshFace(meshFace);
                break;
            default:
                uv = null;
                break;
        }
    }

    public Normal normal() {
        return normal;
    }

    public Shape shape() {
        return shape;
    }

    public UV uv() {
        return uv;
    }

    @Override
    public Point2f v1() {
        return shape.v1();
    }

    @Override
    public Point2f v2() {
        return shape.v2();
    }

    @Override
    public Point2f v3() {
        return shape.v3();
    }
}
