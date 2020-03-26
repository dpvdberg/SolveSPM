package paritygame

import org.junit.Assert.assertEquals
import org.junit.Test
import parser.PGSolverParser

internal class GameTest {

    @Test
    fun startingNode() {
        val rawGame = """
            0 1 1 0,2 "X";
            1 2 0 0,3 "Y'";
            2 1 0 3,5 "X'";
            3 2 1 1,6 "Y";
            4 3 0 4 "Z'";
            5 3 0 4 "Z";
            6 3 0 6,5 "W";
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        assertEquals(game.startingNode.id, 0)
    }

    @Test
    fun getNodesDFS() {
        val rawGame = """
            0 1 1 0,2 "X";
            1 2 0 0,3 "Y'";
            2 1 0 3,5 "X'";
            3 2 1 1,6 "Y";
            4 3 0 4 "Z'";
            5 3 0 4 "Z";
            6 3 0 6,5 "W";
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        assertEquals(game.getNodes(SearchMethod.DFS).count(), game.nodes.size)

        assertEquals(game.getNodes(SearchMethod.DFS).first().id, 0)
        assertEquals(game.getNodes(SearchMethod.DFS).last().id, 1)
        assertEquals(game.getNodes(SearchMethod.DFS).elementAt(3).id, 4)
    }

    @Test
    fun getNodesBFS() {
        val rawGame = """
            0 1 1 0,2 "X";
            1 2 0 0,3 "Y'";
            2 1 0 3,5 "X'";
            3 2 1 1,6 "Y";
            4 3 0 4 "Z'";
            5 3 0 4 "Z";
            6 3 0 6,5 "W";
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        assertEquals(game.getNodes(SearchMethod.BFS).count(), game.nodes.size)

        assertEquals(game.getNodes(SearchMethod.BFS).first().id, 0)
        assertEquals(game.getNodes(SearchMethod.BFS).last().id, 4)
        assertEquals(game.getNodes(SearchMethod.BFS).elementAt(3).id, 5)
    }

    @Test
    fun getEdgesDFS() {
        val rawGame = """
            0 1 1 0,2 "X";
            1 2 0 0,3 "Y'";
            2 1 0 3,5 "X'";
            3 2 1 1,6 "Y";
            4 3 0 4 "Z'";
            5 3 0 4 "Z";
            6 3 0 6,5 "W";
        """.trimIndent()

        val game = PGSolverParser.parse(rawGame)

        assertEquals(game.getEdgesBFS().count(), 12)

        val (a, b) = game.getEdgesBFS().last()
        assert(a.id == 4 && b.id == 4)

        val (c, d) = game.getEdgesBFS().elementAt(2)
        assert(c.id == 2 && d.id == 3)
    }
}