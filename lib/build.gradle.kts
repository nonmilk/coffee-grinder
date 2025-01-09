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

    implementation("io.github.shimeoki.jfx:rasterization:3.0.0")
    implementation("io.github.traunin:triangulation:1.1.1")

    api("io.github.shimeoki:jshaper:0.15.0")
    api("io.github.alphameo:linear_algebra:2.1.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
}

