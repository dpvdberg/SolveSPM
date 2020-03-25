package paritygame

import evaluation.Tuple

class Game(
    var nodes: List<Node>,
    var id: Int? = null
) {
    val maxPriority: Int = nodes.map { n -> n.priority }.max()!!

    val max : Tuple by lazy {
        val count = Tuple(maxPriority + 1)

        nodes.filter { n -> !n.isEven() }.forEach { n -> count.m[n.priority]++ }

        count
    }

}