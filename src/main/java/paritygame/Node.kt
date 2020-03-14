package paritygame

class Node(
    id: Int,
    priority : Int,
    owner: Player,
    successors: List<Node>,
    name: String? = null
)