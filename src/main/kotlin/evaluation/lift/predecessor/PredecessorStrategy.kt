package evaluation.lift.predecessor

import evaluation.Loss
import evaluation.ProgressMeasure
import evaluation.lift.LiftingStrategy
import paritygame.Game
import paritygame.Node
import util.QueueType
import util.addToQueue
import java.util.*

class PredecessorStrategy(val game: Game, private val type : QueueType) : LiftingStrategy {
    private val pending = ArrayDeque(game.nodes)

    override fun getNext() : Node? {
        if (pending.isEmpty()) {
            return null
        }
        return pending.pop()
    }

    override fun setLifted(node: Node, pm: ProgressMeasure) {
        for (w in node.predecessors) {
            if (!pending.contains(w) && pm.g[w] !is Loss) {
                pending.addToQueue(w, type)
            }
        }
    }
}