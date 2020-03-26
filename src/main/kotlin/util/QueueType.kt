package util

import java.util.*

enum class QueueType {
    LIFO, FIFO
}

fun <T> Deque<T>.addToQueue(element: T, method: QueueType) {
    when (method) {
        QueueType.FIFO -> this.add(element)
        QueueType.LIFO -> this.push(element)
    }
}