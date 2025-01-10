package kiso.log.impl.log4j

import kiso.log.LoggerFactory
import kiso.log.LoggingProvider
import org.apache.logging.log4j.LogManager

public class Log4jLoggingProvider : LoggingProvider {
    override val root_logger_name: String
        get() = LogManager.ROOT_LOGGER_NAME

    override val factory: LoggerFactory
        get() = Log4jLoggerFactory
}
