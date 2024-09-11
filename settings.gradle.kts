pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Google 저장소 유지
        mavenCentral() // Maven Central 저장소 유지
        maven("https://jitpack.io") // JitPack 저장소 추가
    }
}


rootProject.name = "Miruni"
include(":app")
 