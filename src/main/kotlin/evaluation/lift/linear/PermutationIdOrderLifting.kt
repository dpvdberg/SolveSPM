package evaluation.lift.linear

import SearchMethod
import paritygame.Game

class PermutationIdOrderLifting(game : Game, searchMethod: SearchMethod) : ReverseIdOrderLifting(game) {
    override val sortedNodes = game.getNodes(searchMethod).toList()
}