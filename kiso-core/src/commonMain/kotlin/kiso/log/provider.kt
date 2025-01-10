package kiso.log

public interface LoggingProvider {
    /**
     * The name of the root logger.
     */
    public val root_logger_name: String

    /**
     * The [LoggerFactory] that is able to create [Logger] instances using the underlying logging system.
     */
    public val factory: LoggerFactory

    public companion object;
}
