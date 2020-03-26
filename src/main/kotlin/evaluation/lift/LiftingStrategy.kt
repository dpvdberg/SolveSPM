package evaluation.lift

import evaluation.ProgressMeasure
import paritygame.Node

interface LiftingStrategy {
    fun getSequence() = generateSequence {
        (getNext()).takeIf { it != null }
    }

    fun getNext() : Node?

    fun setLifted(node : Node, pm : ProgressMeasure)
}