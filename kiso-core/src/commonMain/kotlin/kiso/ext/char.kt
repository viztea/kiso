package kiso.ext

import kiso.common.RepeatedCharSequence

/**
 * Returns a string containing the characters of this character repeated [count] times.
 */
public fun Char.repeat(count: Int): CharSequence = when (count) {
    0    -> ""
    1    -> this.toString()
    else -> RepeatedCharSequence(this, count)
}
