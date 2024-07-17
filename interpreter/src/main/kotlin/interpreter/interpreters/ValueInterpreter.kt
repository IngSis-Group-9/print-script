package interpreter.interpreters

import ast.BinaryOperation
import ast.BooleanOperator
import ast.IdentifierOperator
import ast.Method
import ast.NumberOperator
import ast.StringOperator
import ast.ValueNode
import interpreter.VariableManager
import interpreter.inputs.ReadEnvSource
import interpreter.inputs.ReadInputSource
import interpreter.response.VariableResponse

class ValueInterpreter(private val readInputSource: ReadInputSource, private val readEnvSource: ReadEnvSource) : Interpreter<ValueNode> {
    override fun interpret(
        astNode: ValueNode,
        variableManager: VariableManager,
    ): VariableResponse {
        return when (astNode) {
            is StringOperator -> VariableResponse("String", astNode.value)
            is NumberOperator -> VariableResponse("Number", astNode.value.toString())
            is BooleanOperator -> VariableResponse("Boolean", astNode.value)
            is IdentifierOperator -> {
                val variable = variableManager.getVariableWithValue(astNode.identifier)
                VariableResponse(variable.first.type, variable.second)
            }
            is Method -> {
                when (astNode.identifier.lowercase()) {
                    "readenv" -> {
                        val argument =
                            (astNode.value as? StringOperator)?.value
                                ?: throw IllegalArgumentException("readEnv requires a string argument")
                        val value = readEnvSource.readEnv(argument) ?: throw IllegalArgumentException("Environment variable $argument does not exist")
                        val type =
                            when {
                                value.toBooleanStrictOrNull() != null -> "Boolean"
                                value.toDoubleOrNull() != null -> "Number"
                                else -> "String"
                            }
                        VariableResponse(type, value)
                    }
                    "readinput" -> {
                        val argument =
                            (astNode.value as? StringOperator)?.value
                                ?: throw IllegalArgumentException("readInput requires a string argument")
                        val value = readInputSource.readInput(argument) ?: throw IllegalArgumentException("Failed to read input")
                        val type =
                            when {
                                value.toBooleanStrictOrNull() != null -> "Boolean"
                                value.toDoubleOrNull() != null -> "Number"
                                else -> "String"
                            }
                        VariableResponse(type, value)
                    }
                    else -> throw IllegalArgumentException("Unsupported method: ${astNode.identifier}")
                }
            }
            is BinaryOperation -> {
                val left = interpret(astNode.left, variableManager).value
                val right = interpret(astNode.right, variableManager).value

                when (astNode.symbol) {
                    "+" -> {
                        return if (left.toDoubleOrNull() != null && right.toDoubleOrNull() != null) {
                            val numberValue = left.toDouble() + right.toDouble()
                            if (numberValue % 1 == 0.0) {
                                VariableResponse("Number", numberValue.toInt().toString())
                            } else {
                                VariableResponse("Number", numberValue.toString())
                            }
                        } else {
                            // Si al menos uno es String, los concatena
                            VariableResponse("String", "$left$right")
                        }
                    }

                    "-" -> {
                        return if (left.toDoubleOrNull() != null && right.toDoubleOrNull() != null) {
                            val numberValue = left.toDouble() - right.toDouble()
                            if (numberValue % 1 == 0.0) {
                                VariableResponse("Number", numberValue.toInt().toString())
                            } else {
                                VariableResponse("Number", numberValue.toString())
                            }
                        } else {
                            throw IllegalArgumentException("Unsupported operation: '-' with non-numeric operands")
                        }
                    }

                    "*" -> {
                        return if (left.toDoubleOrNull() != null && right.toDoubleOrNull() != null) {
                            val numberValue = left.toDouble() * right.toDouble()
                            if (numberValue % 1 == 0.0) {
                                VariableResponse("Number", numberValue.toInt().toString())
                            } else {
                                VariableResponse("Number", numberValue.toString())
                            }
                        } else {
                            throw IllegalArgumentException("Unsupported operation: '*' with non-numeric operands")
                        }
                    }

                    "/" -> {
                        if (left.toDoubleOrNull() != null && right.toDoubleOrNull() != null) {
                            val numberValue = left.toDouble() / right.toDouble()
                            if (numberValue % 1 == 0.0) {
                                VariableResponse("Number", numberValue.toInt().toString())
                            } else {
                                VariableResponse("Number", numberValue.toString())
                            }
                        } else {
                            throw IllegalArgumentException("Unsupported operation: '/' with non-numeric operands")
                        }
                    }
                    else -> throw Exception("${astNode.symbol} is not a valid operation")
                }
            }
            else -> throw Exception("Unexpected binary operation node")
        }
    }
}
