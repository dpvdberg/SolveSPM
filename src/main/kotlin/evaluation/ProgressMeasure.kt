package evaluation

import paritygame.Node

class ProgressMeasure {

    val g = mutableMapOf<Node, Measure>()

    // lecture 8, slide 16, maar dan < ipv <=
    fun smallerUpTo(h : ProgressMeasure) : Boolean {
        return false
    }

    fun max(h : ProgressMeasure) : Boolean {
        return false
    }
}