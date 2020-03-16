package evaluation

import kotlin.math.pow

sealed class Measure(var length: Int) {
    fun copyUpTo(i : Int) : Measure {
        return when (this) {
            is Loss -> Loss
            is Tuple -> {
                val copy = Tuple(this.length)
                (0..i).forEach {j -> copy.m[j] = this.m[j]}

                copy
            }
        }
    }

    fun incrementUpTo(i: Int, max : Tuple): Measure {
        when (this) {
            is Loss -> return Loss
            is Tuple -> {
                val current = this.copyUpTo(i) as Tuple

                return current.incrementUpTo(i, max, current)
            }
        }
    }


    operator fun compareTo(pair: Pair<Measure, Int>): Int {
        return MeasureUpToComparator.compare(Pair(this, pair.second), pair)
    }

    operator fun compareTo(other: Measure): Int {
        return MeasureComparator.compare(this, other)
    }
}

class MeasureComparator {
    companion object : Comparator<Measure> {
        override fun compare(o1: Measure, o2: Measure): Int {
            return MeasureUpToComparator.compare(Pair(o1, o1.length - 1), Pair(o2, o2.length - 1))
        }
    }


}

class MeasureUpToComparator {
    companion object : Comparator<Pair<Measure, Int>> {
        override fun compare(o1: Pair<Measure, Int>, o2: Pair<Measure, Int>): Int {
            if (o1.first is Loss) {
                return if (o2.first is Loss) {
                    0
                } else {
                    1
                }
            } else if (o2.first is Loss) {
                return -1
            }

            val o1Tuple = o1.first as Tuple
            val o2Tuple = o2.first as Tuple
            return TupleComparator.compare(Pair(o1Tuple, o1.second), Pair(o2Tuple, o2.second))
        }
    }
}

class TupleComparator {
    companion object : Comparator<Pair<Tuple, Int>>  {
        override fun compare(o1: Pair<Tuple, Int>, o2: Pair<Tuple, Int>): Int {
            if (o1.first.length != o2.first.length) {
                throw Exception("Cannot compare Tuples of unequal size")
            }

            if (o1.second != o2.second) {
                throw Exception("Cannot compare Tuples up to unequal length")
            }

            val differIndex = (0..o1.second).firstOrNull { i -> o1.first.m[i] != o2.first.m[i]} ?: return 0

            return 10.0.pow(differIndex).toInt() * (o1.first.m[differIndex] - o2.first.m[differIndex])
        }
    }
}

class Tuple(var m : IntArray) : Measure(m.size) {
    constructor(length : Int) : this(IntArray(length) { 0 })

    fun incrementUpTo(i : Int, max : Tuple, current : Tuple) : Measure {
        if (i == 0 && this.m[0] == max.m[0]) {
            return Loss
        }

        if (current.m[i] + 1 > max.m[i]) {
            current.incrementUpTo(i - 1, max)
        } else {
            current.m[i] == current.m[i] + 1
        }

        return current
    }
}

object Loss : Measure(0)