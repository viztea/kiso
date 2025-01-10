package kiso.common

import kiso.annotation.KisoUnsafe
import kotlin.jvm.JvmInline

public fun cs(char: Char, vararg chars: Char): CharSequence = CharSequence(char, *chars)

public fun CharSequence(char: Char, vararg other: Char): CharSequence {
    return if (other.isEmpty()) SingleCharSequence(char)
    else SimpleCharSequence(charArrayOf(char, *other))
}

@JvmInline
public value class SimpleCharSequence(private val storage: CharArray) : CharSequence {
    override val length: Int
        get() = storage.size

    override fun get(index: Int): Char {
        return storage[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return SimpleCharSequence(storage.copyOfRange(startIndex, endIndex))
    }

    override fun toString(): String = storage.concatToString()
}

/**
 * Creates a [CharSequence] from the given [CharArray].
 * This function copies the array to ensure immutability.
 */
public fun CharArray.toCharSequence(): CharSequence =
    SimpleCharSequence(copyOf())

/**
 * Creates a [CharArray] from the given [CharSequence] without copying the array.
 * This function is unsafe because it allows the caller to modify the array.
 */
@KisoUnsafe
public fun unsafe_to_char_array(array: CharArray): CharSequence =
    SimpleCharSequence(array)

/**
 * Creates a [CharSequence] from the given [CharSequence].
 */
@JvmInline
public value class SingleCharSequence(private val value: Char) : CharSequence {
    override val length: Int
        get() = 1

    override fun get(index: Int): Char {
        if (index != 0) throw IndexOutOfBoundsException("Index: $index, Size: 1")
        return value
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        if (startIndex == 0 && endIndex == 1) return this
        throw IndexOutOfBoundsException("Start: $startIndex, End: $endIndex, Size: 1")
    }

    override fun toString(): String = value.toString()
}

/**
 * Creates a [CharSequence] from the given [CharSequence].
 */
public data class RepeatedCharSequence(private val value: Char, override val length: Int) : CharSequence {
    override fun get(index: Int): Char {
        if (index !in 0..<length) throw IndexOutOfBoundsException("Index: $index, Size: 1")
        return value
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        if (startIndex > 0 && endIndex <= length) return this
        throw IndexOutOfBoundsException("Start: $startIndex, End: $endIndex, Size: 1")
    }

    override fun toString(): String = value.toString().repeat(length)
}
