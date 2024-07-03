package interpreter

import ast.ASTNode
import ast.Assignation
import ast.Conditional
import ast.Declaration
import ast.DeclarationAssignation
import ast.Method
import ast.SimpleAssignation
import interpreter.interpreters.AssignationInterpreter
import interpreter.interpreters.ConditionalInterpreter
import interpreter.interpreters.DeclarationInterpreter
import interpreter.interpreters.Interpreter
import interpreter.interpreters.MethodInterpreter
import interpreter.response.ErrorResponse
import interpreter.response.InterpreterResponse
import interpreter.response.SuccessResponse

class ExecuteInterpreter(private val interpreters: Map<Class<out ASTNode>, Interpreter<out ASTNode>>, private val variableManager: VariableManager) {
    constructor(interpreters: Map<Class<out ASTNode>, Interpreter<out ASTNode>>) : this(interpreters, VariableManager())

    @Suppress("UNCHECKED_CAST")
    fun interpretAST(astList: List<ASTNode>): InterpreterResponse {
        val stringBuffer = StringBuffer()
        for (ast in astList) {
            try {
                val interpreter = interpreters[ast::class.java] as Interpreter<ASTNode>

                when (val response = interpreter.interpret(ast, variableManager)) {
                    is SuccessResponse -> {
                        response.message?.let { stringBuffer.append(it) }
                        continue
                    }
                    is ErrorResponse -> {
                        return response
                    }
                }
            } catch (e: Exception) {
                return ErrorResponse("Invalid type of ASTNode: ${ast::class.java.simpleName}")
            }
        }
        return SuccessResponse(stringBuffer.takeIf { it.isNotEmpty() }?.toString())
    }

    fun getVariableManager(): VariableManager {
        return variableManager
    }

    companion object {
        fun getDefaultInterpreter(variableManager: VariableManager): ExecuteInterpreter {
            val visitors =
                mapOf(
                    Declaration::class.java to DeclarationInterpreter(),
                    Assignation::class.java to AssignationInterpreter(),
                    DeclarationAssignation::class.java to AssignationInterpreter(),
                    SimpleAssignation::class.java to AssignationInterpreter(),
                    Method::class.java to MethodInterpreter(),
                    Conditional::class.java to ConditionalInterpreter(),
                )
            return ExecuteInterpreter(visitors, variableManager)
        }

        fun getInterpreterByVersion(version: String): ExecuteInterpreter {
            var visitors: Map<Class<out ASTNode>, Interpreter<out ASTNode>> = mapOf()
            when (version) {
                "1.0" -> {
                    visitors =
                        mapOf(
                            Declaration::class.java to DeclarationInterpreter(),
                            Assignation::class.java to AssignationInterpreter(),
                            DeclarationAssignation::class.java to AssignationInterpreter(),
                            SimpleAssignation::class.java to AssignationInterpreter(),
                            Method::class.java to MethodInterpreter(),
                        )
                }
                "1.1" -> {
                    visitors =
                        mapOf(
                            Declaration::class.java to DeclarationInterpreter(),
                            Assignation::class.java to AssignationInterpreter(),
                            DeclarationAssignation::class.java to AssignationInterpreter(),
                            SimpleAssignation::class.java to AssignationInterpreter(),
                            Method::class.java to MethodInterpreter(),
                            Conditional::class.java to ConditionalInterpreter(),
                        )
                }
            }
            return ExecuteInterpreter(visitors)
        }
    }
}
