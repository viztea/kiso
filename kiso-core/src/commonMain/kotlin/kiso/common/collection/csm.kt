package kiso.common.collection

// The code below was taken from the following source:
// - https://github.com/ktorio/ktor/blob/980ccf0019cabf6c9909028edd91c31a967ba5de/ktor-utils/common/src/io/ktor/util/CaseInsensitiveMap.kt
// - https://github.com/ktorio/ktor/blob/980ccf0019cabf6c9909028edd91c31a967ba5de/ktor-utils/common/src/io/ktor/util/DelegatingMutableSet.kt
// - https://github.com/ktorio/ktor/blob/980ccf0019cabf6c9909028edd91c31a967ba5de/ktor-utils/common/src/io/ktor/util/Text.kt#L101
// LICENSE: https://github.com/ktorio/ktor/blob/main/LICENSE

public class CaseInsensitiveMap<Value : Any> : MutableMap<String, Value> {
    private val delegate = mutableMapOf<CaseInsensitiveString, Value>()

    override val size: Int get() = delegate.size

    override fun containsKey(key: String): Boolean = delegate.containsKey(CaseInsensitiveString(key))

    override fun containsValue(value: Value): Boolean = delegate.containsValue(value)

    override fun get(key: String): Value? = delegate[key.caseInsensitive()]

    override fun isEmpty(): Boolean = delegate.isEmpty()

    override fun clear() {
        delegate.clear()
    }

    override fun put(key: String, value: Value): Value? = delegate.put(key.caseInsensitive(), value)

    override fun putAll(from: Map<out String, Value>) {
        from.forEach { (key, value) -> put(key, value) }
    }

    override fun remove(key: String): Value? = delegate.remove(key.caseInsensitive())

    override val keys: MutableSet<String>
        get() = DelegatingMutableSet(
            delegate.keys,
            { content },
            { caseInsensitive() }
        )

    override val entries: MutableSet<MutableMap.MutableEntry<String, Value>>
        get() = DelegatingMutableSet(
            delegate.entries,
            { Entry(key.content, value) },
            { Entry(key.caseInsensitive(), value) }
        )

    override val values: MutableCollection<Value> get() = delegate.values

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is CaseInsensitiveMap<*>) return false
        return other.delegate == delegate
    }

    override fun hashCode(): Int = delegate.hashCode()
}

private class Entry<Key, Value>(
    override val key: Key,
    override var value: Value
) : MutableMap.MutableEntry<Key, Value> {

    override fun setValue(newValue: Value): Value {
        value = newValue
        return value
    }

    override fun hashCode(): Int = 17 * 31 + key!!.hashCode() + value!!.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Map.Entry<*, *>) return false
        return other.key == key && other.value == value
    }

    override fun toString(): String = "$key=$value"
}

internal fun String.caseInsensitive(): CaseInsensitiveString = CaseInsensitiveString(this)

internal class CaseInsensitiveString(val content: String) {
    private val hash: Int

    init {
        var temp = 0
        for (element in content) {
            temp = temp * 31 + element.lowercaseChar().code
        }

        hash = temp
    }

    override fun equals(other: Any?): Boolean =
        (other as? CaseInsensitiveString)?.content?.equals(content, ignoreCase = true) == true

    override fun hashCode(): Int = hash

    override fun toString(): String = content
}

internal open class DelegatingMutableSet<From, To>(
    private val delegate: MutableSet<From>,
    private val convertTo: From.() -> To,
    private val convert: To.() -> From
) : MutableSet<To> {

    open fun Collection<To>.convert(): Collection<From> = map { it.convert() }
    open fun Collection<From>.convertTo(): Collection<To> = map { it.convertTo() }

    override val size: Int = delegate.size

    override fun add(element: To): Boolean = delegate.add(element.convert())

    override fun addAll(elements: Collection<To>): Boolean = delegate.addAll(elements.convert())

    override fun clear() {
        delegate.clear()
    }

    override fun remove(element: To): Boolean = delegate.remove(element.convert())

    override fun removeAll(elements: Collection<To>): Boolean = delegate.removeAll(elements.convert().toSet())

    override fun retainAll(elements: Collection<To>): Boolean = delegate.retainAll(elements.convert().toSet())

    override fun contains(element: To): Boolean = delegate.contains(element.convert())

    override fun containsAll(elements: Collection<To>): Boolean = delegate.containsAll(elements.convert())

    override fun isEmpty(): Boolean = delegate.isEmpty()

    override fun iterator(): MutableIterator<To> = object : MutableIterator<To> {
        val delegateIterator = delegate.iterator()

        override fun hasNext(): Boolean = delegateIterator.hasNext()

        override fun next(): To = delegateIterator.next().convertTo()

        override fun remove() = delegateIterator.remove()
    }

    override fun hashCode(): Int = delegate.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Set<*>) return false

        val elements = delegate.convertTo()
        return other.containsAll(elements) && elements.containsAll(other)
    }

    override fun toString(): String = delegate.convertTo().toString()
}
