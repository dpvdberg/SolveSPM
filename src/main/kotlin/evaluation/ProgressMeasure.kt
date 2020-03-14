package evaluation

import paritygame.Node

class ProgressMeasure {

    val g = mutableMapOf<Node, Measure>()

    fun smallerUpTo(h : ProgressMeasure) : Boolean {
        return false
    }

    fun max(h : ProgressMeasure) : Boolean {
        return false
    }
}