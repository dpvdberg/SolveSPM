package evaluation

import evaluation.lift.LiftingStrategy
import paritygame.*
import printlnv
import printlnvv

class SPMSolver {
    companion object {
        fun solveTimed(game : Game, liftingStrategy: LiftingStrategy) : Pair<Partition, Long> {
            val start = System.nanoTime()
            val result = solve(game, liftingStrategy)
            return Pair(result, System.nanoTime() - start)
        }

        fun solve(game: Game, liftingStrategy: LiftingStrategy): Partition {
            val progressMeasure = computeProgressMeasure(game, liftingStrategy)
            val winPartitionDiamond = game.nodes.filter { n -> progressMeasure.g.getValue(n) !is Loss }.toSet()
            val winPartitionBox = game.nodes.filter { n -> progressMeasure.g.getValue(n) is Loss }.toSet()

            return Partition(winPartitionDiamond, winPartitionBox)
        }

        private fun computeProgressMeasure(game: Game, liftingStrategy: LiftingStrategy): ProgressMeasure {
            val progressMeasure = ProgressMeasure(game.maxPriority + 1)

            var iteration = 0

            do {
                val next = liftingStrategy
                    .getSequence(progressMeasure)
                    .firstOrNull { n -> progressMeasure.g.getValue(n) < lift(game.max, progressMeasure, n) }

                printlnv("Iteration: $iteration")
                printlnvv(progressMeasure)
                printlnvv("")

                if (next != null) {
                    progressMeasure.g[next] = lift(game.max, progressMeasure, next)
                    liftingStrategy.setLifted(next, progressMeasure)
                }
                iteration++
            } while (next != null)

            SolveSPM.benchmarkIterations = iteration

            return progressMeasure
        }

        private fun lift(max: Tuple, progMeasure: ProgressMeasure, node: Node): Measure {
            val comp: Measure

            comp = if (node.owner is Diamond) {
                node.successors.map { m -> prog(max, progMeasure, node, m) }.min()!!
            } else {
                node.successors.map { m -> prog(max, progMeasure, node, m) }.max()!!
            }

            return maxOf(comp, progMeasure.g.getValue(node))
        }

        private fun prog(max: Tuple, progMeasure: ProgressMeasure, from: Node, to: Node): Measure {
            val prog: Measure
            val measureTo = progMeasure.g.getValue(to)

            prog = if (from.isEven()) {
                measureTo.copySubTuple(from.priority)
            } else {
                measureTo.incrementUpTo(from.priority, max)
            }

            return prog
        }
    }
}