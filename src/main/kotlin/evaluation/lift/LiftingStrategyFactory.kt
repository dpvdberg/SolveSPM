package evaluation.lift

import evaluation.lift.linear.IdOrderLifting
import evaluation.lift.linear.OptimizedIdOrderLifting
import evaluation.lift.linear.PermutationIdOrderLifting
import evaluation.lift.linear.ReverseIdOrderLifting
import evaluation.lift.predecessor.MetricLifting
import evaluation.lift.predecessor.PredecessorLifting
import paritygame.Game
import util.MinMax
import util.QueueType
import util.SearchMethod

class LiftingStrategyFactory {
    fun createLiftingStrategy(
        name : StrategyName,
        game : Game,
        searchMethod: SearchMethod? = null,
        queueType: QueueType? = null,
        minMax : MinMax? = null) : LiftingStrategy {
        return when (name) {
            StrategyName.IDORDER ->
                IdOrderLifting(game)
            StrategyName.REVERSED_IDORDER ->
                ReverseIdOrderLifting(game)
            StrategyName.OPTIMIZED_IDORDER ->
                OptimizedIdOrderLifting(game)
            StrategyName.PERMUTATION_IDORDER ->
                PermutationIdOrderLifting(game, searchMethod ?: throw IllegalArgumentException("Search method expected"))
            StrategyName.PREDECESSOR ->
                PredecessorLifting(game, queueType ?: throw IllegalArgumentException("Queue type expected"))
            StrategyName.METRIC ->
                MetricLifting(game, minMax ?: throw IllegalArgumentException("Min or max expected"))
        }
    }
}