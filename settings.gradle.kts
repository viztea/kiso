rootProject.name = "kiso-root"

include(":codegen")
include(":kiso-core")
include(":kiso-log")
include(":kiso-iort")
include(":kiso-http")
include(":kiso-tracing")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
