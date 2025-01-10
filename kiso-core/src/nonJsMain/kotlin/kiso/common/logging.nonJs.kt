package kiso.common

import kiso.log.Logger
import kotlin.reflect.KClass

public actual fun logger(func: KClass<*>, name: String?): Logger =
    logger(func.qualifiedName ?: name ?: "")
