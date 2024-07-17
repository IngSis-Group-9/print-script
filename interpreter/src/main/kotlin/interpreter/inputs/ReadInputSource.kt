package interpreter.inputs

fun interface ReadInputSource {
    fun readInput(string: String): String?
}
