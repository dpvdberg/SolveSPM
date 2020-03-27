package evaluation.lift.predecessor

import evaluation.Loss
import evaluation.Measure
import evaluation.ProgressMeasure
import paritygame.Game
import paritygame.Node
import util.MinMax

class MetricLifting(val game: Game, private val metric : MinMax) : PredecessorStrategy() {
    private val pending = game.nodes.toMutableSet()

    private fun nodeValue(node : Node, pm : ProgressMeasure) : Measure {
        return node.successors.map{s -> pm.g.getValue(s)}.max()!!
    }

    override fun addPendingNode(node: Node) {
        pending.add(node)
    }

    override fun isPending(node: Node): Boolean {
        return pending.contains(node)
    }

    override fun removePending(pm : ProgressMeasure): Node {
        val node : Node
        when (metric) {
           MinMax.MAX -> {
               node = pending.filter { n -> pm.g.getValue(n) !is Loss }
               .maxBy { n -> nodeValue(n, pm) }!!
           }
            MinMax.MIN -> {
                node = pending.filter { n -> pm.g.getValue(n) !is Loss }
                    .minBy { n -> nodeValue(n, pm) }!!
            }
        }

        pending.remove(node)
        return node
    }

    override fun emptyPending(): Boolean {
        return pending.isEmpty()
    }
}