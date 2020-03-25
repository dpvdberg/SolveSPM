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
                .map{ n -> Pair(n, lift(game.max, progMeasure, n)) }
                .firstOrNull { pair -> progMeasure.g.getValue(pair.first) < pair.second }

            print(progMeasure)
            println()

            if (next != null) progMeasure.g[next.first] = next.second
        } while (next != null)

        return progMeasure
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
            measureTo.copyUpTo(from.priority)
        } else {
            measureTo.incrementUpTo(from.priority, max)
        }

        return prog
    }

    abstract fun defineLiftOrder(nodes : List<Node>): List<Node>
}