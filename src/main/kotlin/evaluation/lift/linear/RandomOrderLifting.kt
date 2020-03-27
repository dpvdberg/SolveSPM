package evaluation.lift.linear

import paritygame.Game
import paritygame.Node
import kotlin.random.Random

class RandomOrderLifting(game : Game, seed : Int) : LinearStrategy(game) {
    private val sortedNodes = game.nodes.shuffled(Random(seed))
    private var index = 0

    override fun fetchNextNode(): Node {
        val node = sortedNodes[index]
        index = (index + 1) % sortedNodes.size

        return node
    }
}