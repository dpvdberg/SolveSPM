package paritygame

import util.QueueType
import util.SearchMethod
import util.toQueueType
import java.util.*

fun <T> ArrayDeque<T>.addToQueue(element: T, method: QueueType) {
    when (method) {
        QueueType.FIFO -> this.add(element)
        QueueType.LIFO -> this.push(element)
    }
}

fun getNodes(node: Node, method: SearchMethod): Sequence<Node> = sequence {
    val pending = ArrayDeque<Node>()
    val visited: MutableList<Node> = mutableListOf(node)
    pending.add(node)

    while (!pending.isEmpty()) {
        val next = pending.pop()
        yield(next)
        next.successors.forEach { n ->
            if (!visited.contains(n)) {
                visited.add(n)
                pending.addToQueue(n, method.toQueueType())
            }
        }
    }
}

fun getEdgesBFS(node: Node): Sequence<Pair<Node, Node>> = sequence {
    val pending = ArrayDeque<Node>()
    val visited: MutableList<Node> = mutableListOf(node)
    pending.add(node)

    while (!pending.isEmpty()) {
        val next = pending.remove()
        next.successors.forEach { n ->
            yield(Pair(next, n))

            if (!visited.contains(n)) {
                visited.add(n)
                pending.addToQueue(n, SearchMethod.BFS.toQueueType())
            }
        }
    }
}