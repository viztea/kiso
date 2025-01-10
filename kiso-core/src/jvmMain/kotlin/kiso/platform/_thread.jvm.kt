package kiso.platform

public actual fun current_thread_name(): String? = Thread.currentThread().name
