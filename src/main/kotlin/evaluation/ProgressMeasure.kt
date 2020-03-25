package evaluation

import paritygame.Node

class ProgressMeasure {
    val g = mutableMapOf<Node, Measure>()

    fun less(h : ProgressMeasure) : Boolean {
        return g.keys.any { k -> g[k]!! < h.g[k]!! }
    }

    override fun toString(): String {
        return g.keys
            .sortedBy { n -> n.id }
            .joinToString (separator = "\r\n") { n -> "${n.id}\t| ${n.name}\t| ${g[n]}" }
    }
}