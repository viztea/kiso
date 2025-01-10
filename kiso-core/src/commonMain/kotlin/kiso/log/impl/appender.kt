package kiso.log.impl

import kiso.log.LogEvent
import kiso.log.LogLevel

/**
 *
 */
public interface LogAppender {
    /**
     *
     */
    public fun append(event: LogEvent)

    /**
     * A simple appender that prints to the console.
     */
    public data object Simple : LogAppender {
        var first: Boolean = true

        override fun append(event: LogEvent) {
            if (first) {
                first = false
                println("No appender configured. Using default appender.")
            }

            print(event.level, event.message.toString())

            event.cause?.printStackTrace()
        }
    }
}

/**
 * Whether to write
 */
public expect val LOG_SHOULD_WRITE_ENDING: Boolean

internal expect fun print(
    level: LogLevel,
    message: String
)

public class DefaultLogAppender(formatter: LogFormatter) : FormattingLogAppender(formatter) {
    override fun append_formatted(event: LogEvent, formatted: String) {
        print(event.level, formatted)
    }
}

public abstract class FormattingLogAppender(public val formatter: LogFormatter) : LogAppender {
    protected abstract fun append_formatted(event: LogEvent, formatted: String)

    final override fun append(event: LogEvent) {
        append_formatted(event, formatter.format(event))
    }
}
