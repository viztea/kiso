plugins {
    `kiso-module`
}

version = "0.0.3"

kotlin {
    sourceSets.commonMain {
        kotlin.srcDir(project.layout.projectDirectory.dir("generated/kotlin/commonMain"))

        dependencies {
            api(libs.kotlin.datetime)

            implementation(libs.mordant.core)
        }
    }
}
