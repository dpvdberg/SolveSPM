package evaluation

import paritygame.*
import kotlin.random.Random

abstract class RandomOrderSolver(var seed : Int) : SPMSolver() {

    override fun defineLiftOrder(nodes : List<Node>): List<Node> {
        return nodes.shuffled(Random(seed))
    }
}