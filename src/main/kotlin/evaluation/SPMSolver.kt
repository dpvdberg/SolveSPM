package evaluation

import paritygame.*

abstract class SPMSolver {
    fun solve(game : Game) : Partition {
        val progressMeasure = computeProgressMeasure(game)
        val winPartitionDiamond = game.nodes.filter { n -> progressMeasure.g.getValue(n) !is Loss }.toSet()
        val winPartitionBox = game.nodes.filter { n -> progressMeasure.g.getValue(n) is Loss }.toSet()


        return Partition(winPartitionDiamond, winPartitionBox)
    }

    private fun computeProgressMeasure(game : Game) : ProgressMeasure {
        var progMeasure = ProgressMeasure(game.maxPriority + 1)

        val liftOrder = defineLiftOrder(game.nodes)

        do {
            val next = liftOrder.asSequence()
                .map{ n -> lift(game.max, progMeasure, n)}
                .firstOrNull { pm -> progMeasure.less(pm) }

            print(progMeasure)
            println()

            if (next != null) progMeasure = next
        } while (next != null)

        return progMeasure
    }

    private fun lift(max : Tuple, progMeasure : ProgressMeasure, node : Node) : ProgressMeasure {
        val comp : Measure
        if (node.owner is Diamond) {
            comp = node.successors.map { m -> prog(max, progMeasure, node, m) }.min()!!
        } else {
            comp = node.successors.map { m -> prog(max, progMeasure, node, m) }.max()!!
        }

        progMeasure.g[node] = maxOf(comp, progMeasure.g.getValue(node))

        return progMeasure
    }

    private fun prog(max : Tuple, progMeasure: ProgressMeasure, from : Node, to : Node) : Measure {
        val prog : Measure
        val measureTo = progMeasure.g.getValue(to)

        if (from.isEven()) {
            prog = measureTo.copyUpTo(from.priority)
        } else {
            prog = measureTo.incrementUpTo(from.priority, max)
        }

        return prog
    }

    abstract fun defineLiftOrder(nodes : List<Node>): List<Node>
}