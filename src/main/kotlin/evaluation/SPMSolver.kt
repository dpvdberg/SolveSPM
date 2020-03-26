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
        val progressMeasure = ProgressMeasure(game.maxPriority + 1)

        val liftOrder = applyLiftingStrategy(game.nodes)

        do {
            val next = liftOrder
                .firstOrNull { n -> progressMeasure.g.getValue(n) < lift(game.max, progressMeasure, n) }

            print(progressMeasure)
            println()

            if (next != null) progressMeasure.g[next] = lift(game.max, progressMeasure, next)
        } while (next != null)

        return progressMeasure
    }

    private fun lift(max : Tuple, progMeasure : ProgressMeasure, node : Node) : Measure {
        val comp : Measure

        comp = if (node.owner is Diamond) {
            node.successors.map { m -> prog(max, progMeasure, node, m) }.min()!!
        } else {
            node.successors.map { m -> prog(max, progMeasure, node, m) }.max()!!
        }

        return maxOf(comp, progMeasure.g.getValue(node))
    }

    private fun prog(max : Tuple, progMeasure: ProgressMeasure, from : Node, to : Node) : Measure {
        val prog : Measure
        val measureTo = progMeasure.g.getValue(to)

        prog = if (from.isEven()) {
            measureTo.copySubTuple(from.priority)
        } else {
            measureTo.incrementUpTo(from.priority, max)
        }

        return prog
    }

    abstract fun applyLiftingStrategy(nodes : List<Node>): List<Node>
}