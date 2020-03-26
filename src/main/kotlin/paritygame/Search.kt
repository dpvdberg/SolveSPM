package paritygame

import util.SearchMethod
import java.util.*

fun <T> ArrayDeque<T>.addToQueue(element: T, method: SearchMethod) {
    when (method) {
        // FIFO
        SearchMethod.BFS -> this.add(element)
        // LIFO
        SearchMethod.DFS -> this.push(element)
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
                pending.addToQueue(n, method)
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
                pending.addToQueue(n, SearchMethod.BFS)
            }
        }
    }
}