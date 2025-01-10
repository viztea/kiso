package kiso.text.format

import kiso.common.cs
import kiso.ext.get
import kiso.ext.get_opt
import kotlin.jvm.JvmInline

@JvmInline
public value class Format(private val fragments: List<Fragment>) {
    public fun apply(vararg args: Any?): String =
        buildString { apply_args(Formatter(this), args) }

    /**
     * Applies the format to the given appendable.
     */
    public fun apply_args(formatter: Formatter, args: Array<out Any?>) {
        var pos = 0
        for (fragment in fragments) when (fragment) {
            is Fragment.Text        -> {
                formatter.output.append(fragment.value, fragment.start, fragment.end)
            }

            is Fragment.Placeholder -> {
                val arg = when (val i = fragment.specifier.index) {
                    is Specifier.Index.Explicit -> args[i.value]
                    Specifier.Index.None        -> null
                    Specifier.Index.Previous    -> args[pos - 1]
                    Specifier.Index.Unspecified -> args.elementAtOrNull(pos++)
                }

                try {
                    fragment.specifier.converter.format(formatter, fragment.specifier.options, arg)
                } catch (ex: ConverterException) {
                    val real: FormatException = when (val t = ex.type) {
                        is ConverterException.Type.InvalidArgument     ->
                            InvalidFormatArgumentException(fragment.specifier, t.message, arg)

                        is ConverterException.Type.InvalidArgumentType -> {
                            val received = arg
                                ?.let { it::class }
                                ?: "null"

                            InvalidFormatArgumentException(
                                fragment.specifier,
                                "expected ${t.required} not $received ($arg)",
                                arg
                            )
                        }

                        is ConverterException.Type.InvalidFlags        ->
                            IllegalFlagsException(fragment.specifier, t.provided)

                        is ConverterException.Type.MissingFlags        ->
                            MissingFormatFlagsException(fragment.specifier, t.flag)

                        ConverterException.Type.MissingArgument        ->
                            MissingFormatArgumentException(fragment.specifier)

                        ConverterException.Type.MissingWidth           ->
                            MissingWidthException(fragment.specifier)
                    }

                    throw real
                }
            }
        }
    }

    override fun toString(): String {
        // Format[%d, ...]
        return "Format$fragments"
    }

    /**
     * A fragment of a format string.
     */
    public sealed interface Fragment {
        /**
         * A plain text fragment, not a specifier.
         */
        public data class Text(public val value: String, val start: Int, val end: Int) : Fragment {
            override fun toString(): String = "\"${value.substring(start, end)}\""
        }

        /**
         * A placeholder fragment, contains a specifier to format an argument.
         */
        @JvmInline
        public value class Placeholder(public val specifier: Specifier) : Fragment {
            override fun toString(): String = specifier.toString()
        }
    }

    public data class Specifier(
        /** The range of the specifier in the format string. */
        val range: IntRange,
        /** The index of the argument to format. */
        val index: Index,
        /** Converter that was specified */
        val converter: Converter,
        /** Options that modify the behavior of the conversion. */
        val options: Options
    ) {
        override fun toString(): String = buildString {
            append('%')
            when (index) {
                is Index.Explicit -> append(index.value, '$')
                Index.Previous    -> append('<')
                else              -> {}
            }

            for (flag in options.flags) {
                append(flag.char)
            }

            options.width?.let { append(it) }
            options.precision?.let { append('.', it) }
            append(converter.identifier)
        }

        public sealed interface Index {
            /** An explicit argument is used. */
            @JvmInline
            public value class Explicit(public val value: Int) : Index

            /** The previous argument is used. */
            public data object Previous : Index

            /** No index is specified, the next argument is used. */
            public data object Unspecified : Index

            /** No index, the specifier is for a literal conversion, i.e., percent sign or a line separator. */
            public data object None : Index
        }

        /**
         * The context in which a conversion is performed.
         *
         * Most of these fields are not used in the implementation of [Converter]s but provided
         * if required, e.g., for digit grouping.
         */
        public data class Options(
            /** The flags that modify the behavior of the conversion. */
            val flags: ConverterFlags,
            /**
             * The minimum number of characters that the output should occupy.
             *
             * If necessary,
             * spaces are added to the right of the output to reach this width unless [ConverterFlag.LEFT_JUSTIFIED] was specified.
             */
            val width: Int?,
            /**
             * How many decimal digits to show in the formatted data.
             */
            val precision: Int?,
        ) {
            public companion object {
                public val None: Options = Options(ConverterFlags.None, null, null)
            }
        }

        public companion object {
            private val REGEX: Regex = run {
                val f = ConverterFlag.entries.joinToString("", "[", "]") { cs(it.char) }
                val c = Converter.ALL.joinToString("", "[", "]") { cs(it.identifier) }
                """%(?<i>[1-9]\d*|<)?(?<f>$f+)?(?<w>\d+|\{\w+})?(?:\.(?<p>\d+|\{\w+}))?(?<c>$c)""".toRegex()
            }

            internal fun of(ident: Char, start: Int): Specifier? {
                val converter = Converter.find(ident)
                    ?: return null

                return Specifier(
                    start..start + 1,
                    if (converter is Converter.Literal) Index.None else Index.Unspecified,
                    converter,
                    Options.None
                )
            }

            internal fun parse(
                s: String,
                parameters: Map<String, Any>,
                i: Int = 0
            ): Specifier? =
                REGEX.find(s, i)?.let { parse(it, parameters) }

            private fun String.from_parameter(params: Map<String, Any>): Int {
                // attempt to parse the value as an integer.
                val value = toIntOrNull()
                if (value != null) return value

                // if it fails, then it's a parameter.
                val parameter = removeSurrounding("{", "}")
                return params[parameter] as? Int
                    ?: throw FormatException("missing parameter: $parameter")
            }

            internal fun parse(
                match: MatchResult,
                parameters: Map<String, Any>
            ): Specifier {
                var index = when (val i = match.get_opt(1)) {
                    null -> Index.Unspecified
                    "<"  -> Index.Previous
                    else -> Index.Explicit(i.toInt())
                }

                var flags = match.get_opt(2)
                    ?.map { ConverterFlag.valueOf(it) }
                    ?.let { ConverterFlags.of(it) }
                    ?: ConverterFlags.None

                val width = match.get_opt(3)?.from_parameter(parameters)

                val precision = match.get_opt(4)?.from_parameter(parameters)

                // use the specified identifier to find the converter to use.
                var ident = match[5][0]
                if (ident.isUpperCase()) {
                    ident = ident.lowercaseChar()
                    flags += ConverterFlag.UPPER_CASE
                }

                val converter = Converter.find(ident)
                    ?: throw UnknownFormatConversionException(ident)

                // if the converter is a literal, an index is not required.
                if (converter is Converter.Literal) {
                    index = Index.None
                }

                return Specifier(
                    match.range,
                    index,
                    converter,
                    Options(flags, width, precision)
                )
            }
        }
    }

    /**
     * ```kt
     * val myFormat = Format.compile(
     *    "`#%0{padding}d` %s",
     *    "padding" to 5
     * )
     * ```
     */
    public companion object {
        /**
         * Parses the given format string into a set of fragments.
         */
        public fun compile(s: String, vararg params: Pair<String, Any>): Format {
            val parameters = params.toMap()

            // a mutable set to store the fragments.
            val fragments = mutableListOf<Fragment>()

            // a helper function to commit text fragments to the set.
            fun commit(start: Int, end: Int) =
                if (end - start > 0) fragments += Fragment.Text(s, start, end) else Unit

            var i = 0
            while (i < s.length) {
                val n = s.indexOf('%', i)
                when {
                    // no more specifiers left in the string... but there may be some trailing text.
                    n < 0            -> {
                        commit(i, s.length)
                        break
                    }

                    // trailing %
                    n == s.lastIndex -> throw FormatException("trailing % at :$i")

                    // add any text fragments in between the last and current specifier...
                    i != n           -> commit(i, n)
                }

                // first check if the first character is a converter, otherwise parse the specifier using regex
                // and add it to the fragment set.
                val specifier = Specifier.of(s[n + 1], n)
                    ?: Specifier.parse(s, parameters, n)
                    ?: throw UnknownFormatConversionException(s[n + 1])

                fragments += Fragment.Placeholder(specifier)

                // move the index to the end of the specifier (exclusive).
                i = specifier.range.last + 1
            }

            return Format(fragments)
        }
    }
}
