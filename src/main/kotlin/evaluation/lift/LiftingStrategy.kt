package evaluation.lift

import evaluation.ProgressMeasure
import paritygame.Node

abstract class LiftingStrategy {
    private var progressMeasure = ProgressMeasure(0)

    fun getSequence(pm: ProgressMeasure) = generateSequence {
        (getNext(pm)).takeIf { it != null }
    }

    abstract fun getNext(pm : ProgressMeasure) : Node?

    abstract fun setLifted(node : Node, pm : ProgressMeasure)

    internal fun setProgressMeasure(pm: ProgressMeasure) {
        this.progressMeasure = pm
    }

    internal fun getProgressMeasure() : ProgressMeasure {
        return this.progressMeasure
    }

    open fun initialize() {}
}