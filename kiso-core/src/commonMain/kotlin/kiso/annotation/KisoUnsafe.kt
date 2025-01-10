package kiso.annotation

/**
 * Marks an API as unsafe to use.
 */
@RequiresOptIn(
    message = "This API is considered unsafe and should not be used directly.",
    level = RequiresOptIn.Level.ERROR
)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.BINARY)
public annotation class KisoUnsafe
