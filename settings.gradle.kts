rootProject.name = "kiso-root"

include(":codegen")
include(":kiso-core")
include(":kiso-core:log-impl-log4j2")
include(":kiso-core:log-impl-slf4j")
include(":kiso-iort")
include(":kiso-http")
include(":kiso-tracing")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
