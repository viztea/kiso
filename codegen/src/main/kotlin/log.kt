import kiso.log.LogLevel

fun main() {
    fun code(level: LogLevel): String {
        val method_name = level.name.lowercase()

        val reference = "LogLevel.${level.name}"

        return """
public inline fun Logger.$method_name(crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level($reference) { message() }
}

public fun Logger.$method_name(message: String, vararg args: Any?) {
    $method_name { message.format(*args) } 
}

public fun Logger.$method_name(cause: Throwable?, message: String, vararg args: Any?) {
    $method_name(cause) { message.format(*args) } 
}

public fun Logger.$method_name(cause: Throwable?): Unit =
    emit_event($reference) { this.cause = cause }

public inline fun Logger.$method_name(cause: Throwable?, crossinline message: () -> Any?) {
    contract { callsInPlace(message, InvocationKind.AT_MOST_ONCE) }
    level($reference, cause) { message() }
}""".trimIndent()
    }


    LogLevel.entries
        .filterNot { it == LogLevel.ALL || it == LogLevel.OFF }
        .joinToString("\n") { code(it) }.also(::println)
}
