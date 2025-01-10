package kiso.ext

/**
 * @return the root cause of this throwable.
 */
public val Throwable.root_cause: Throwable
    get() {
        var t = this
        while (t.cause != null) t = t.cause ?: break

        return t
    }

/**
 * Determines whether this [Throwable] is a fatal exception.
 *
 * On the JVM the following exceptions are matched: `VirtualMachineError`
 *  (for example, `OutOfMemoryError` and `StackOverflowError`, subclasses of `VirtualMachineError`), `ThreadDeath`,
 *  `LinkageError`, `InterruptedException`.
 *
 * This will match Kotlin [kotlin.coroutines.cancellation.CancellationException]s as well.
 */
public expect fun Throwable.is_fatal(): Boolean

/**
 * An exception that is not meant to be used as an exception...
 * It is primarily used for control flow or to provide context to an exception.
 *
 * On the JVM, this class does not have a stack trace and cannot have suppressed causes.
 */
public expect open class ControlFlowException : RuntimeException {
    public constructor()

    public constructor(cause: Throwable?)

    public constructor(message: String?)

    public constructor(message: String?, cause: Throwable?)
}
