package evaluation

import paritygame.*

abstract class SPMSolver {
    val liftOrder : List<Node> = defineLiftOrder()

    fun solve(game : Game) : Partition {
        val progressMeasure = computeProgressMeasure(game.max)
        val winPartitionDiamond = game.nodes.filter { n -> progressMeasure.g[n] !is Loss }.toSet()
        val winPartitionBox = game.nodes.filter { n -> progressMeasure.g[n] is Loss }.toSet()


        return Partition(winPartitionDiamond, winPartitionBox)
    }

    private fun computeProgressMeasure(max : Tuple) : ProgressMeasure {
        var progMeasure = ProgressMeasure()

        do {
            val next = liftOrder.asSequence()
                .map{ n -> lift(max, progMeasure, n)}
                .firstOrNull { pm -> progMeasure.less(pm) }

            if (next != null) progMeasure = next
        } while (next != null)

        return progMeasure
    }

    private fun lift(max : Tuple, progMeasure : ProgressMeasure, node : Node) : ProgressMeasure {
        val comp : Measure
        if (node.owner is Diamond) {
            comp = node.successors.map { m -> prog(max, progMeasure, node, m) }.minWith(MeasureComparator)!!
        } else {
            comp = node.successors.map { m -> prog(max, progMeasure, node, m) }.maxWith(MeasureComparator)!!
        }

        progMeasure.g[node] = maxOf(comp, progMeasure.g[node]!!, MeasureComparator)

        return progMeasure
    }

    private fun prog(max : Tuple, progMeasure: ProgressMeasure, from : Node, to : Node) : Measure {
        val prog : Measure
        val measureTo = progMeasure.g[to]!!

        if (from.isEven()) {
            prog = measureTo.copyUpTo(from.priority)
        } else {
            prog = measureTo.incrementUpTo(from.priority, max)
        }

        return prog
    }

    abstract fun defineLiftOrder(): List<Node>
}