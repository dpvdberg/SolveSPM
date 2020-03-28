package paritygame

import util.SearchMethod
import evaluation.Tuple

class Game(
    val nodes: List<Node>,
    val id: Int? = null
) {
    // This is the node with the lowest id
    val startingNode = nodes.minBy { n -> n.id }!!

    val maxPriority: Int = nodes.map { n -> n.priority }.max()!!

    val max: Tuple by lazy {
        val count = Tuple(maxPriority + 1)

        nodes.filter { n -> !n.isEven() }.forEach { n -> count.m[n.priority]++ }

        count
    }

    fun getNodes(method: SearchMethod): List<Node> {
        val remainingNodes = nodes.sortedBy { n -> n.id }.toMutableList()
        val orderedNodes = mutableListOf<Node>()
        while (remainingNodes.isNotEmpty()) {
            orderedNodes += paritygame.searchNodes(remainingNodes.first(), method)
                .filter { n -> !orderedNodes.contains(n) }
            orderedNodes.forEach { n -> remainingNodes.remove(n) }
        }

        return orderedNodes.toList()
    }

    fun getEdges(): Sequence<Pair<Node, Node>> = sequence {
        nodes.forEach { n -> n.successors.forEach { m -> yield(Pair(n, m)) } }
    }

    override fun toString() =
        """
            Game id: $id
            Number of nodes: ${nodes.size}
            
            Starting node: $startingNode
            Maximum priority: $maxPriority
        """.trimIndent()
}