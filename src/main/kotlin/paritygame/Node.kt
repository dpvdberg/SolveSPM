package paritygame

class Node(
    var id: Int,
    var priority : Int,
    var owner: Player,
    var successors: List<Node>,
    var successorsIds : List<Int>,
    var name: String? = null
) {

    fun isEven() : Boolean {
        return this.priority % 2 == 0
    }
}