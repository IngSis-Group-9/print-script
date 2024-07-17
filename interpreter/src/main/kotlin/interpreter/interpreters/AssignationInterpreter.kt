package interpreter.interpreters

import ast.Assignation
import ast.DeclarationAssignation
import ast.SimpleAssignation
import interpreter.VariableManager
import interpreter.response.ErrorResponse
import interpreter.response.InterpreterResponse
import interpreter.response.SuccessResponse

class AssignationInterpreter(private val valueInterpreter: ValueInterpreter) : Interpreter<Assignation> {
    override fun interpret(
        astNode: Assignation,
        variableManager: VariableManager,
    ): InterpreterResponse {
        return when (astNode) {
            // Differentiate between declaration with assignation and a simple assignation
            is DeclarationAssignation -> {
                try {
                    val value = valueInterpreter.interpret(astNode.value, variableManager)
                    variableManager.addVariable(astNode.declaration.identifier, astNode.declaration.type, astNode.isConst, value)
                    SuccessResponse(null)
                } catch (e: Exception) {
                    ErrorResponse(e.message ?: "Error while declaring variable ${astNode.declaration.identifier}")
                }
            }
            is SimpleAssignation -> {
                try {
                    val value = valueInterpreter.interpret(astNode.value, variableManager)
                    variableManager.setVariable(astNode.identifier, value)
                    SuccessResponse(null)
                } catch (e: Exception) {
                    ErrorResponse(e.message ?: "Error while setting variable ${astNode.identifier}")
                }
            }
        }
    }
}
