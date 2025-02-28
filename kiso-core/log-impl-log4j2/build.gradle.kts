plugins {
    `kiso-module`
}

version = "0.0.3"

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.kisoCore)
    }

    sourceSets.jvmMain.dependencies {
        implementation("org.apache.logging.log4j:log4j-api:2.24.0")
    }
}
