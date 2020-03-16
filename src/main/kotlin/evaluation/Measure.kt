package evaluation

import javax.xml.soap.Node
import kotlin.math.abs
import kotlin.math.pow

sealed class Measure(var length: Int) {
    fun smallerUpTo(n: Measure, i: Int): Boolean {
        return false
    }

    //TODO: implement, <=
    fun smallerEqualUpTo(n: Measure, i: Int): Boolean {
        return false
    }


    operator fun compareTo(pair: Pair<Measure, Int>): Int {
        return MeasureUpToComparator.compare(Pair(this, pair.second), pair)
    }

    operator fun compareTo(other: Measure): Int {
        return MeasureUpToComparator.compare(Pair(this, length), Pair(other, other.length))
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

            val differIndex = (0 until o1.second).firstOrNull { i -> o1.first.m[i] != o2.first.m[i]} ?: return 0

            return 10.0.pow(differIndex).toInt() * (o1.first.m[differIndex] - o2.first.m[differIndex])
        }
    }
}

class Tuple(length : Int) : Measure(length) {
    var m = IntArray(length) { 0 }
}

object Loss : Measure(0)