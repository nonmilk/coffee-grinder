module io.github.nonmilk.coffee.grinder {

    // javafx
    requires transitive javafx.graphics;

    // not transitive
    requires io.github.shimeoki.jfx.rasterization;
    requires io.github.traunin.triangulation;
    requires ru.vsu.cs.konygina_d.render_engine;

    // transitive
    requires transitive io.github.shimeoki.jshaper;
    requires transitive io.github.alphameo.linear_algebra;

    exports io.github.nonmilk.coffee.grinder;
}
