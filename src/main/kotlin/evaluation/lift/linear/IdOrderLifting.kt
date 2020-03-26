package evaluation.lift.linear

import evaluation.lift.LiftingStrategy
import paritygame.Game
import paritygame.Node

class IdOrderLifting(game : Game) : LinearStrategy(game) {
    private val sortedNodes = game.nodes.sortedBy { n -> n.id }
    private var index = 0

    override fun fetchNextNode(): Node {
        val node = sortedNodes[index]
        index = (index + 1) % sortedNodes.size

        return node
    }
}