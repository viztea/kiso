package kiso.text.format

public open class FormatException public constructor(message: String) : RuntimeException(message)

public class FormatterClosedException : FormatException("Formatter is closed")

public class UnknownFormatConversionException(conversion: Char) : FormatException("Unknown conversion: $conversion")

public open class FormatSpecifierException(
    public val specifier: Format.Specifier,
    message: String
) : FormatException(
    "[col ${specifier.range}; $specifier] $message"
)

public class MissingFormatFlagsException(
    specifier: Format.Specifier,
    public val flags: ConverterFlags
) : FormatSpecifierException(
    specifier,
    flags.values.joinToString(", ") { "${it.name} (${it.char})" }
)

public class MissingFormatArgumentException(specifier: Format.Specifier) : FormatSpecifierException(
    specifier,
    "Missing Argument"
)

public class InvalidFormatArgumentException(
    specifier: Format.Specifier,
    message: String,
    public val argument: Any?,
) : FormatSpecifierException(specifier, message)

public class IllegalFlagsException(
    specifier: Format.Specifier,
    public val flags: ConverterFlags
) : FormatSpecifierException(
    specifier,
    flags.values.joinToString(", ", prefix = "found illegal flags: ") { "${it.name} (${it.char})" }
)

public class MissingWidthException(specifier: Format.Specifier) : FormatSpecifierException(
    specifier,
    "Missing Width"
)
