package evaluation

import paritygame.Node

class ProgressMeasure {

    val g = mutableMapOf<Node, Measure>()

    fun smallerUpTo(g : ProgressMeasure) : Boolean {
        return false
    }
}