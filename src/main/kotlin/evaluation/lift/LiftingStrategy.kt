package evaluation.lift

import paritygame.Game
import paritygame.Node

interface LiftingStrategy {
    fun getSequence() = generateSequence {
        (getNext()).takeIf { it != null }
    }

    fun getNext() : Node?

    fun setLifted(node : Node)
}