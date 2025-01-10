package kiso.platform

import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.Worker

@OptIn(ObsoleteWorkersApi::class)
public actual fun current_thread_name(): String? = Worker.current.name
