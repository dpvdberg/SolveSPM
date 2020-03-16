package evaluation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

internal class MeasureTest {

    @Test
    fun equal() {
        val m1 = Tuple(intArrayOf(1, 2, 3))
        val m2 = Tuple(intArrayOf(1, 2, 3))

        assert(m1 <= m2)
        assert(m1 >= m2)

        assertFalse(m1 < m2)
        assertFalse(m1 > m2)
    }

    @Test
    fun greater() {
        val m1 = Tuple(intArrayOf(1, 2, 4))
        val m2 = Tuple(intArrayOf(1, 2, 3))

        assert(m1 > m2)
        assertFalse(m1 < m2)
    }


    @Test
    fun greaterLexicographical() {
        val m1 = Tuple(intArrayOf(1, 3, 4))
        val m2 = Tuple(intArrayOf(1, 2, 1))

        assert(m1 > m2)
        assertFalse(m1 < m2)
    }

    @Test
    fun equalUpTo() {
        val m1 = Tuple(intArrayOf(1, 3, 4))
        val m2 = Tuple(intArrayOf(1, 2, 1))

        assert(m1 <= Pair(m2, 0))
        assert(m1 >= Pair(m2, 0))

        assertFalse(m1 <= Pair(m2, 1))
        assert(m1 >= Pair(m2, 1))
    }

    @Test
    fun testLoss() {
        val m1 = Loss
        val m2 = Tuple(intArrayOf(1, 2, 3))

        assert(m1 >= m2)
        assert(m1 >= Pair(m2, 0))
        assert(m1 >= Pair(m2, 1))
        assert(m1 >= Pair(m2, 2))

        assertFalse(m1 <= m2)
        assertFalse(m1 <= Pair(m2, 0))
        assertFalse(m1 <= Pair(m2, 1))
        assertFalse(m1 <= Pair(m2, 2))

        assert(m1 > m2)
        assert(m1 > Pair(m2, 0))
        assert(m1 > Pair(m2, 1))
        assert(m1 > Pair(m2, 2))

        assertFalse(m1 < m2)
        assertFalse(m1 < Pair(m2, 0))
        assertFalse(m1 < Pair(m2, 1))
        assertFalse(m1 < Pair(m2, 2))
    }

    @Test
    fun testLossAgainstLoss() {
        val m1 = Loss
        val m2 = Loss

        assert(m1 >= m2)
        assert(m1 <= m2)

        assertFalse(m1 > m2)
        assertFalse(m1 < m2)
    }

    @Test
    fun testMax() {
        val m1 = Tuple(intArrayOf(1, 2, 3, 4, 5, 6))
        val m2 = Tuple(intArrayOf(1, 2, 3, 4, 4, 6))
        val m3 = Tuple(intArrayOf(1, 2, 3, 5, 5, 6))
        val m4 = Tuple(intArrayOf(1, 2, 3, 4, 0, 0))

        val max = listOf(m1, m2, m3, m4).maxWith(MeasureComparator)

        assertEquals(m3, max)
    }

    @Test
    fun testMin() {
        val m1 = Tuple(intArrayOf(1, 2, 3, 4, 5, 6))
        val m2 = Tuple(intArrayOf(1, 2, 3, 4, 4, 6))
        val m3 = Tuple(intArrayOf(1, 2, 3, 5, 5, 6))
        val m4 = Tuple(intArrayOf(1, 2, 3, 4, 0, 0))

        val min = listOf(m1, m2, m3, m4).minWith(MeasureComparator)

        assertEquals(m4, min)
    }
}