package interpreter.inputs

fun interface ReadEnvSource {
    fun readEnv(name: String): String?
}
