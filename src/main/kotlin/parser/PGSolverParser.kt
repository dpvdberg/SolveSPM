package parser

import Exception.ParsingException
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import paritygame.Game
import parser.grammar.ParityGameLexer
import parser.grammar.ParityGameParser
import parser.visitor.GameVisitor

class PGSolverParser {
    companion object {
        fun parse(data: String): Game {
            val lexer = ParityGameLexer(CharStreams.fromString(data))
            val tokens = CommonTokenStream(lexer)
            val parser = ParityGameParser(tokens)
            val visitor = GameVisitor()

            val parityGame = visitor.visit(parser.game()) as Game

            if (parser.numberOfSyntaxErrors > 0) {
                throw ParsingException("Syntax error during the parsing of parity game: $data")
            }

            setNodes(parityGame)
            return parityGame
        }

        private fun setNodes(parityGame: Game) {
            parityGame.nodes.forEach { n ->
                n.successors = n.successorsIds.map { id ->
                    parityGame.nodes.first { m -> m.id == id }
                }.toList()
            }

            parityGame.nodes.forEach { n ->
                n.predecessors = parityGame.nodes.filter { m ->
                    n in m.successors
                }.toList()
            }
        }
    }
}