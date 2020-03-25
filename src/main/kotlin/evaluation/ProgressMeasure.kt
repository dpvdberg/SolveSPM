package evaluation

import paritygame.Node

class ProgressMeasure(var length : Int) {
    val g = mutableMapOf<Node, Measure>()
        .withDefault { Tuple(length) }

    fun less(other : ProgressMeasure) : Boolean {
        return g.keys.any { node -> g.getValue(node) < other.g.getValue(node) }
    }

    override fun toString(): String {
        return g.keys
            .sortedBy { n -> n.id }
            .joinToString (separator = "\r\n") { n -> "${n.id} \t| ${n.name} \t| ${g.getValue(n)}" }
    }
}