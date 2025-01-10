package kiso.ext

import kotlin.coroutines.cancellation.CancellationException

public actual fun Throwable.is_fatal(): Boolean =
    this is CancellationException

public actual open class ControlFlowException : RuntimeException {
    public actual constructor() : super()

    public actual constructor(cause: Throwable?) : super(cause)

    public actual constructor(message: String?) : super(message)

    public actual constructor(message: String?, cause: Throwable?) : super(message, cause)
}
