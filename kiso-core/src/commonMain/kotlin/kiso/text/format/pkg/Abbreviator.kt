package kiso.text.format.pkg

import kotlin.jvm.JvmStatic

public interface Abbreviator {
    /**
     * Determines if the given class name should be abbreviated.
     */
    public fun should_abbreviate(fqcn: String): Boolean

    /**
     * Abbreviates the given class name and appends it to the output.
     */
    public fun abbreviate(fqcn: String, output: Appendable)

    public fun abbreviate(fqcn: String): String =
        if (should_abbreviate(fqcn)) buildString { abbreviate(fqcn, this) } else fqcn

    public companion object {
        @JvmStatic
        public fun maxLen(max: Int): Abbreviator = MaxLen(max)
    }

    public class MaxLen(public val max: Int) : Abbreviator {
        override fun should_abbreviate(fqcn: String): Boolean = fqcn.length > max

        override fun abbreviate(fqcn: String, output: Appendable) {
            var dots = false
            fun add(part: String) {
                if (dots) output.append(PackageName.SEP)
                output.append(part)
                dots = true
            }

            var total = fqcn.length

            // split the logger name by periods
            val remaining = fqcn.split(PackageName.SEP).toMutableList()
            while (remaining.isNotEmpty()) {
                if (total <= max) {
                    // if the total size is under the maximum length, append the remaining parts.
                    for (part in remaining) add(part)
                    break
                }

                // remove the first part from the list.
                val part = remaining.removeFirst()
                if (remaining.isEmpty()) {
                    // if this is the last part, add it to the list and truncate it.
                    add(part.take(max - (total - part.length)))
                    break
                }

                // add the shortened version of the part to the list.
                add(part.take(1))

                // remove the length of all but the first character of the part from the total size.
                total -= part.lastIndex
            }
        }
    }
}