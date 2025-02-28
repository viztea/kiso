plugins {
    `kiso-module`
}

version = "0.0.3"

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.kisoCore)
    }

    sourceSets.jvmMain.dependencies {
        implementation("org.slf4j:slf4j-api:2.0.3")
    }
}
