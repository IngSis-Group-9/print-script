package interpreter.interpreters

import ast.Method
import interpreter.VariableManager
import interpreter.response.ErrorResponse
import interpreter.response.InterpreterResponse
import interpreter.response.SuccessResponse

class MethodInterpreter(private val valueInterpreter: ValueInterpreter) : Interpreter<Method> {
    override fun interpret(
        astNode: Method,
        variableManager: VariableManager,
    ): InterpreterResponse {
        return when (astNode.identifier.lowercase()) {
            "println" -> {
                try {
                    val value = valueInterpreter.interpret(astNode.value, variableManager).value
                    SuccessResponse("$value\n")
                } catch (e: Exception) {
                    ErrorResponse(e.message ?: "Error while printing")
                }
            }
            else -> ErrorResponse("Unsupported method: ${astNode.identifier}")
        }
    }
}
