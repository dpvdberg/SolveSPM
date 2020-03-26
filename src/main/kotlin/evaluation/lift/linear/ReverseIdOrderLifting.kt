package evaluation.lift.linear

import evaluation.lift.LiftingStrategy
import paritygame.Game
import paritygame.Node

open class ReverseIdOrderLifting(game : Game) : LinearStrategy(game) {
    open val sortedNodes = game.nodes.sortedBy { n -> n.id }
    private var index = game.nodes.size - 1

    override fun fetchNextNode(): Node {
        val node = sortedNodes[index]
        index = Math.floorMod(index - 1, sortedNodes.size)

        return node
    }

    override fun liftedNode(node: Node) {
        // Nothing to do here, we just continue linearly
    }
}