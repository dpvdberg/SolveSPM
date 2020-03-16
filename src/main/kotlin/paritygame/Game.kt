package paritygame

class Game(
    var nodes: List<Node>,
    var id: Int? = null
) {
    private var max : Int = nodes.map{ n -> n.priority}.min()!!

    fun maxPrio(): Int {
        return max
    }
}