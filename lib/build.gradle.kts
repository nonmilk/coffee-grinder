plugins {
    id("java-library")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "io.github.nonmilk.coffee"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.shimeoki.jfx:rasterization:2.0.1")
    implementation("io.github.shimeoki:jshaper:0.9.6")
    implementation("io.github.traunin:triangulation:1.1.0")
    implementation("io.github.alphameo:linear_algebra:1.0.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
