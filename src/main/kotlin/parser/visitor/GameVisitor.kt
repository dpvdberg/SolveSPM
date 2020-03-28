package parser.visitor

import paritygame.*
import parser.grammar.ParityGameBaseVisitor
import parser.grammar.ParityGameParser

class GameVisitor(val nodeCallback : (n : Node) -> Unit) : ParityGameBaseVisitor<Any>() {

    override fun visitGame(ctx: ParityGameParser.GameContext?): Game {
        return Game(
            // Reverse distinctBy on id, to keep last with that id
            ctx!!.node()
                .map { n -> this.visit(n) as Node }
                .reversed()
                .distinctBy { n -> n.id }
                .reversed()
                .toList(),
            ctx.id?.text?.toInt()
        )
    }

    override fun visitNode(ctx: ParityGameParser.NodeContext?): Node {
        val node = Node(
            ctx!!.id.text.toInt(),
            ctx.priority.text.toInt(),
            this.visit(ctx.player()) as Player,
            emptyList(),
            emptyList(),
            this.visit(ctx.successors()) as List<Int>,
            ctx.name?.text?.trim('"')
            )

        nodeCallback(node)
        return node
    }

    override fun visitPlayer(ctx: ParityGameParser.PlayerContext?): Player {
        return when (ctx!!.p.type) {
            ParityGameParser.PLAYER0 -> Diamond
            ParityGameParser.PLAYER1 -> Box
            else -> throw IllegalStateException("Player type not found")
        }
    }

    override fun visitSuccessors(ctx: ParityGameParser.SuccessorsContext?): List<Int> {
        return ctx!!.identifier().map { n -> n.text.toInt() }.toList()
    }
}