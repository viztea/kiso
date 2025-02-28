package kiso.log.impl.slf4j

import kiso.log.LogLevel
import org.slf4j.event.Level

public fun LogLevel.to_slf4j(): Level = when (this) {
    LogLevel.TRACE -> Level.TRACE
    LogLevel.DEBUG -> Level.DEBUG
    LogLevel.INFO  -> Level.INFO
    LogLevel.WARN  -> Level.WARN
    LogLevel.ERROR -> Level.ERROR
    else           -> error("unsupported log level: $this")
}

public fun Level.to_kiso(): LogLevel = when (this) {
    Level.TRACE -> LogLevel.TRACE
    Level.DEBUG -> LogLevel.DEBUG
    Level.INFO  -> LogLevel.INFO
    Level.WARN  -> LogLevel.WARN
    Level.ERROR -> LogLevel.ERROR
}
