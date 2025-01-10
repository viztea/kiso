# Core - [`kiso.log`](../kiso-core/src/commonMain/kotlin/kiso/log)

a simple logging api based on kotlin-logging

## Features

- Functions for logging at different levels and with different parameters.
- Levels include `trace`, `debug`, `info`, `warn`, and `error`.
- Lazy *inlined* evaluation of log messages.
- Built-in support for logging exceptions.
- Built-in support for [formatting](./format.md)

## Usage

```kt
import kiso.common.logger
import kiso.log.info
import kiso.log.debug
import kiso.log.trace
import kiso.log.error
import kiso.log.warn

fun main() {
    val log = logger("main")

    log.info { "this is an info log" }

    log.debug("this is also a %s log!", "debug")

    log.error(Throwable()) { "this is an error log too" }

    log.trace(Throwable(), "this is an %s log too", "trace")
}

class MyClass {
    fun something() {
        LOG.info { "this is from MyClass!" }
    }

    companion object {
        val LOG = logger<MyClass>()
    }
}
```
