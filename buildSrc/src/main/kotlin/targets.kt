import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultCInteropSettings
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val DEFAULT_OPT_INS = listOf(
    "kotlin.ExperimentalStdlibApi",
    "kotlin.ExperimentalUnsignedTypes",
    "kotlin.uuid.ExperimentalUuidApi",
    "kotlin.io.encoding.ExperimentalEncodingApi",
    "kotlin.experimental.ExperimentalNativeApi",
    "kotlin.contracts.ExperimentalContracts",
    "kotlinx.cinterop.ExperimentalForeignApi",
    "kotlinx.serialization.ExperimentalSerializationApi",
    "kotlinx.serialization.InternalSerializationApi",
    "kotlinx.coroutines.DelicateCoroutinesApi",
    "kotlinx.coroutines.FlowPreview",
    "kotlinx.serialization.json.internal.JsonFriendModuleApi"
)

val DEFAULT_JAVA_TARGET = JvmTarget.JVM_19
const val DEFAULT_JAVA_VERSION_STR = "19"
const val DEFAULT_JAVA_VERSION_INT = 19

fun KotlinCommonCompilerOptions.commonCompilerOptions() {
    progressiveMode.set(true)
    freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xwhen-guards", "-Xjvm-default=all")
    optIn.addAll(DEFAULT_OPT_INS)
}

fun KotlinJvmExtension.commonConfig() {
    compilerOptions {
        commonCompilerOptions()
        jvmTarget.set(DEFAULT_JAVA_TARGET)
    }

    jvmToolchain(DEFAULT_JAVA_VERSION_INT)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
fun KotlinMultiplatformExtension.commonConfig(
    js: Boolean = true,
    jvm: Boolean = true,
    native: Boolean = true,
) {
    compilerOptions {
        commonCompilerOptions()
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("nix") {
                withLinux()
                withMacos()
            }

            group("posix") {
                withMingw()
                withLinux()
            }

            group("jsAndWasm") {
                withJs()
                withWasmJs()
            }

            group("nonJs") {
                withJvm()
                withNative()
            }

            group("nonJvm") {
                withJs()
                withWasmJs()
                withNative()
            }
        }
    }

    if (js) {
        js {
            useCommonJs()

            nodejs()
            browser()
        }

        wasmJs {
            useCommonJs()

            nodejs()
            browser()
        }
    }

    if (jvm) {
        jvm {
            withJava()

            compilerOptions {
                jvmTarget.set(DEFAULT_JAVA_TARGET)
            }
        }

        jvmToolchain(DEFAULT_JAVA_VERSION_INT)
    }

    if (native) {
        /* linux */
        linuxX64()
        linuxArm64()

        /* windows */
        mingwX64()
    }

    /**/
}

fun KotlinMultiplatformExtension.cinterop(
    name: String,
    cinterop_targets: List<String>? = null,
    block: DefaultCInteropSettings.() -> Unit,
) {
    fun KotlinNativeTarget.create() = compilations["main"].cinterops.create(name) {
        block()
    }

    if (cinterop_targets == null) {
        targets.withType<KotlinNativeTarget> { create() }
    } else {
        cinterop_targets
            .mapNotNull { targets.findByName(it) }
            .filterIsInstance<KotlinNativeTarget>()
            .forEach { it.create() }
    }
}
