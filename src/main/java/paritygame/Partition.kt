package paritygame

class Partition(diamondSet : Set<Node>, boxSet : Set<Node>) {
    private val partition : Map<Player, Set<Node>> = mapOf(
        Diamond to diamondSet,
        Box to boxSet
    )

    fun getSet(player : Player): Set<Node> {
        return partition[player] ?: error("Player not found in map")
    }
}