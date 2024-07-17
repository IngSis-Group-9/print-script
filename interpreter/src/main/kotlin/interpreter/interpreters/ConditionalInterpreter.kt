package interpreter.interpreters

import ast.ASTNode
import ast.Conditional
import interpreter.ExecuteInterpreter
import interpreter.VariableManager
import interpreter.response.ErrorResponse
import interpreter.response.InterpreterResponse
import interpreter.response.SuccessResponse

class ConditionalInterpreter(private val valueInterpreter: ValueInterpreter) : Interpreter<Conditional> {
    override fun interpret(
        astNode: Conditional,
        variableManager: VariableManager,
    ): InterpreterResponse {
        try {
            val condition = valueInterpreter.interpret(astNode.condition, variableManager)
            if (condition.value.toBooleanStrict()) {
                variableManager.addScope()
                val response = interpretAST(astNode.then, variableManager)
                variableManager.removeScope()
                return response
            } else {
                astNode.otherwise?.let {
                    variableManager.addScope()
                    val response = interpretAST(it, variableManager)
                    variableManager.removeScope()
                    return response
                }
                return SuccessResponse(null)
            }
        } catch (e: Exception) {
            return ErrorResponse(e.message ?: "Error while interpreting condition")
        }
    }

    private fun interpretAST(
        astList: List<ASTNode>,
        variableManager: VariableManager,
    ): InterpreterResponse {
        val interpreter = ExecuteInterpreter.getDefaultInterpreter(variableManager)
        return interpreter.interpretAST(astList)
    }
}
