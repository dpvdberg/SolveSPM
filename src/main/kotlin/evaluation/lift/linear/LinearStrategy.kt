package evaluation.lift.linear

import evaluation.ProgressMeasure
import evaluation.lift.LiftingStrategy
import paritygame.Game
import paritygame.Node

abstract class LinearStrategy(val game: Game) : LiftingStrategy {
    private var nonLiftedFetchedNodes = 0

    abstract fun fetchNextNode(): Node

    override fun getNext(pm : ProgressMeasure) : Node? {
        return if (nonLiftedFetchedNodes >= game.nodes.size) {
            /* All nodes fetched but non are lifted, we are done */
            null
        } else {
            nonLiftedFetchedNodes++

            /* Get next node for the strategy */
            fetchNextNode()
        }
    }

    override fun setLifted(node : Node, pm : ProgressMeasure) {
        nonLiftedFetchedNodes = 0
    }
}