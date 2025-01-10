package kiso.log.impl.log4j

import kiso.log.LogLevel
import org.apache.logging.log4j.Level

public fun LogLevel.to_log4j(): Level = when (this) {
    LogLevel.TRACE -> Level.TRACE
    LogLevel.DEBUG -> Level.DEBUG
    LogLevel.INFO  -> Level.INFO
    LogLevel.WARN  -> Level.WARN
    LogLevel.ERROR -> Level.ERROR
    LogLevel.OFF   -> Level.OFF
    LogLevel.ALL   -> Level.ALL
}

public fun Level.to_kiso(): LogLevel = when (this) {
    Level.TRACE -> LogLevel.TRACE
    Level.DEBUG -> LogLevel.DEBUG
    Level.INFO  -> LogLevel.INFO
    Level.WARN  -> LogLevel.WARN
    Level.ERROR -> LogLevel.ERROR
    Level.OFF   -> LogLevel.OFF
    Level.ALL   -> LogLevel.ALL
    else        -> error("unsupported log level: $this")
}
