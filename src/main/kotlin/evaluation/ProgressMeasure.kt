package evaluation

import paritygame.Node

class ProgressMeasure {
    val g = mutableMapOf<Node, Measure>()

    fun lessOrEqual(h : ProgressMeasure) : Boolean {
        return g.keys.all { k -> g[k]!! <= h.g[k]!! }
    }
}