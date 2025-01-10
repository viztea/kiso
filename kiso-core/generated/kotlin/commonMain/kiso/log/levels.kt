package kiso.log

import kiso.ext.format
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

//

@PublishedApi
internal inline fun Logger.level(level: LogLevel, cause: Throwable? = null, crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }

    if (!is_enabled(level)) return

    emit_event(level, true) {
        this.message = try {
            message().toString()
        } catch (e: Throwable) {
            "failed to stringify message <error: ${e.message}>"
        }

        this.cause = cause
    }
}

public inline fun Logger.trace(crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.TRACE) { message() }
}

public fun Logger.trace(message: String, vararg args: Any?) {
    trace { message.format(*args) }
}

public fun Logger.trace(cause: Throwable?, message: String, vararg args: Any?) {
    trace(cause) { message.format(*args) }
}

public fun Logger.trace(cause: Throwable?): Unit =
    emit_event(LogLevel.TRACE) { this.cause = cause }

public inline fun Logger.trace(cause: Throwable?, crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.TRACE, cause) { message() }
}

public inline fun Logger.debug(crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.DEBUG) { message() }
}

public fun Logger.debug(message: String, vararg args: Any?) {
    debug { message.format(*args) }
}

public fun Logger.debug(cause: Throwable?, message: String, vararg args: Any?) {
    debug(cause) { message.format(*args) }
}

public fun Logger.debug(cause: Throwable?): Unit =
    emit_event(LogLevel.DEBUG) { this.cause = cause }

public inline fun Logger.debug(cause: Throwable?, crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.DEBUG, cause) { message() }
}

public inline fun Logger.info(crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.INFO) { message() }
}

public fun Logger.info(message: String, vararg args: Any?) {
    info { message.format(*args) }
}

public fun Logger.info(cause: Throwable?, message: String, vararg args: Any?) {
    info(cause) { message.format(*args) }
}

public fun Logger.info(cause: Throwable?): Unit =
    emit_event(LogLevel.INFO) { this.cause = cause }

public inline fun Logger.info(cause: Throwable?, crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.INFO, cause) { message() }
}

public inline fun Logger.warn(crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.WARN) { message() }
}

public fun Logger.warn(message: String, vararg args: Any?) {
    warn { message.format(*args) }
}

public fun Logger.warn(cause: Throwable?, message: String, vararg args: Any?) {
    warn(cause) { message.format(*args) }
}

public fun Logger.warn(cause: Throwable?): Unit =
    emit_event(LogLevel.WARN) { this.cause = cause }

public inline fun Logger.warn(cause: Throwable?, crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.WARN, cause) { message() }
}

public inline fun Logger.error(crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.ERROR) { message() }
}

public fun Logger.error(message: String, vararg args: Any?) {
    error { message.format(*args) }
}

public fun Logger.error(cause: Throwable?, message: String, vararg args: Any?) {
    error(cause) { message.format(*args) }
}

public fun Logger.error(cause: Throwable?): Unit =
    emit_event(LogLevel.ERROR) { this.cause = cause }

public inline fun Logger.error(cause: Throwable?, crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level(LogLevel.ERROR, cause) { message() }
}