package kiso.log.impl

import kiso.log.LogLevel
import kiso.log.is_error

public actual val LOG_SHOULD_WRITE_ENDING: Boolean
    get() = true

internal actual fun print(level: LogLevel, message: String) {
    if (level.is_error()) System.err.print(message) else print(message)
}
