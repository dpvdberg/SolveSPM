package evaluation.lift.predecessor

import evaluation.Loss
import evaluation.ProgressMeasure
import evaluation.lift.LiftingStrategy
import paritygame.Node

abstract class PredecessorStrategy : LiftingStrategy {
    override fun getNext(pm : ProgressMeasure) : Node? {
        if (emptyPending()) {
            return null
        }
        return removePending(pm)
    }

    override fun setLifted(node: Node, pm: ProgressMeasure) {
        for (w in node.predecessors) {
            if (!isPending(w) && pm.g.getValue(w) !is Loss) {
                addPendingNode(w)
            }
        }
    }

    abstract fun addPendingNode(node : Node)
    abstract fun isPending(node : Node) : Boolean
    abstract fun removePending(pm : ProgressMeasure) : Node
    abstract fun emptyPending() : Boolean
}