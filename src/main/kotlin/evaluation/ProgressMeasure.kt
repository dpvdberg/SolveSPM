package evaluation

import paritygame.Node

class ProgressMeasure {
    val g = mutableMapOf<Node, Measure>()

    fun less(h : ProgressMeasure) : Boolean {
        return g.keys.any { k -> g[k]!! < h.g[k]!! }
    }
}