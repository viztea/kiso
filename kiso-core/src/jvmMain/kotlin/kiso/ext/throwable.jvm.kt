package kiso.ext

import kotlin.coroutines.cancellation.CancellationException

@Suppress("removal", "DEPRECATION")
public actual fun Throwable.is_fatal(): Boolean = when (root_cause) {
    // jvm
    is VirtualMachineError, is ThreadDeath,
    is LinkageError, is InterruptedException,
        -> true
    // kotlin
    is CancellationException -> true
    //
    else                     -> false
}

public actual open class ControlFlowException : RuntimeException {
    public actual constructor() : super(null, null, false, false)
    public actual constructor(cause: Throwable?) : super(null, cause, false, false)
    public actual constructor(message: String?) : super(message, null, false, false)
    public actual constructor(message: String?, cause: Throwable?) : super(message, cause, false, false)
}