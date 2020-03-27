package util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

internal class QueueTypeKtTest {
    @Test
    fun LifoTest() {
        val q = ArrayDeque<Int>()
        q.addToQueue(1, QueueType.LIFO)
        q.addToQueue(2, QueueType.LIFO)
        q.addToQueue(3, QueueType.LIFO)
        q.addToQueue(4, QueueType.LIFO)
        q.addToQueue(5, QueueType.LIFO)
        q.addToQueue(6, QueueType.LIFO)

        assertEquals(q.pop(), 6)
        assertEquals(q.pop(), 5)
    }

    @Test
    fun FifoTest() {
        val q = ArrayDeque<Int>()
        q.addToQueue(1, QueueType.FIFO)
        q.addToQueue(2, QueueType.FIFO)
        q.addToQueue(3, QueueType.FIFO)
        q.addToQueue(4, QueueType.FIFO)
        q.addToQueue(5, QueueType.FIFO)
        q.addToQueue(6, QueueType.FIFO)

        assertEquals(q.pop(), 1)
        assertEquals(q.pop(), 2)
    }


    @Test
    fun LifoTestRemove() {
        val q = ArrayDeque<Int>()
        q.addToQueue(1, QueueType.LIFO)
        q.addToQueue(2, QueueType.LIFO)
        q.addToQueue(3, QueueType.LIFO)
        q.addToQueue(4, QueueType.LIFO)
        q.addToQueue(5, QueueType.LIFO)
        q.addToQueue(6, QueueType.LIFO)

        assertEquals(q.remove(), 6)
        assertEquals(q.remove(), 5)
    }

    @Test
    fun FifoTestRemove() {
        val q = ArrayDeque<Int>()
        q.addToQueue(1, QueueType.FIFO)
        q.addToQueue(2, QueueType.FIFO)
        q.addToQueue(3, QueueType.FIFO)
        q.addToQueue(4, QueueType.FIFO)
        q.addToQueue(5, QueueType.FIFO)
        q.addToQueue(6, QueueType.FIFO)

        assertEquals(q.remove(), 1)
        assertEquals(q.remove(), 2)
    }
}