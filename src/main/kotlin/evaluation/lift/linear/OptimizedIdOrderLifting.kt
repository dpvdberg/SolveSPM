package evaluation.lift.linear

import paritygame.Game
import paritygame.Node

class OptimizedIdOrderLifting(game : Game) : LinearStrategy(game) {
    private val linearStrategy : LinearStrategy
    init {
        val increasingEdges = game.getEdges().count {
            (a, b) -> a.id < b.id
        }
        val decreasingEdges = game.getEdges().count {
            (a, b) -> a.id > b.id
        }

        linearStrategy = if (increasingEdges > decreasingEdges) {
            ReverseIdOrderLifting(game)
        } else {
            IdOrderLifting(game)
        }
    }

    override fun fetchNextNode(): Node {
        return linearStrategy.fetchNextNode()
    }
}