package kiso.text.format

import kiso.common.unreachable
import kiso.ext.repeat

/**
 * Applies the correct casing to the specified character.
 */
internal fun casing(ctx: Format.Specifier.Options, char: Char): Char = when {
    ConverterFlag.UPPER_CASE in ctx.flags -> char.uppercaseChar()
    ConverterFlag.LOWER_CASE in ctx.flags -> char.lowercaseChar()
    else                                  -> char
}

/**
 * Applies the correct casing to the specified string.
 */
internal fun casing(ctx: Format.Specifier.Options, str: String): String = when {
    ConverterFlag.UPPER_CASE in ctx.flags -> str.uppercase()
    ConverterFlag.LOWER_CASE in ctx.flags -> str.lowercase()
    else                                  -> str
}

/**
 * Prints the specified value to the formatter output while applying the specified options.
 */
internal fun print(
    ctx: Format.Specifier.Options,
    fmt: Formatter,
    value: CharSequence,
    offset: Int = 0,
) {
    // if no width or precision is set, just append the value.
    if (ctx.precision == null && ctx.width == null) {
        fmt.output.append(value)
        return
    }

    // if the precision is set, truncate the output to the specified length.
    val str = ctx.precision
        ?.let { value.take(it) }
        ?: value

    // justify the output if a width is set.
    if (ctx.width == null || ctx.width < (offset + str.length)) {
        fmt.output.append(str)
        return
    }

    val padding = ' '.repeat(ctx.width - str.length - offset)

    // if the output is left-justified, append the padding first.
    if (ConverterFlag.LEFT_JUSTIFIED in ctx.flags) {
        fmt.output.append(padding, str)
    } else {
        fmt.output.append(str, padding)
    }
}

internal fun Number.as_long(decimal: Boolean): Long {
    var value = when (this) {
        is Long  -> this
        is Int   -> toLong()
        is Short -> toLong()
        is Byte  -> toLong()
        else     -> unreachable()
    }

    if (this !is Long && value < 0 && !decimal) {
        val offset = when (this) {
            is Int   -> 32
            is Short -> 16
            is Byte  -> 8
            else     -> unreachable()
        }

        value += 1 shl offset
    }

    return value
}