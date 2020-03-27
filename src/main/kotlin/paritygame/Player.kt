package paritygame

sealed class Player

object Diamond : Player() {
    override fun toString(): String {
        return "<>"
    }
}
object Box : Player() {
    override fun toString(): String {
        return "[]"
    }
}