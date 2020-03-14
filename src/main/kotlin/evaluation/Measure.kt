package evaluation

import javax.xml.soap.Node

sealed class Measure {
    //TODO: implement
    fun smallerUpTo(n : Measure) : Boolean {
        return false
    }

    //TODO: implement
    fun smallerEqualUpTo(n : Measure) : Boolean {
        return false
    }
}

class Tuple : Measure() {
    var m = mutableMapOf<Int, Int>().withDefault { 0 }
}

class Loss : Measure()