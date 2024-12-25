package io.github.nonmilk.coffee.grinder.render;

import java.util.Objects;

import io.github.nonmilk.coffee.grinder.render.triangle.Normal;
import io.github.nonmilk.coffee.grinder.render.triangle.Positioning;
import io.github.shimeoki.jfx.rasterization.geom.Point2f;
import io.github.shimeoki.jfx.rasterization.triangle.geom.Triangle;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;

public class RenderedFace implements Triangle {

    private final Positioning positioning;
    private final Normal normal;

    public RenderedFace(final ObjFace face, final ScreenTransform transform) {
        Objects.requireNonNull(face);
        Objects.requireNonNull(transform);

        positioning = Positioning.makePositioningFromFace(face, transform);
        normal = Normal.makeNormalFromFace(face, transform);
    }

    public Normal normal() {
        return normal;
    }

    public Positioning positioning() {
        return positioning;
    }

    @Override
    public Point2f v1() {
        return positioning.v1();
    }

    @Override
    public Point2f v2() {
        return positioning.v2();
    }

    @Override
    public Point2f v3() {
        return positioning.v3();
    }
}