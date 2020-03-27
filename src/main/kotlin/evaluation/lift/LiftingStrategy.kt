package evaluation.lift

import evaluation.ProgressMeasure
import paritygame.Node

interface LiftingStrategy {
    fun getSequence(pm: ProgressMeasure) = generateSequence {
        (getNext(pm)).takeIf { it != null }
    }

    fun getNext(pm : ProgressMeasure) : Node?

    fun setLifted(node : Node, pm : ProgressMeasure)
}