package kiso.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Returns a new [Instant] using the supplied [Clock][clock]
 */
public fun now(clock: Clock = Clock.System): Instant =
    clock.now()

/**
 *  Returns the current epoch milliseconds using the supplied [Clock][clock]
 */
public fun now_ms(clock: Clock = Clock.System): Long =
    now(clock).toEpochMilliseconds()

public fun now_secs(clock: Clock = Clock.System): Long =
    now(clock).epochSeconds

/**
 * The current time in nanoseconds.
 */
public expect fun now_nanos(): Long
