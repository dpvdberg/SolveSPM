package evaluation

import evaluation.lift.LiftingStrategy
import evaluation.lift.linear.*
import evaluation.lift.predecessor.MetricLifting
import evaluation.lift.predecessor.PredecessorLifting
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import paritygame.Box
import paritygame.Diamond
import paritygame.Game
import parser.PGSolverParser
import util.MinMax
import util.QueueType
import util.SearchMethod
import java.util.*

internal class ParityGameTests {

    private fun getStrategies(game : Game) : List<LiftingStrategy> {
        return listOf(
            IdOrderLifting(game),
            ReverseIdOrderLifting(game),
            OptimizedIdOrderLifting(game),
            PermutationIdOrderLifting(game, SearchMethod.BFS),
            PermutationIdOrderLifting(game, SearchMethod.DFS),
            PredecessorLifting(game, QueueType.FIFO),
            PredecessorLifting(game, QueueType.LIFO),
            MetricLifting(game, MinMax.MIN),
            MetricLifting(game, MinMax.MAX),
            RandomOrderLifting(game, 0)
        )
    }

    @Test
    fun equal() {
        val rawGame = """
            0 0 1 1,6 ;
            1 3 0 2 ;
            2 2 1 3 ;
            3 5 0 4 ;
            4 8 1 2,3 ;
            5 6 0 4,6 ;
            6 1 1 5,7 ;
            7 4 0 6,0 ;
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        val strategies = getStrategies(game)

        for (strategy in strategies) {
            val partition = SPMSolver.solve(game, strategy)


            assertEquals(partition.getSet(Box).size, game.nodes.size)
            assertEquals(partition.getSet(Diamond).size, 0)
        }
    }

    @Test
    fun equal1() {
        val rawGame = """
            1 1 1 2,3;
            2 2 0 1,2;
            3 3 0 1,3,4;
            4 0 0 1;
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        val strategies = getStrategies(game)

        for (strategy in strategies) {
            val partition = SPMSolver.solve(game, strategy)


            assertEquals(partition.getSet(Box).size, 0)
            assertEquals(partition.getSet(Diamond).size, game.nodes.size)
        }
    }

    @Test
    fun equal2() {
        val rawGame = """
            0 0 0 1,2;
            1 1 1 1;
            2 2 1 2;
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        val strategies = getStrategies(game)

        for (strategy in strategies) {
            val partition = SPMSolver.solve(game, strategy)


            assertEquals(partition.getSet(Box).size, 1)
            assertEquals(partition.getSet(Diamond).size, 2)
        }
    }


}