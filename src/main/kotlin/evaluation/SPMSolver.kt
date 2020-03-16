package evaluation

import paritygame.*
import sun.util.resources.en.CurrencyNames_en_AU

abstract class SPMSolver {
    fun solve(game : Game) : Partition {
        val progressMeasure = computeProgressMeasure()
        val winPartitionDiamond = game.nodes.filter { n -> progressMeasure.g[n] !is Loss }.toSet()
        val winPartitionBox = game.nodes.filter { n -> progressMeasure.g[n] is Loss }.toSet()

        return Partition(winPartitionDiamond, winPartitionBox)
    }

    private fun computeProgressMeasure() : ProgressMeasure {
        var progMeasure = ProgressMeasure()
        var lift = lift(progMeasure, getNext())
        while (progMeasure.smallerUpTo(lift)) {
            progMeasure = lift

            lift = lift(progMeasure, getNext())
        }
        return progMeasure
    }

    private fun lift(progMeasure : ProgressMeasure, node : Node) : ProgressMeasure {
        val comp : Measure
        if (node.owner is Diamond) {
            //= node.successors.map { m -> prog(game, progMeasure, node, m) }.minWith(Measure::compareTo)
            comp = Loss
        } else {
            //= node.successors.map { m -> prog(game, progMeasure, node, m) }.maxWith(Measure::compareTo)
            comp = Loss
        }
        //progMeasure.g[node].compareTo(comp)
        progMeasure.g[node] = Loss

        return progMeasure
    }

    private fun prog(progMeasure: ProgressMeasure, from : Node, to : Node) : Measure {
        var prog : Measure
        // priority is even
        if (from.priority % 2 == 0) {
            val measure = progMeasure.g[to]!!
            prog = measure.copyUpTo(from.priority)
        } else { // priority is odd
            prog = Loss
        }

        return prog
    }

    abstract fun getNext() : Node

}