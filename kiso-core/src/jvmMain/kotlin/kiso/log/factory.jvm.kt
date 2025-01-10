package kiso.log

import kiso.ext.service_for_opt

private val DEFAULT_PROVIDER: LoggingProvider by lazy {
    var available = service_for_opt<LoggingProvider>()
    if (available == null) {
        System.err.println("Using fallback logging provider...")
        available = FallbackLoggingProvider
    }

    available
}

public actual fun LoggingProvider.Companion.default(): LoggingProvider =
    DEFAULT_PROVIDER
