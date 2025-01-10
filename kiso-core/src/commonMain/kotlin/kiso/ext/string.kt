package kiso.ext

import kiso.text.format.Format

/**
 * Format a string with the given arguments.
 */
public fun String.format(vararg args: Any?): String =
    Format.compile(this).apply(*args)
