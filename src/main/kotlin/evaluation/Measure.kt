package evaluation

import javax.xml.soap.Node

sealed class Measure {
    fun smallerUpTo(n : Measure, i : Int) : Boolean {
        return false
    }

    //TODO: implement, <=
    fun smallerEqualUpTo(n : Measure, i : Int) : Boolean {
        return false
    }


    operator fun compareTo(pair : Pair<Measure, Int>) : Int {
        return -1
    }
}

class Tuple : Measure() {
    var m = mutableMapOf<Int, Int>().withDefault { 0 }
}

class Loss : Measure()