package kiso.common

import kiso.log.Logger
import kotlin.reflect.KClass

// js doesn't support reflection...
public actual fun logger(func: KClass<*>, name: String?): Logger =
    logger(name)
