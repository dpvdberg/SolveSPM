package paritygame

class Node(
    val id: Int,
    val priority : Int,
    val owner: Player,
    var successors: List<Node>,
    var predecessors: List<Node>,
    val successorsIds : List<Int>,
    val name: String? = null
) : Comparable<Node> {

    fun isEven() : Boolean {
        return this.priority % 2 == 0
    }

    override fun toString(): String {
        return "id: $id ${name ?: ""} \t\t owner: $owner \t prio: $priority \t successors: $successorsIds"
    }

    override fun compareTo(other: Node): Int {
        return compareValues(this.id, other.id)
    }
}