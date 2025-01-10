package kiso.text.format

import kiso.ext.ControlFlowException
import kotlin.contracts.contract
import kotlin.jvm.JvmInline
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kiso.text.format.ConverterFlags.Companion as FC

internal class ConverterException(val type: Type) : ControlFlowException() {
    sealed interface Type {
        data object MissingArgument : Type

        data object MissingWidth : Type

        @JvmInline
        value class InvalidArgument(val message: String) : Type

        @JvmInline
        value class InvalidArgumentType(val required: KType) : Type

        @JvmInline
        value class InvalidFlags(val provided: ConverterFlags) : Type

        @JvmInline
        value class MissingFlags(val flag: ConverterFlags) : Type
    }

    companion object {
        fun invalid(message: String): Nothing {
            throw ConverterException(Type.InvalidArgument(message))
        }

        fun needs_width(): Nothing {
            throw ConverterException(Type.MissingWidth)
        }

        fun exclusive(opts: Format.Specifier.Options, flags: ConverterFlags) {
            if (opts.flags.count { it in flags } > 1) {
                throw ConverterException(Type.InvalidFlags(FC.of(opts.flags.intersect(flags))))
            }
        }

        fun illegal(opts: Format.Specifier.Options, impossible: ConverterFlags) {
            if (opts.flags.any { it in impossible }) {
                throw ConverterException(Type.InvalidFlags(FC.of(opts.flags.intersect(impossible))))
            }
        }

        fun any(opts: Format.Specifier.Options, possible: ConverterFlags) {
            if (opts.flags.any { it !in possible }) {
                throw ConverterException(Type.InvalidFlags(FC.of(opts.flags - possible)))
            }
        }

        inline fun <reified T> needs(value: Any?) {
            contract {
                returns() implies (value is T)
            }

            if (value == null) {
                throw ConverterException(Type.MissingArgument)
            } else if (value !is T) {
                throw ConverterException(Type.InvalidArgumentType(typeOf<T>()))
            }
        }
    }
}