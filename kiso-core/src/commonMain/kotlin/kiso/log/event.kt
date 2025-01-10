package kiso.log

import kiso.platform.current_thread_name
import kotlinx.datetime.Instant
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public data object LogLevels {
    public const val ALL_INT: Int = Int.MAX_VALUE

    public const val TRACE_INT: Int = 50

    public const val DEBUG_INT: Int = 40

    public const val INFO_INT: Int = 30

    public const val WARN_INT: Int = 20

    public const val ERROR_INT: Int = 10

    public const val OFF_INT: Int = 0
}

public enum class LogLevel(public val int_value: Int) {
    ALL(LogLevels.ALL_INT),
    TRACE(LogLevels.TRACE_INT),
    DEBUG(LogLevels.DEBUG_INT),
    INFO(LogLevels.INFO_INT),
    WARN(LogLevels.WARN_INT),
    ERROR(LogLevels.ERROR_INT),
    OFF(LogLevels.OFF_INT),
    ;
}

public fun LogLevel.is_error(): Boolean = this == LogLevel.ERROR

/**
 *
 */
public interface LogEvent {
    public val logger: String

    /**
     * The level at which the event was logged.
     */
    public val level: LogLevel

    /**
     * The name of the thread that logged the event.
     */
    public val thread_name: String?

    /**
     *
     */
    public val message: String?

    /**
     * The timestamp of when the event was captured.
     */
    public val timestamp: Instant?

    /**
     * The
     */
    public val cause: Throwable?
}

/**
 *
 */
public inline fun Logger.emit_event(
    level: LogLevel,
    unsafe: Boolean = false,
    block: MutableLogEvent.() -> Unit,
) {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
    if (!unsafe && !is_enabled(level)) return
    val event = MutableLogEvent(logger = name, level = level).apply(block)
    event.thread_name = current_thread_name()
    emit_event_unsafe(event)
}

/**
 *
 */
public data class MutableLogEvent(
    override var logger: String,
    override var level: LogLevel,
    override var message: String? = null,
    override var thread_name: String? = null,
    override var timestamp: Instant? = null,
    override var cause: Throwable? = null,
) : LogEvent
