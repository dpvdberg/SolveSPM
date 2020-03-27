package evaluation.lift

import util.MinMax
import util.QueueType
import util.SearchMethod

enum class StrategyName(val fancyName : String) {
    IDORDER("id"),
    REVERSED_IDORDER("id_reversed"),
    OPTIMIZED_IDORDER("id_optimized"),
    PERMUTATION_IDORDER("id_permutation"),
    PREDECESSOR("predecessor"),
    METRIC("metric");

    companion object {
        val map = values().associate { s -> s.fancyName to s }
    }
}