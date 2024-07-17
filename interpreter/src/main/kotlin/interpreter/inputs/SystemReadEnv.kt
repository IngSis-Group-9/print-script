package interpreter.inputs

class SystemReadEnv : ReadEnvSource {
    override fun readEnv(name: String): String? {
        return System.getenv(name)
    }
}
