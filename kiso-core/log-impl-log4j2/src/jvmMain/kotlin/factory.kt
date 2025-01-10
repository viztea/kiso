package kiso.log.impl.log4j

import kiso.log.Logger
import kiso.log.LoggerFactory
import org.apache.logging.log4j.LogManager

/**
 * A [LoggerFactory] that is able to create [Logger] instances using the Log4j API.
 */
public object Log4jLoggerFactory : LoggerFactory {
    override fun get(name: String): Logger = Log4jLoggerImpl(LogManager.getLogger(name))
}
