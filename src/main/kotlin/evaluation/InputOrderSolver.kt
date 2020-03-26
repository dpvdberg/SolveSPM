package evaluation

import paritygame.*

class InputOrderSolver : SPMSolver() {
    override fun applyLiftingStrategy(nodes : List<Node>): List<Node> {
        return nodes.sortedBy { n -> n.id }
    }
}