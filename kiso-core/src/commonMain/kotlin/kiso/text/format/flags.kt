package kiso.text.format

import kiso.text.format.ConverterFlag.entries
import kotlin.jvm.JvmInline

public sealed interface BitSet {
    public val value: Int
}

public enum class ConverterFlag(public val char: Char, public val numeric: Boolean = false) : BitSet {
    //@formatter:off
    /** Marks the output as left-justified by adding any padding spaces to the right instead of to the left. */
    LEFT_JUSTIFIED   ('-'),

    /** Shows an alternative representation of the formatted data depending on the conversion. */
    ALTERNATE_FORM   ('#'),

    /** Converts the output to uppercase. */
    UPPER_CASE       ('^'),

    LOWER_CASE       ('_'),

    /**
     * Adds a space to positive number,
     * primarily so that the digits can be lined up with the digits of negative numbers
     */
    LEADING_SPACE    (' ', true),

    /** Pads numbers with zeros on the left. */
    ZERO_PADDED      ('0', true),

    /** Groups digits and puts separators in between the groups, affected by the locale. */
    GROUP            (',', true),

    /** Enclose negative numbers in parentheses. */
    ENCLOSE          ('(', true),

    /** Forces the sign to be shown for positive numbers. */
    FORCE_SIGN       ('+', true)

    ;
    //@formatter:on

    override val value: Int
        get() = 1 shl ordinal

    public companion object {
        /**
         * The list of all flags.
         */
        public fun valueOf(char: Char): ConverterFlag = entries.first { it.char == char }
    }
}

@JvmInline
public value class ConverterFlags(override val value: Int) : BitSet, Iterable<ConverterFlag> {
    public val values: Set<ConverterFlag>
        get() = ConverterFlag.entries.filter { it in this }.toSet()

    override fun iterator(): Iterator<ConverterFlag> = values.iterator()

    public operator fun get(bitset: BitSet): Boolean = contains(bitset)

    public operator fun contains(bitset: BitSet): Boolean = value and bitset.value == bitset.value

    /**
     * Adds the given flags to this instance.
     */
    public operator fun plus(flags: BitSet): ConverterFlags = ConverterFlags(value or flags.value)

    /**
     * Removes the given flag from this instance.
     */
    public operator fun minus(flag: BitSet): ConverterFlags = ConverterFlags(value and flag.value.inv())

    override fun toString(): String = "Flags${values.joinToString(", ", "{", "}") { it.name }}"

    public companion object {
        public val None: ConverterFlags = ConverterFlags(0)

        public val Casing: ConverterFlags = ConverterFlags(ConverterFlag.UPPER_CASE, ConverterFlag.LOWER_CASE)

        /**
         * Creates a new [ConverterFlags] instance with the given flags.
         */
        public operator fun invoke(vararg flags: BitSet): ConverterFlags =
            of(flags.asIterable())

        /**
         * Creates a new [ConverterFlags] instance with the given flags.
         */
        public fun of(flags: Iterable<BitSet>): ConverterFlags =
            ConverterFlags(flags.fold(0) { acc, flag -> acc or flag.value })
    }
}
