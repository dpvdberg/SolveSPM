package parser

import Exception.ParsingException
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import paritygame.Game
import parser.grammar.ParityGameLexer
import parser.grammar.ParityGameParser
import parser.visitor.GameVisitor

class GrammarPGSolverParser : PGSolverParser() {
    override fun parse(data: String): Game {
        val lexer = ParityGameLexer(CharStreams.fromString(data))
        val tokens = CommonTokenStream(lexer)
        val parser = ParityGameParser(tokens)
        val visitor = GameVisitor()

        val parityGame = visitor.visit(parser.game()) as Game

        if (parser.numberOfSyntaxErrors > 0) {
            throw ParsingException("Syntax error during the parsing of parity game: $data")
        }

        // TODO: Fix dit hier
        setNodes(parityGame, nodeMap, predecessorMap)
        return parityGame
    }
}