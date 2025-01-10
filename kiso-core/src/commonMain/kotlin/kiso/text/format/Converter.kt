package kiso.text.format

import kiso.common.cs
import kiso.ext.repeat
import kotlin.math.absoluteValue
import kiso.text.format.ConverterFlag as F
import kiso.text.format.ConverterFlags.Companion as FS

public sealed interface Converter {
    public companion object {
        /**
         * All built-in conversions.
         */
        public val ALL: Set<Converter> = setOf(
            Percent,
            LineSeparator,
            HashCode,
            Bool,
            Str,
            UnicodeCharacter,
            DecimalInteger,
            HexInteger
        )

        private val by_ident by lazy { ALL.associateBy { it.identifier } }

        public fun find(ident: Char): Converter? = by_ident[ident]
    }

    /**
     * The identifiers that are used to specify this specifier.
     */
    public val identifier: Char

    public fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?)

    public sealed interface Literal : Converter {
        public val literal: CharSequence

        override fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?) {
            print(opts, fmt, literal)
        }
    }

    public data object Percent : Literal {
        override val identifier: Char get() = '%'

        override val literal: CharSequence get() = cs(identifier)
    }

    public data object LineSeparator : Literal {
        override val identifier: Char get() = 'n'

        override val literal: CharSequence get() = cs('\n')
    }

    public data object HashCode : Converter {
        override val identifier: Char get() = 'h'

        override fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?) {
            ConverterException.any(opts, FS(F.UPPER_CASE, F.LEFT_JUSTIFIED))

            val value = arg
                .hashCode()
                .toHexString(if (opts.flags[F.UPPER_CASE]) HexFormat.UpperCase else HexFormat.Default)

            print(opts, fmt, value)
        }
    }

    public data object Bool : Converter {
        override val identifier: Char get() = 'b'

        override fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?) {
            ConverterException.needs<Boolean>(arg)

            print(opts, fmt, casing(opts, arg.toString()))
        }
    }

    public data object Str : Converter {
        override val identifier: Char get() = 's'

        override fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?) {
            if (arg is Formattable) return arg.format(fmt, opts)

            print(opts, fmt, casing(opts, arg.toString()))
        }
    }

    public data object UnicodeCharacter : Converter {
        override val identifier: Char get() = 'c'

        override fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?) {
            ConverterException.any(opts, FS(FS.Casing, F.LEFT_JUSTIFIED))

            val value: Char = if (arg is Char) {
                arg
            } else {
                // require a number so that we can convert it to a character code.
                ConverterException.needs<Number>(arg)

                val code = arg.toInt()
                if (code < Char.MIN_VALUE.code || code > Char.MAX_VALUE.code)
                    ConverterException.invalid("Invalid Code Point")

                code.toChar()
            }

            // TODO: is it okay to use a one-to-many uppercase mapping here?
            print(opts, fmt, cs(casing(opts, value)))
        }
    }

    public data object DecimalInteger : Converter {
        override val identifier: Char get() = 'd'

        override fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?) {
            ConverterException.illegal(opts, FS(FS.Casing, F.ALTERNATE_FORM, F.LEFT_JUSTIFIED))
            ConverterException.exclusive(opts, FS(F.ZERO_PADDED, F.LEFT_JUSTIFIED))
            ConverterException.exclusive(opts, FS(F.FORCE_SIGN, F.LEADING_SPACE))

            // only allow non-floating point numbers.
            if (arg is Float || arg is Double) {
                ConverterException.invalid("Floating-point numbers are not supported for the 'd' specifier.")
            }

            ConverterException.needs<Number>(arg)

            // convert to a Long value.
            val value: Long = arg.as_long(true)
            val neg = value < 0
            val str = value.absoluteValue.toString()
            val out = StringBuilder()

            // add leading character
            when {
                // if the number is negative, add the sign or an opening parenthesis if the flag was set.
                neg                         -> out.append(if (F.ENCLOSE in opts.flags) '(' else '-')

                // if the number is positive, add a plus sign or a space if the flag was set.
                opts.flags[F.FORCE_SIGN]    -> out.append('+')
                opts.flags[F.LEADING_SPACE] -> out.append(' ')
            }

            val start = out.length

            // append the absolute value to the buffer, depending on the flags, add separators.
            if (F.GROUP in opts.flags && str.length > 3) {
                // TODO: localization
                for (i in str.indices.reversed() step 3) {
                    if (i - 3 < 0) {
                        out.insert(start, str.substring(0, i + 1))
                        break
                    }

                    out.insert(start, str.substring(i - 2, i + 1))
                    out.insert(start, ',')
                }
            } else {
                out.append(str)
            }

            // if the output is zero-padded, calculate the padding and it to the buffer.
            if (opts.flags[F.ZERO_PADDED]) {
                val width = opts.width ?: ConverterException.needs_width()
                if (width > out.length) out.insert(start, "0".repeat(width.minus(out.length)))
            }

            // if the number is negative and the flag is set, add the closing parenthesis.
            if (F.ENCLOSE in opts.flags && neg) {
                out.append(')')
            }

            fmt.output.append(out)
        }
    }

    public data object HexInteger : Converter {
        private val UPPER = HexFormat {
            upperCase = true
            number.removeLeadingZeros = true
        }

        private val LOWER = HexFormat {
            number.removeLeadingZeros = true
        }

        override val identifier: Char get() = 'x'

        override fun format(fmt: Formatter, opts: Format.Specifier.Options, arg: Any?) {
            ConverterException.any(opts, FS(F.UPPER_CASE, F.ZERO_PADDED, F.ALTERNATE_FORM, F.LEFT_JUSTIFIED))

            ConverterException.exclusive(opts, FS(F.ZERO_PADDED, F.LEFT_JUSTIFIED))

            // only allow non-floating point numbers.
            if (arg is Float || arg is Double) {
                ConverterException.invalid("Floating-point numbers are not supported for the 'x' specifier.")
            }

            ConverterException.needs<Number>(arg)

            // convert to a Long value.
            val format = if (opts.flags[F.UPPER_CASE]) UPPER else LOWER
            val value = arg.as_long(false).toHexString(format)

            val sb = StringBuilder()

            var len = value.length
            if (F.ALTERNATE_FORM in opts.flags) {
                sb.append(casing(opts, "0x"))
                len += 2
            }

            if (opts.flags[F.ZERO_PADDED]) {
                val width = opts.width ?: ConverterException.needs_width()
                sb.append('0'.repeat(width - len))
            }

            sb.append(value)
            print(opts, fmt, sb)
        }
    }

//    public data object UnsignedHexInt : Converter {
//        override val identifiers: CharSequence get() = CharSequence('h', 'H')
//    }
//
//    public data object OctalInteger : Converter {
//        override val identifiers: CharSequence get() = CharSequence('O')
//    }
//
//    public data object ScientificNotation : Converter {
//        override val identifiers: CharSequence get() = CharSequence('e', 'E')
//    }
//
//    public data object FloatingPointNumber : Converter {
//        override val identifiers: CharSequence get() = CharSequence('f')
//    }
//
//    public data object GeneralNumber : Converter {
//        override val identifiers: CharSequence get() = CharSequence('g', 'G')
//    }
//
//    public data object HexadecimalFloatingPointNumber : Converter {
//        override val identifiers: CharSequence get() = CharSequence('a', 'A')
//    }
//
//    public data object DateTime : Specifier {
//        override val identifiers: CharSequence get() = CharSequence('t', 'T')
//    }
}
