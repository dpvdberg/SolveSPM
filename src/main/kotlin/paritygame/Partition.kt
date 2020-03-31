package paritygame

class Partition(private val diamondSet : Set<Node>, private val boxSet : Set<Node>) {
    private val partition : Map<Player, Set<Node>> = mapOf(
        Diamond to diamondSet,
        Box to boxSet
    )

    fun getSet(player : Player): Set<Node> {
        return partition[player] ?: error("Player not found in map")
    }

    override fun toString(): String =
        """
            Diamond (<>) : ${diamondSet.size} nodes
            Box ([]) : ${boxSet.size} nodes
        """.trimIndent()

    fun toStringFull(): String =
        """
            Diamond (<>) : ${diamondSet.size} nodes -> ${getSetString(Diamond)}
            Box ([]) : ${boxSet.size} nodes -> ${getSetString(Box)}
        """.trimIndent()

    fun getSetString(player: Player) = getSet(player)
        .map{n -> n.id}
        .sorted()
        .toList()
        .toString()
        .replace('[', '{')
        .replace(']', '}')
}