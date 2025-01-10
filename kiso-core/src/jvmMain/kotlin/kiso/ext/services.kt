package kiso.ext

import kiso.log.Logger
import kiso.log.error
import kiso.common.logger
import java.util.*
import kotlin.streams.asSequence

// TODO: make a catching version.

public inline fun <reified T : Any> service_for(): T =
    services_for<T>().first()

public inline fun <reified T : Any> service_for_opt(): T? =
    services_for<T>().firstOrNull()

public inline fun <reified T : Any> services_for(): Sequence<T> =
    ServiceLoader.load(T::class.java)
        .also { it.reload() }
        .stream()
        .asSequence()
        .mapNotNull { it.get() }

@PublishedApi
internal val SERVICE_LOADER_LOG: Logger = logger("kiso.ext.ServicesKt")

public inline fun <reified T : Any, R> with_service(crossinline block: (T) -> R): R {
    val services = services_for<T>()
    for (service in services) try {
        return block(service)
    } catch (ex: Throwable) {
        if (ex.is_fatal()) throw ex
        SERVICE_LOADER_LOG.error(ex) { "Failed to use service $service" }
        continue
    }

    error("Failed to find a suitable service for ${T::class}")
}
