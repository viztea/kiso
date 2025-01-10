package kiso.log

public interface LoggerFactory {
    /**
     * Returns a logger with the specified name.
     */
    public fun get(name: String): Logger
}

public expect fun LoggingProvider.Companion.default(): LoggingProvider

public val ROOT_LOGGER_NAME: String
    get() = LoggingProvider.default().root_logger_name

public val Loggers: LoggerFactory
    get() = LoggingProvider.default().factory
