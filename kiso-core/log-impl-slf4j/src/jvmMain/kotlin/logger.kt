package kiso.log.impl.slf4j

import kiso.log.LogEvent
import kiso.log.LogLevel
import kiso.log.Logger

@JvmInline
public value class Slf4jLoggerImpl(public val inner: org.slf4j.Logger) : Logger {
    override val name: String
        get() = inner.name

    override val level: LogLevel
        get() = when {
            inner.isTraceEnabled -> LogLevel.TRACE
            inner.isDebugEnabled -> LogLevel.DEBUG
            inner.isInfoEnabled  -> LogLevel.INFO
            inner.isWarnEnabled  -> LogLevel.WARN
            inner.isErrorEnabled -> LogLevel.ERROR
            else                 -> LogLevel.OFF
        }

    override fun emit_event(event: LogEvent) {
        if (is_enabled(event.level)) emit_event_unsafe(event)
    }

    override fun emit_event_unsafe(event: LogEvent) {
        inner.atLevel(event.level.to_slf4j())
            .setMessage(event.message)
            .setCause(event.cause)
            .log()
    }

    override fun is_enabled(level: LogLevel): Boolean = when (level) {
        LogLevel.ALL   -> true
        LogLevel.TRACE -> inner.isTraceEnabled
        LogLevel.DEBUG -> inner.isDebugEnabled
        LogLevel.INFO  -> inner.isInfoEnabled
        LogLevel.WARN  -> inner.isWarnEnabled
        LogLevel.ERROR -> inner.isErrorEnabled
        LogLevel.OFF   -> false
    }

}