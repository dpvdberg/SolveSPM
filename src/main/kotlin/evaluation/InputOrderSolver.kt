package evaluation

import paritygame.*

abstract class InputOrderSolver : SPMSolver() {

    override fun defineLiftOrder(nodes : List<Node>): List<Node> {
        return nodes.sortedBy { n -> n.id }
    }
}