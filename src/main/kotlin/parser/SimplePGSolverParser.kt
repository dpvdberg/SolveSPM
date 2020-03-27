package parser

import paritygame.*

class SimplePGSolverParser : PGSolverParser() {
    override fun parse(data: String): Game {
        var lines = data.lineSequence()
        var parityId: Int? = null

        if (lines.first().startsWith("parity")) {
            parityId = lines.first().trim(';').split(' ')[1].toInt()
            lines = lines.drop(1)
        }

        val nodeMap: MutableMap<Int, Node> = mutableMapOf()
        val predecessorMap = mutableMapOf<Int, MutableList<Node>>()
            .withDefault { mutableListOf() }

        val nodes = lines
            .filter { l -> l.isNotEmpty() }
            .map { l -> parseLineToNode(l, nodeMap, predecessorMap) }
            .toList()
        val game = Game(nodes, parityId)

        setNodes(game, nodeMap, predecessorMap)
        return game
    }

    private fun parseLineToNode(
        l: String,
        nodeMap: MutableMap<Int, Node>,
        predecessorMap: MutableMap<Int, MutableList<Node>>
    ): Node {
        val split = l.trim(';').split(' ')
        val node = Node(
            split[0].toInt(),
            split[1].toInt(),
            if (split[2].toInt() == 0) Diamond else Box,
            emptyList(),
            emptyList(),
            split[3].split(',').map { n -> n.toInt() },
            split.getOrNull(4)?.trim('"')
        )
        nodeMap[node.id] = node
        node.successorsIds.forEach { s -> predecessorMap.getValue(s).add(node) }

        return node
    }
}