package kiso.log

public interface Logger {
    /**
     * The name of this logger.
     */
    public val name: String

    /**
     * The log level of this logger.
     */
    public val level: LogLevel

    /**
     * Logs the specified event.
     * Use [emit_event_unsafe] if you want to avoid the overhead of checking if the log level is enabled.
     */
    public fun emit_event(event: LogEvent)

    /**
     * Logs the specified event without checking if the log level is enabled.
     * This method is intended to be used by the logger implementation to avoid the overhead of checking if the log level is enabled.
     * Note: not all logger implementations support this method.
     */
    public fun emit_event_unsafe(event: LogEvent)

    /**
     * Returns `true` if the specified log level is enabled for this logger.
     */
    public fun is_enabled(level: LogLevel): Boolean
}
