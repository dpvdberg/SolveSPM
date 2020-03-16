package evaluation

import paritygame.*
import sun.util.resources.en.CurrencyNames_en_AU

abstract class SPMSolver {
    fun solve(game : Game) : Partition {
        val progressMeasure = computeProgressMeasure(game)
        val winPartitionDiamond = game.nodes.filter { n -> progressMeasure.g[n] !is Loss }.toSet()
        val winPartitionBox = game.nodes.filter { n -> progressMeasure.g[n] is Loss }.toSet()

        return Partition(winPartitionDiamond, winPartitionBox)
    }

    fun computeProgressMeasure(game : Game) : ProgressMeasure {
        var progMeasure = ProgressMeasure()
        var next = getNext()
        var lift = lift(progMeasure, next)
        while (progMeasure.smallerUpTo(lift)) {
            progMeasure = lift

            next = getNext()
            lift = lift(progMeasure, next)
        }
        return progMeasure
    }

    fun lift(progMeasure : ProgressMeasure, node : Node) : ProgressMeasure {
        var max : Measure
        if (node.owner is Diamond) {

        } else {

        }
        return ProgressMeasure()
    }

    fun prog(progMeasure: ProgressMeasure, n : Node, m : Node) : Measure{
        return Loss()
    }

    abstract fun getNext() : Node

}