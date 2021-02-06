plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

group = "ro.dragossusi.sevens"
version = Versions.app

kotlin {

    jvm()
    js(BOTH) {
        nodejs()
    }
    ios{
        binaries.framework {
            baseName = "SevensGame"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")

                //modules
                implementation("ro.dragossusi.sevens:payload:${Versions.app}")
                implementation("ro.dragossusi:logger:${Versions.app}")
                implementation("ro.dragossusi.sevens:game-listener:${Versions.app}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {

        }
        val jsMain by getting {

        }
    }
}

publishing {
    publications {
        publications.withType<MavenPublication> {
            pom {
                name.set("Sevens Game")
                description.set("Game classes used in sevens")
                url.set("http://www.dragossusi.ro/sevens")
            }
        }
    }
}

apply<PublishPlugin>()