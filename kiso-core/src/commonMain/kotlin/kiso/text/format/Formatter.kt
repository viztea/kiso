package kiso.text.format

// TODO: locale stuff.

/**
 *
 */
public data class Formatter(@PublishedApi internal val a: Appendable) : AutoCloseable {
    private var is_closed: Boolean = false

    /**
     * The underlying [Appendable] that this [Formatter] writes to.
     */
    inline val output: Appendable
        get() {
            ensure_open()
            return a
        }

    /**
     *
     */
    public fun format(format: String, vararg arguments: Any?): Unit =
        format_args(format, arguments)

    /**
     *
     */
    public fun format_args(format: String, args: Array<out Any?>) {
        ensure_open()
        Format.compile(format).apply_args(this, args)
    }

    override fun toString(): String {
        ensure_open()
        return a.toString()
    }

    /**
     * Attempts to close the underlying [Appendable].
     * Further calls to [format] will be ignored.
     */
    override fun close() {
        if (is_closed || a !is AutoCloseable) return

        try {
            a.close()
        } finally {
            is_closed = true
        }
    }

    @PublishedApi
    internal fun ensure_open() {
        if (!is_closed) return
        throw FormatterClosedException()
    }
}
