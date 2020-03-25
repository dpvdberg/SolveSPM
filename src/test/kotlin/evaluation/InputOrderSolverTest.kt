package evaluation

import org.junit.Assert.*
import org.junit.Test
import parser.PGSolverParser

internal class InputOrderSolverTest {
    @Test
    fun equal() {
        val rawGame = """
            0 1 1 0,1 "X";
            1 2 0 0,3 "Y'";
            2 1 0 3,5 "X'";
            3 2 1 1,6 "Y";
            4 3 0 4 "Z'";
            5 3 0 4 "Z";
            6 3 0 6,5 "W";
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        val solver = InputOrderSolver()
        val partition = solver.solve(game)

        assertNotNull(partition)
    }
}