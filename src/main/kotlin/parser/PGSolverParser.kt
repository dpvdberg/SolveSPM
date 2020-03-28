package parser

import paritygame.Game
import paritygame.Node

abstract class PGSolverParser {
    abstract fun parse(data: String): Game

    private val nodeMap: MutableMap<Int, Node> = mutableMapOf()
    private val predecessorMap = mutableMapOf<Int, MutableList<Node>>()
        .withDefault { mutableListOf() }

    fun setDescendantInfo(parityGame: Game) {
        parityGame.nodes.forEach { n ->
            n.successors = n.successorsIds.map { id ->
                nodeMap[id]!!
            }.toList()
        }

        parityGame.nodes.forEach { n ->
            n.predecessors = predecessorMap.getValue(n.id)
        }
    }

    fun storeDescendantInfo(node : Node) {
        nodeMap[node.id] = node
        node.successorsIds.forEach { s ->
            run {
                if (!predecessorMap.containsKey(s)) {
                    predecessorMap[s] = mutableListOf()
                }
                predecessorMap[s]!!.add(node)
            }
        }
    }
}