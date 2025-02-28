package kiso.log

import kiso.log.impl.DefaultLogAppender
import kiso.log.impl.DefaultLogFormatter
import kiso.log.impl.LevelMap
import kiso.log.impl.LogAppender
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmInline

public data object FallbackLoggingProvider : LoggingProvider {
    override val root_logger_name: String
        get() = LevelMap.ROOT_LEVEL_NAME

    override val factory: LoggerFactory
        get() = FallbackLoggers
}

public object FallbackLoggers : LoggerFactory {
    private val lock = SynchronizedObject()
    private val level_cache = mutableMapOf<String, LogLevel>()
    private val levels = LevelMap.empty()

    @Volatile
    public var appender: LogAppender = DefaultLogAppender(DefaultLogFormatter)

    override fun get(name: String): Logger =
        FallbackLogger(name)

    public fun set_level(name: String, level: LogLevel) {
        synchronized(lock) {
            levels[name] = level
            level_cache.remove(name)
        }
    }

    public fun get_level(name: String): LogLevel = synchronized(lock) {
        level_cache.getOrPut(name) { levels[name] }
    }
}

@JvmInline
public value class FallbackLogger(override val name: String) : Logger {
    override val level: LogLevel
        get() = FallbackLoggers.get_level(name)

    override fun emit_event(event: LogEvent) {
        if (is_enabled(event.level)) emit_event_unsafe(event)
    }

    override fun emit_event_unsafe(event: LogEvent) {
        FallbackLoggers.appender.append(event)
    }

    override fun is_enabled(level: LogLevel): Boolean =
        this.level.int_value >= level.int_value

}
