package kiso.common

import kiso.log.Logger
import kiso.log.Loggers
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

public expect fun logger(func: KClass<*>, name: String? = null): Logger

public fun logger(name: String? = null): Logger =
    Loggers.get(name ?: "<unknown>")

@JvmName("logger0")
public inline fun <reified T> logger(name: String? = null): Logger =
    logger(T::class, name)
