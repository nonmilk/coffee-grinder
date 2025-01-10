module io.github.nonmilk.coffee.grinder {

    requires transitive io.github.shimeoki.jfx.rasterization;
    requires transitive io.github.traunin.triangulation;
    requires transitive java.desktop;

    requires transitive javafx.graphics;
    requires transitive io.github.shimeoki.jshaper;
    requires transitive io.github.alphameo.linear_algebra;

    exports io.github.nonmilk.coffee.grinder;
    exports io.github.nonmilk.coffee.grinder.transformations;

    exports io.github.nonmilk.coffee.grinder.camera;
    exports io.github.nonmilk.coffee.grinder.camera.view;

    exports io.github.nonmilk.coffee.grinder.math;
    exports io.github.nonmilk.coffee.grinder.math.affine;

    exports io.github.nonmilk.coffee.grinder.render;
    exports io.github.nonmilk.coffee.grinder.render.triangle;
}
