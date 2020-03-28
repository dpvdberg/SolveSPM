package evaluation

import paritygame.Node
import util.MinMax

class ProgressMeasureNodeComparator(private val pm : ProgressMeasure, private val minMax: MinMax) : Comparator<Node> {
    override fun compare(o1: Node?, o2: Node?): Int {
        return getMinMaxSuccessorMeasure(o1!!).compareTo(getMinMaxSuccessorMeasure(o2!!))
    }

    private fun getMinMaxSuccessorMeasure(node : Node) : Measure {
        return when (minMax) {
            MinMax.MAX -> node.successors.map { s -> pm.g.getValue(s) }.max()!!
            MinMax.MIN -> node.successors.map { s -> pm.g.getValue(s) }.min()!!
        }
    }
}