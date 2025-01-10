# Core - [`kiso.text.format`](../kiso-core/src/commonMain/kotlin/kiso/text/format)

A wip text formatting library based on Java's `String#format` and other formatting utilities.

## Features

Available Flags:

- `'-'` Marks the output as left-justified by adding any padding spaces to the right instead of to the left.
- `'#'` Shows an alternative representation of the formatted data depending on the conversion.
- `'^'` Converts the output to uppercase.
- `'_'` Converts the output to lowercase.
- `' '` Adds a space to positive number, primarily so that the digits can be lined up with the digits of negative numbers.
- `'0'` Pads numbers with zeros on the left.
- `','` Groups digits and puts separators in between the groups, affected by the locale.
- `'('` Enclose negative numbers in parentheses.
- `'+'` Forces the sign to be shown for positive numbers.

Available Conversions:

- `'%'` Prints a percentage sign
- `'n'` Prints a newline
- `'h'` Prints the hashcode of the argument.
- `'b'` Prints a boolean
- `'s'` Prints the stringified argument or calls `format` on `Formattable` arguments
- `'c'` Prints a character
- `'d'` Prints an integer
- `'x'` Prints an integer in hexadecimal

## Usage

```kotlin
// common
implementation("gay.vzt.kiso:kiso-core:{VERSION}")
```

```kotlin
import kiso.ext.format
import kiso.text.format.Format
import kiso.text.format.Formatter
import kiso.text.format.Formattable

fun main() {
    // simple string formatting
    println("Hello, %s!".format("world"))

    // compile a format string for multiple uses
    val format = Format.compile("Hello, %s! Your number is %02d")
    println(format.apply("world", 3)) // Hello, world! Your number is 03

    // printing a Formattable
    println("%.2s".format(MyClass())) // MyClass 2
}

class MyClass : Formattable {
    override fun format(formatter: Formatter, options: Format.Specifier.Options) {
        formatter.format("%s %d", "MyClass", options.precision)
    }
}
```

## TODO:

- `h` unsigned hex int
- `O` octal int
- `e` scientific notation
- `f` floating point
- `g` general number
- `a` hexadecimal floating point
- `t` date time
