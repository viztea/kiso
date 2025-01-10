allprojects {
    repositories {
        mavenLocal()
        maven("https://maven.dimensional.fun/snapshots")
        maven("https://maven.dimensional.fun/releases")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
        maven("https://jitpack.io")
    }
}
