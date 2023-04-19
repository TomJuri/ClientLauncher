plugins {
    kotlin("jvm") version "1.8.20"
    id("maven-publish")
}

group = "de.tomjuri"
version = "1.0.0"

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            group = "de.tomjuri"
            artifactId = "clientlauncher"
            version = "1.0.0"
            from(components["java"])
        }
    }
}

kotlin {
    jvmToolchain(8)
}