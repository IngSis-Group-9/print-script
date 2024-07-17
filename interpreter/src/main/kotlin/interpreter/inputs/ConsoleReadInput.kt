package interpreter.inputs

class ConsoleReadInput : ReadInputSource {
    override fun readInput(string: String): String? {
        print(string)
        return readlnOrNull()
    }
}
