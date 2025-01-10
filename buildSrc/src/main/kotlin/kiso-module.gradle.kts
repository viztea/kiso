import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    id("com.vanniktech.maven.publish.base")

    org.jetbrains.kotlin.multiplatform
    org.jetbrains.kotlin.plugin.serialization
    org.jetbrains.kotlinx.atomicfu
    org.jetbrains.dokka
}

group = "gay.vzt.kiso"

version = "0.0.0-SNAPSHOT"

/* targets */
kotlin {
    explicitApi()

    commonConfig()
}

publishing {
    repositories {
        maven("https://maven.dimensional.fun/releases") {
            name = "dimensional"

            credentials.username = System.getenv("REPO_ALIAS")
            credentials.password = System.getenv("REPO_TOKEN")
        }
    }
}

mavenPublishing {
    coordinates("gay.vzt.kiso", project.name)

    if (plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
        configure(KotlinMultiplatform(javadocJar = JavadocJar.Dokka("dokkaHtml")))
    }
}
