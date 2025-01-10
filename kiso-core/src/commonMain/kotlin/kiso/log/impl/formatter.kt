package kiso.log.impl

import com.github.ajalt.colormath.model.Ansi16
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles
import kiso.common.now
import kiso.log.LogEvent
import kiso.log.LogLevel
import kiso.text.format.pkg.PackageName
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

/**
 *
 */
public interface LogFormatter {
    /**
     *
     */
    public fun format(event: LogEvent): String
}

public const val PACKAGE_NAME_LENGTH: Int = 40

public data object DefaultLogFormatter : LogFormatter {
    override fun format(event: LogEvent): String = event.default_format()
}

public fun LogEvent.default_format(): String = buildString {
    val time = (timestamp ?: now()).format(DateTimeComponents.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        dayOfMonth()
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
        char('.')
        secondFraction(3)
    })

    append((TextColors.black + TextStyles.bold)(time))
    append(" ")

    thread_name
        ?.takeUnless { it.isBlank() }
        ?.padEnd(24, ' ')
        ?.take(24)
        ?.let { TextColors.magenta(it) }
        ?.let { append("$it ") }

    val logger_name = when {
        logger.length <= PACKAGE_NAME_LENGTH ->
            logger

        PackageName.is_valid(logger)         ->
            PackageName.of(logger).abbreviate(PACKAGE_NAME_LENGTH).value

        else                                 ->
            logger.take(PACKAGE_NAME_LENGTH)
    }

    logger_name.padEnd(PACKAGE_NAME_LENGTH, ' ')
        .take(PACKAGE_NAME_LENGTH)
        .let(TextColors.cyan::invoke)
        .let { append("$it ") }

    val color = when (level) {
        LogLevel.INFO  -> TextColors.blue
        LogLevel.WARN  -> TextColors.red
        LogLevel.ERROR -> TextColors.red + TextStyles.bold
        else           -> TextStyle(Ansi16(39))
    }

    append("${color(level.name.padEnd(6, ' '))} ")

    append(message)

    cause?.let { append("\n" + it.stackTraceToString()) }

    if (LOG_SHOULD_WRITE_ENDING) {
        append("\n")
    }
}
