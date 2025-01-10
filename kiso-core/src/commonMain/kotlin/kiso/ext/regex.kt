package kiso.ext

public fun MatchResult.get_opt(idx: Int): String? = groups[idx]?.value

public fun MatchResult.get_opt(name: String): String? = groups[name]

public operator fun MatchResult.get(idx: Int): String = get_opt(idx)
                                                        ?: throw IllegalArgumentException("No group at index $idx")

public operator fun MatchResult.get(name: String): String = get_opt(name)
                                                            ?: throw IllegalArgumentException("No group with name $name")

public operator fun MatchGroupCollection.get(name: String): String? {
    val namedGroups = this as? MatchNamedGroupCollection
                      ?: throw UnsupportedOperationException("Retrieving groups by name is not supported on this platform.")

    return namedGroups[name]?.value
}

public operator fun MatchGroupCollection.contains(group: String): Boolean = get(group) != null
