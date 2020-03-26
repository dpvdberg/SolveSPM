package paritygame

import SearchMethod
import evaluation.Tuple
import java.util.*

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

    fun getNodes(method : SearchMethod, node: Node = startingNode) = when (method) {
        SearchMethod.DFS -> getNodesDFS(node)
        SearchMethod.BFS -> getNodesBFS(node)
    }

    fun getEdgesDFS(node: Node = startingNode) = getEdgesBFS(node)
}