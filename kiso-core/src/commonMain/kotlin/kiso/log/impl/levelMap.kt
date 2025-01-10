package kiso.log.impl

import kiso.common.collection.CaseInsensitiveMap
import kiso.log.LogLevel
import kiso.text.format.pkg.PackageName
import kotlin.jvm.JvmInline

/**
 * A map of log levels for different loggers.
 */
@JvmInline
public value class LevelMap private constructor(private val root: Setting) {
    public operator fun get(name: String): LogLevel {
        val level = when {
            //
            name.equals(ROOT_LEVEL_NAME, true) -> root.level

            PackageName.is_valid(name)         -> {
                val parts = name.split('.')

                var current = root

                var level = root.level
                for (part in parts) {
                    // try to go deeper into the tree.
                    current = current.children[part] ?: break

                    // update the level if it is set.
                    current.level?.let { level = it }
                }

                level
            }

            else                               -> root.children[name]?.level
        }

        return level ?: DEFAULT_LEVEL
    }

    public operator fun set(name: String, level: LogLevel): Unit = when {
        name.equals(ROOT_LEVEL_NAME, true) -> {
            root.level = level
        }

        // package names require special handling.
        PackageName.is_valid(name)         -> {
            // split the logger name into parts and fill in the tree as needed.
            // then set the level of the logger.
            name.split('.')
                .fold(root) { acc, part -> acc.children.getOrPut(part, Setting::none) }
                .level = level
        }

        else                               -> {
            // set the level of the logger.
            root.children.getOrPut(name, Setting::none).level = level
        }
    }

    private data class Setting(
        var level: LogLevel?,
        val children: MutableMap<String, Setting>,
    ) {
        override fun toString(): String {
            val children = children.entries.joinToString(", ", prefix = "{", postfix = "}") { (k, v) -> "$k=$v" }
            return "Setting(level=$level, children=$children)"
        }

        companion object {
            fun none(): Setting = Setting(null, CaseInsensitiveMap())
        }
    }

    public companion object {
        /**
         * The name of the root logger.
         */
        public const val ROOT_LEVEL_NAME: String = "root"

        /**
         * The default log level.
         */
        public val DEFAULT_LEVEL: LogLevel = LogLevel.INFO

        public fun empty(): LevelMap = LevelMap(Setting.none())

        /**
         * Computes the level map from the specified levels.
         */
        public fun of(levels: Map<String, LogLevel>): LevelMap {
            val empty = empty()
            for ((loggerName, level) in levels) {
                empty[loggerName] = level
            }

            return empty
        }
    }
}
