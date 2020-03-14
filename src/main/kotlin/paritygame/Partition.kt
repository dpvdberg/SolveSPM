package paritygame

class Partition(diamondSet : MutableSet<Node>, boxSet : MutableSet<Node>) {
    private val partition : Map<Player, MutableSet<Node>> = mapOf(
        Diamond to diamondSet,
        Box to boxSet
    )

    fun getSet(player : Player): MutableSet<Node> {
        return partition[player] ?: error("Player not found in map")
    }
}