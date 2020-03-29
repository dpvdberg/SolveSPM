package evaluation.lift.linear

import paritygame.Game
import paritygame.Node
import printlnv
import kotlin.random.Random

class RandomOrderLifting(game : Game, seed : Int? = null) : LinearStrategy(game) {
    private val random = if (seed == null) Random.Default else Random(seed)
    private val sortedNodes = game.nodes.shuffled(random)
    private var index = 0

    init {
        if (seed == null) {
            printlnv("Using default random")
        } else {
            printlnv("Using seed: $seed")
        }
    }

    override fun fetchNextNode(): Node {
        val node = sortedNodes[index]
        index = (index + 1) % sortedNodes.size

        return node
    }
}