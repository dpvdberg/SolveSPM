package evaluation.lift.predecessor

import datastructure.MaxHeap
import evaluation.Loss
import evaluation.ProgressMeasure
import evaluation.ProgressMeasureNodeComparator
import paritygame.Game
import paritygame.Node
import util.MinMax

class MetricLifting(val game: Game, private val metric: MinMax) : PredecessorStrategy() {
    // To be initialized
    private var pending: MaxHeap<Node>? = null

    override fun initialize() {
        pending = MaxHeap(game.nodes.size, ProgressMeasureNodeComparator(this.getProgressMeasure(), metric))
        game.nodes.forEach { n -> pending?.insert(n) }
    }

    override fun addPendingNode(node: Node) {
        pending?.insert(node)
    }

    override fun isPending(node: Node): Boolean {
        return pending!!.contains(node)
    }

    override fun removePending(pm: ProgressMeasure): Node {
        return pending!!.extract()
    }

    override fun emptyPending(): Boolean {
        return pending!!.isEmpty
    }
}