package parser.visitor

import paritygame.*
import parser.grammar.ParityGameBaseVisitor
import parser.grammar.ParityGameParser

class GameVisitor : ParityGameBaseVisitor<Any>() {
    override fun visitGame(ctx: ParityGameParser.GameContext?): Game {
        return Game(
            ctx!!.node().map { n -> this.visit(n) }.toList() as List<Node>, ctx.id?.text?.toInt()
        )
    }

    override fun visitNode(ctx: ParityGameParser.NodeContext?): Node {
        return Node(
            ctx!!.id.text.toInt(),
            ctx.priority.text.toInt(),
            this.visit(ctx.player()) as Player,
            emptyList(),
            this.visit(ctx.successors()) as List<Int>,
            ctx.name?.text
            )
    }

    override fun visitPlayer(ctx: ParityGameParser.PlayerContext?): Player {
        return when (ctx!!.p.type) {
            ParityGameParser.PLAYER0 -> Diamond
            ParityGameParser.PLAYER1 -> Box
            else -> throw IllegalStateException("operation not found")
        }
    }

    override fun visitSuccessors(ctx: ParityGameParser.SuccessorsContext?): List<Int> {
        return ctx!!.NUMBER().map { n -> n.text.toInt() }.toList()
    }
}