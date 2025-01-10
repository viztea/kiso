package kiso.common

public class UnreachableException : Error("Shouldn't have reached this point")

/**
 * Throws an [AssertionError] if this value is `false`.
 */
public infix fun Boolean.assert(message: String? = null) {
    if (!this) throw AssertionError(message)
}

/**
 * Throws an [UnreachableException].
 */
public fun unreachable(): Nothing = throw UnreachableException()

/**
 * Throws an [UnsupportedOperationException] with the specified [message].
 */
public fun unsupported(message: String? = null): Nothing =
    throw UnsupportedOperationException(message)
