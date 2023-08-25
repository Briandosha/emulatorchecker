buildscript {
//    ext {
//        val kotlin_version = "1.6.0"
//        val room_version = "2.3.0"
//    }
//
//    dependencies {
//        classpath("com.android.tools.build:gradle:3.4.0")

//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
//    }

    repositories {
        google()
        mavenCentral()

        maven(url = "https://jitpack.io")


        jcenter()
        maven ( url = "https://oss.sonatype.org/content/repositories/snapshots/" )
    }
}



// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.0" apply false
}