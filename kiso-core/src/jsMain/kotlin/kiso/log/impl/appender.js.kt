package kiso.log.impl

import kiso.log.LogLevel
import kiso.log.is_error

// TODO: check for node.js environment and use the Process object instead of Console.

public actual val LOG_SHOULD_WRITE_ENDING: Boolean
    get() = false

public actual fun print(level: LogLevel, message: String) {
    if (level.is_error()) {
        console.error(message)
    } else {
        console.log(message)
    }
}
