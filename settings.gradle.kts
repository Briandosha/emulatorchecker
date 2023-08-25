pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        gradlePluginPortal()

        jcenter()
        maven ( url = "https://oss.sonatype.org/content/repositories/snapshots/" )


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")

        jcenter()
        maven ( url = "https://oss.sonatype.org/content/repositories/snapshots/" )

    }
}


rootProject.name = "Emulator Detector"
include(":app")
include(":emulatorchecker")
