package kiso.common

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.CLOCK_MONOTONIC
import platform.posix.clock_gettime
import platform.posix.timespec
import kotlin.time.Duration.Companion.seconds

// TODO: test this

public actual fun now_nanos(): Long = memScoped {
    val tv = alloc<timespec>()
    clock_gettime(CLOCK_MONOTONIC, tv.ptr)

    tv.tv_sec.seconds.inWholeNanoseconds + tv.tv_nsec
}
