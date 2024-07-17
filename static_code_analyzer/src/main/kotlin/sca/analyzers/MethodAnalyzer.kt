package sca.analyzers

import ast.ASTNode
import ast.BinaryOperation
import ast.IdentifierOperator
import ast.Method
import ast.NumberOperator
import ast.StringOperator
import ast.ValueNode
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue
import token.Position

class MethodAnalyzer : StaticCodeAnalyzer {
    override fun analyzeNode(
        astNode: ASTNode,
        rules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue> {
        val methodNode = astNode as Method
        val issues = mutableListOf<StaticCodeIssue>()
        val lineIndex = 1
        var columnIndex = 1

        if (methodNode.identifier == "println") {
            val argument = extractArgument(methodNode.value)
            if (!checkMethodArgument(argument, rules.functionArgumentCheck)) {
                columnIndex++
                issues.add(StaticCodeIssue("The println function should be called only with an identifier or a literal, the expression: $argument is invalid.", Position(lineIndex, columnIndex)))
            }
        }
        return issues
    }

    private fun extractArgument(value: ValueNode): String {
        return when (value) {
            is IdentifierOperator -> value.identifier
            is StringOperator -> value.value
            is NumberOperator -> value.value.toString()
            is BinaryOperation -> {
                val left = extractArgument(value.left)
                val right = extractArgument(value.right)
                "$left ${value.symbol} $right"
            }
            else -> ""
        }
    }

    private fun checkMethodArgument(
        argument: String,
        rule: Boolean,
    ): Boolean {
        // Verificar si el argumento es un identificador o un literal (número o string)
        if (!rule) return true // Si las reglas están desactivadas, siempre retorna true
        return argument.matches("""^[\w\d]+$""".toRegex())
    }
}
