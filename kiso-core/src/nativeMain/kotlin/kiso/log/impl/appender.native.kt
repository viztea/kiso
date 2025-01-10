package kiso.log.impl

import kiso.log.LogLevel
import kiso.log.is_error
import platform.posix.fflush
import platform.posix.fprintf
import platform.posix.stderr

public actual val LOG_SHOULD_WRITE_ENDING: Boolean
    get() = true

internal actual fun print(level: LogLevel, message: String) {
    if (level.is_error()) {
        fprintf(stderr, message)
        fflush(stderr)
    } else {
        print(message)
    }
}
