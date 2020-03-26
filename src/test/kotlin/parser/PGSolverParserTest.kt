package parser

import org.junit.Assert.*
import org.junit.Test
import paritygame.Game

internal class PGSolverParserTest {

    @Test
    fun parseSimpleTest() {
        val sample =
            """
            parity 4;
            1 3 0 1,3,4 "Europe";
            0 6 1 4,2 "Africa";
            4 5 1 0 "Antarctica";
            1 8 1 2,4,3 "America";
            3 6 0 4,2 "Australia";
            2 7 0 3,1,0,4;
            """

        val game = PGSolverParser.parse(sample)

        assertNotNull(game)
        assert(game is Game)
        assert(game!!.id == 4)
        assert(game.nodes.size == 5)

        var node = game.nodes.first { n -> n.id == 1}
        assert(node.id == 1)
        assert(node.priority == 8)
        assert(node.name == "America")
        assert(node.successorsIds.size == 3)
        assert(node.successors.any { n -> n.id == 2})
        assert(node.successors.any { n -> n.id == 4})
        assert(node.successors.any { n -> n.id == 3})

        node = game.nodes.first { n -> n.id == 2}
        assert(node.id == 2)
        assert(node.priority == 7)
        assertNull(node.name)
    }

    @Test
    fun parseNoHeaderTest() {
        val sample =
            """
            1 3 0 1,3,4 "Europe";
            0 6 1 4,2 "Africa";
            4 5 1 0 "Antarctica";
            1 8 1 2,4,3 "America";
            3 6 0 4,2 "Australia";
            2 7 0 3,1,0,4;
            """

        val game = PGSolverParser.parse(sample)

        assertNotNull(game);
        assert(game is Game)
        assertNull(game!!.id)
        assert(game.nodes.size == 5)
    }
}