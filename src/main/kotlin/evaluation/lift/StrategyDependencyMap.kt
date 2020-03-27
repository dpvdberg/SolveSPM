package evaluation.lift

import util.MinMax
import util.QueueType
import util.SearchMethod

val strategyDependencyMap = mapOf(
    StrategyName.PERMUTATION_IDORDER to SearchMethod.values(),
    StrategyName.PREDECESSOR to QueueType.values(),
    StrategyName.METRIC to MinMax.values()
)