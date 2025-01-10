plugins {
    org.jetbrains.kotlin.jvm
}

kotlin {
    commonConfig()
}

dependencies {
    implementation(projects.kisoCore)
}
