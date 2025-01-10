plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
    mavenCentral()
    gradlePluginPortal()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    mavenLocal()
}

kotlin {
    jvmToolchain(19)
}

dependencies {
    implementation(libs.gradle.serialization.plugin)
    implementation(libs.gradle.kotlin.plugin)
    implementation(libs.gradle.atomicfu.plugin)
    implementation(libs.gradle.ksp.plugin)
    implementation(libs.gradle.dokka.plugin)
    implementation(libs.gradle.maven.publish.plugin)
}
