plugins {
    kotlin("multiplatform")
    `maven-publish`
    signing
}

kotlin {

    jvm()
    js(BOTH) {
        nodejs()
    }
    ios{
        binaries.framework {
            baseName = "SevensAi"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

                // modules
                implementation("ro.dragossusi.sevens:payload:${Versions.app}")
                implementation("ro.dragossusi:logger:${Versions.app}")
                implementation("ro.dragossusi.sevens:game-listener:${Versions.app}")

                //local
                implementation(project(":game"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
    }
}

publishing {
    publications {
        publications.withType<MavenPublication> {
            pom {
                name.set("Sevens AI")
                description.set("AI classes used in sevens")
                url.set("http://www.dragossusi.ro/sevens")
            }
        }
    }
}

apply<PublishPlugin>()