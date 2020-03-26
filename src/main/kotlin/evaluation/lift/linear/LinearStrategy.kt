package evaluation.lift.linear

import evaluation.lift.LiftingStrategy
import paritygame.Game
import paritygame.Node

abstract class LinearStrategy(val game: Game) : LiftingStrategy {
    private var nonLiftedFetchedNodes = 0

    abstract fun fetchNextNode(): Node

    abstract fun liftedNode(node: Node)

    override fun getNext() : Node? {
        return if (nonLiftedFetchedNodes >= game.nodes.size) {
            /* All nodes fetched but non are lifted, we are done */
            null
        } else {
            nonLiftedFetchedNodes++

            /* Get next node for the strategy */
            fetchNextNode()
        }
    }

    override fun setLifted(node : Node) {
        nonLiftedFetchedNodes = 0

        liftedNode(node)
    }
}