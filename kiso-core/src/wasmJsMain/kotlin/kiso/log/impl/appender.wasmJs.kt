package kiso.log.impl

import kiso.log.LogLevel

public actual val LOG_SHOULD_WRITE_ENDING: Boolean
    get() = true

public actual fun print(level: LogLevel, message: String) {
    print(message)
}