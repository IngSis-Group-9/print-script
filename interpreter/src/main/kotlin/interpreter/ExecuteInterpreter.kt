package interpreter

import ast.ASTNode
import ast.Assignation
import ast.Conditional
import ast.Declaration
import ast.DeclarationAssignation
import ast.Method
import ast.SimpleAssignation
import interpreter.inputs.ConsoleReadInput
import interpreter.inputs.ReadEnvSource
import interpreter.inputs.ReadInputSource
import interpreter.inputs.SystemReadEnv
import interpreter.interpreters.AssignationInterpreter
import interpreter.interpreters.ConditionalInterpreter
import interpreter.interpreters.DeclarationInterpreter
import interpreter.interpreters.Interpreter
import interpreter.interpreters.MethodInterpreter
import interpreter.interpreters.ValueInterpreter
import interpreter.response.ErrorResponse
import interpreter.response.InterpreterResponse
import interpreter.response.SuccessResponse

class ExecuteInterpreter(private val interpreters: Map<Class<out ASTNode>, Interpreter<out ASTNode>>, private val variableManager: VariableManager) {
    constructor(interpreters: Map<Class<out ASTNode>, Interpreter<out ASTNode>>) : this(interpreters, VariableManager())

    fun <T : ASTNode> interpretAST(astList: List<T>): InterpreterResponse {
        val stringBuffer = StringBuffer()
        for (ast in astList) {
            val interpreter = interpreters[ast::class.java] as? Interpreter<T> ?: return ErrorResponse("Unsupported ASTNode: ${ast::class.java.simpleName}")

            when (val response = interpreter.interpret(ast, variableManager)) {
                is SuccessResponse -> {
                    response.message?.let { stringBuffer.append(it) }
                    continue
                }
                is ErrorResponse -> {
                    return response
                }
            }
        }
        return SuccessResponse(stringBuffer.takeIf { it.isNotEmpty() }?.toString())
    }

    fun getVariableManager(): VariableManager {
        return variableManager
    }

    companion object {
        fun getDefaultInterpreter(variableManager: VariableManager): ExecuteInterpreter {
            val valueInterpreter = ValueInterpreter(ConsoleReadInput(), SystemReadEnv())
            val visitors =
                mapOf(
                    Declaration::class.java to DeclarationInterpreter(),
                    Assignation::class.java to AssignationInterpreter(valueInterpreter),
                    DeclarationAssignation::class.java to AssignationInterpreter(valueInterpreter),
                    SimpleAssignation::class.java to AssignationInterpreter(valueInterpreter),
                    Method::class.java to MethodInterpreter(valueInterpreter),
                    Conditional::class.java to ConditionalInterpreter(valueInterpreter),
                )
            return ExecuteInterpreter(visitors, variableManager)
        }

        fun getInterpreterByVersion(
            version: String,
            readInputSource: ReadInputSource? = null,
            readEnvSource: ReadEnvSource? = null,
        ): ExecuteInterpreter {
            var visitors: Map<Class<out ASTNode>, Interpreter<out ASTNode>> = mapOf()
            when (version) {
                "1.0" -> {
                    val valueInterpreter = ValueInterpreter({ _ -> throw Exception("Invalid expression") }, { _ -> throw Exception("Invalid expression") })
                    visitors =
                        mapOf(
                            Declaration::class.java to DeclarationInterpreter(),
                            Assignation::class.java to AssignationInterpreter(valueInterpreter),
                            DeclarationAssignation::class.java to AssignationInterpreter(valueInterpreter),
                            SimpleAssignation::class.java to AssignationInterpreter(valueInterpreter),
                            Method::class.java to MethodInterpreter(valueInterpreter),
                        )
                }
                "1.1" -> {
                    val valueInterpreter =
                        ValueInterpreter(
                            readInputSource ?: ConsoleReadInput(),
                            readEnvSource ?: SystemReadEnv(),
                        )
                    visitors =
                        mapOf(
                            Declaration::class.java to DeclarationInterpreter(),
                            Assignation::class.java to AssignationInterpreter(valueInterpreter),
                            DeclarationAssignation::class.java to AssignationInterpreter(valueInterpreter),
                            SimpleAssignation::class.java to AssignationInterpreter(valueInterpreter),
                            Method::class.java to MethodInterpreter(valueInterpreter),
                            Conditional::class.java to ConditionalInterpreter(valueInterpreter),
                        )
                }
            }
            return ExecuteInterpreter(visitors)
        }
    }
}
