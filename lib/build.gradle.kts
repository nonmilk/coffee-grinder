plugins {
    id("java-library")
}

group = "io.github.nonmilk.coffee"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
