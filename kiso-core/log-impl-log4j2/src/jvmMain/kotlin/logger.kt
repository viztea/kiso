package kiso.log.impl.log4j

import kiso.log.LogEvent
import kiso.log.LogLevel
import kiso.log.Logger
import org.apache.logging.log4j.message.MessageFactory
import org.apache.logging.log4j.Logger as Log4jLogger

public class Log4jLoggerImpl(public val logger: Log4jLogger) : Logger {
    override val name: String
        get() = logger.name

    override val level: LogLevel
        get() = logger.level.to_kiso()

    override fun is_enabled(level: LogLevel): Boolean = when (level) {
        LogLevel.OFF   -> false
        LogLevel.ALL   -> true
        LogLevel.TRACE -> logger.isTraceEnabled
        LogLevel.DEBUG -> logger.isDebugEnabled
        LogLevel.INFO  -> logger.isInfoEnabled
        LogLevel.WARN  -> logger.isWarnEnabled
        LogLevel.ERROR -> logger.isErrorEnabled
    }

    override fun emit_event(event: LogEvent) {
        logger.atLevel(event.level.to_log4j())
            .withThrowable(event.cause)
            .log(event.message)
    }

    override fun emit_event_unsafe(event: LogEvent) {
        val message = logger
            .getMessageFactory<MessageFactory>()
            .newMessage(event.message)

        logger.logMessage(event.level.to_log4j(), null, FQCN, null, message, event.cause)
    }

    public companion object {
        private val FQCN = Log4jLoggerImpl::class.qualifiedName
    }
}
