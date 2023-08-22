buildscript {
//    ext {
//        val kotlin_version = "1.6.0"
//        val room_version = "2.3.0"
//    }
//
//    dependencies {
//        classpath("com.android.tools.build:gradle:3.4.0")
//        classpath("com.preemptive.dasho:dasho-android:1.4.+")
//        classpath("com.google.gms:google-services:4.3.14")
//        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
//        classpath(group = "com.smileidentity", name = "smile-id-android", version = "1.0.1")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
//    }

    repositories {
        mavenCentral()
        maven(url = "https://maven.preemptive.com/")
    }
}



// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.0" apply false
}