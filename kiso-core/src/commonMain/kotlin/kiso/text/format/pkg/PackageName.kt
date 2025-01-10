package kiso.text.format.pkg

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmStatic

/**
 * Represents the package of a class or file.
 */
@JvmInline
public value class PackageName private constructor(public val value: String) {
    public val parts: List<String> get() = value.split(SEP)

    /**
     * The length of the package name including separators.
     */
    public val length: Int get() = value.length

    /**
     * Attempts to shorten the package name to the specified maximum length.
     *
     * @param max The maximum length of the package name.
     */
    public fun abbreviate(max: Int): PackageName =
        PackageName(Abbreviator.MaxLen(max).abbreviate(value))

    public companion object {
        public const val SEP: Char = '.'

        public val REGEX: Regex = """(?i)^[a-z]\w*(\.\w+)+$""".toRegex()

        @JvmStatic
        public fun is_valid(value: String): Boolean = REGEX.matches(value)

        @JvmStatic
        public fun of(value: String): PackageName = requireNotNull(of_opt(value)) { "Invalid package name: $value" }

        @JvmStatic
        public fun of_opt(value: String): PackageName? = if (is_valid(value)) PackageName(value) else null
    }
}
