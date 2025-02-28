package kiso.log.impl.slf4j

import kiso.log.LoggerFactory
import kiso.log.LoggingProvider

public class Slf4jLoggingProvider : LoggingProvider {
    override val root_logger_name: String
        get() = org.slf4j.LoggerFactory.getILoggerFactory().
    override val factory: LoggerFactory
        get() = TODO("Not yet implemented")

}