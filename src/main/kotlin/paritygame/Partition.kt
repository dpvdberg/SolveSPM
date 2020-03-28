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
            Diamond (<>) : ${diamondSet.size} nodes -> ${diamondSet.map{n -> n.id}.sorted().toList()}
            Box ([]) : ${boxSet.size} nodes -> ${boxSet.map{n -> n.id}.sorted().toList()}
        """.trimIndent()
}