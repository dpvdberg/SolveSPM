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

        val nodes = lines
            .filter { l -> l.isNotEmpty() }
            .map { l -> parseLineToNode(l) }
            .toList()
        val game = Game(nodes, parityId)

        setDescendantInfo(game)
        return game
    }

    private fun parseLineToNode(l: String): Node {
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

        storeDescendantInfo(node)

        return node
    }
}