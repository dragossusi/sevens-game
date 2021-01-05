// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenLocal()
    }
}

plugins {
    kotlin("multiplatform") version Versions.kotlin apply false
    kotlin("plugin.serialization") version Versions.kotlin apply false
}

allprojects {
    repositories {
        mavenLocal()
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}