package evaluation.lift.predecessor

import evaluation.SPMSolver
import org.junit.Assert
import org.junit.Test
import paritygame.Box
import paritygame.Diamond
import parser.GrammarPGSolverParser
import parser.PGSolverParser
import util.QueueType

internal class PredecessorStrategyTest {
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

        val partitionFIFO = SPMSolver.solve(game, PredecessorLifting(game, QueueType.FIFO))
        val partitionLIFO = SPMSolver.solve(game, PredecessorLifting(game, QueueType.LIFO))

        Assert.assertEquals(partitionFIFO.getSet(Box).size, game.nodes.size)
        Assert.assertEquals(partitionLIFO.getSet(Box).size, game.nodes.size)
        Assert.assertEquals(partitionFIFO.getSet(Diamond).size, 0)
        Assert.assertEquals(partitionLIFO.getSet(Diamond).size, 0)
    }
}