package util

enum class SearchMethod {
    DFS,
    BFS
}

fun SearchMethod.toQueueType(): QueueType {
    return when (this) {
        SearchMethod.BFS -> QueueType.FIFO
        SearchMethod.DFS -> QueueType.LIFO
    }
}