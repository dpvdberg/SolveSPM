package evaluation.lift.predecessor

import evaluation.SPMSolver
import org.junit.Assert
import org.junit.Test
import paritygame.Box
import paritygame.Diamond
import parser.GrammarPGSolverParser
import parser.PGSolverParser
import util.MinMax

internal class MetricLiftingTest {
    @Test
    fun equal() {
        val rawGame = """
            0 1 1 0,2 "X";
            1 2 0 0,3 "Y'";
            2 1 0 3,5 "X'";
            3 2 1 1,6 "Y";
            4 3 0 4 "Z'";
            5 3 0 4 "Z";
            6 3 0 6,5 "W";
        """.trimIndent()

        val game = GrammarPGSolverParser().parse(rawGame)

        val partitionMin = SPMSolver.solve(game, MetricLifting(game, MinMax.MIN))
        val partitionMax = SPMSolver.solve(game, MetricLifting(game, MinMax.MIN))

        Assert.assertEquals(partitionMin.getSet(Box).size, game.nodes.size)
        Assert.assertEquals(partitionMax.getSet(Box).size, game.nodes.size)
        Assert.assertEquals(partitionMin.getSet(Diamond).size, 0)
        Assert.assertEquals(partitionMax.getSet(Diamond).size, 0)
    }
}