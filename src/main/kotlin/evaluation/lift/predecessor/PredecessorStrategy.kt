package evaluation.lift.predecessor

import evaluation.Loss
import evaluation.ProgressMeasure
import evaluation.lift.LiftingStrategy
import paritygame.Node

abstract class PredecessorStrategy : LiftingStrategy {
    override fun getNext() : Node? {
        if (emptyPending()) {
            return null
        }
        return removePending()
    }

    override fun setLifted(node: Node, pm: ProgressMeasure) {
        for (w in node.predecessors) {
            if (!isPending(w) && pm.g[w] !is Loss) {
                addPendingNode(w)
            }
        }
    }

    abstract fun addPendingNode(node : Node)
    abstract fun isPending(node : Node) : Boolean
    abstract fun removePending() : Node
    abstract fun emptyPending() : Boolean
}