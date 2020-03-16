package evaluation

import javax.xml.soap.Node

sealed class Measure {
    fun smallerUpTo(n: Measure, i: Int): Boolean {
        return false
    }

    //TODO: implement, <=
    fun smallerEqualUpTo(n: Measure, i: Int): Boolean {
        return false
    }


    operator fun compareTo(pair: Pair<Measure, Int>): Int {
        return -1
    }

    operator fun compareTo(other: Measure): Int {
        return -1
    }
}

class MeasureComparator : Comparator<Measure> {
    override fun compare(o1: Measure, o2: Measure): Int {
        if (o1 is Loss) {
            return if (o2 is Loss) {
                0
            } else {
                1
            }
        } else if (o2 is Loss) {
            return -1
        }

        val o1Tuple = o1 as Tuple
        val o2Tuple = o2 as Tuple
        return o1Tuple.compareTo(o2Tuple)
    }
}

class TupleComparator : Comparator<Tuple> {
    override fun compare(o1: Tuple, o2: Tuple): Int {
        return -1
    }
}

class Tuple(length: Int) : Measure() {
    var m = IntArray(length) { 0 }
}

class Loss : Measure()