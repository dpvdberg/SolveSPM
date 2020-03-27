package parser

import paritygame.Game
import paritygame.Node

abstract class PGSolverParser {
    abstract fun parse(data: String): Game

    fun setNodes(
        parityGame: Game,
        nodeMap: MutableMap<Int, Node>,
        predecessorMap: MutableMap<Int, MutableList<Node>>
    ) {
        parityGame.nodes.forEach { n ->
            n.successors = n.successorsIds.map { id ->
                nodeMap[id]!!
            }.toList()
        }

        parityGame.nodes.forEach { n ->
            n.predecessors = predecessorMap[n.id]!!
        }
    }
}