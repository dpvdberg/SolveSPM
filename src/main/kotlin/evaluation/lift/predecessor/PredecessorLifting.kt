package evaluation.lift.predecessor

import evaluation.Loss
import evaluation.ProgressMeasure
import evaluation.lift.LiftingStrategy
import paritygame.Game
import paritygame.Node
import util.QueueType
import util.addToQueue
import java.util.*

class PredecessorLifting(game: Game, private val type : QueueType) : PredecessorStrategy() {
    private val pending = ArrayDeque(game.nodes)

    override fun addPendingNode(node: Node) {
        pending.addToQueue(node, type)
    }

    override fun isPending(node: Node): Boolean = pending.contains(node)

    override fun removePending(): Node = pending.remove()

    override fun emptyPending(): Boolean = pending.isEmpty()
}