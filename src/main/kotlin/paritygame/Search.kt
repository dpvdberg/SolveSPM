package paritygame

import java.util.*

fun getNodesDFS(node: Node): Sequence<Node> = sequence {
    // LIFO Queue
    val pending: Stack<Node> = Stack()
    val visited: MutableList<Node> = mutableListOf(node)
    pending.add(node)

    while (!pending.isEmpty()) {
        val next = pending.pop()
        yield(next)
        println(next.id)
        next.successors.forEach { n ->
            if (!visited.contains(n)) {
                visited.add(n)
                pending.add(n)
            }
        }
    }
}

fun getNodesBFS(node: Node): Sequence<Node> = sequence {
    // FIFO Queue
    val pending: Queue<Node> = LinkedList()
    val visited: MutableList<Node> = mutableListOf(node)
    pending.add(node)

    while (!pending.isEmpty()) {
        val next = pending.remove()
        yield(next)
        next.successors.forEach { n ->
            if (!visited.contains(n)) {
                visited.add(n)
                pending.add(n)
            }
        }
    }
}

fun getEdgesBFS(node: Node): Sequence<Pair<Node, Node>> = sequence {
    // FIFO Queue
    val pending: Queue<Node> = LinkedList()
    val visited: MutableList<Node> = mutableListOf(node)
    pending.add(node)

    while (!pending.isEmpty()) {
        val next = pending.remove()
        next.successors.forEach { n ->
            yield(Pair(next, n))

            if (!visited.contains(n)) {
                visited.add(n)
                pending.add(n)
            }
        }
    }
}