package kiso.log

// TODO: customization

public actual fun LoggingProvider.Companion.default(): LoggingProvider =
    FallbackLoggingProvider
