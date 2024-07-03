package formatter.formatters

import ast.ASTNode
import ast.BinaryOperation
import ast.IdentifierOperator
import ast.Method
import ast.NumberOperator
import ast.StringOperator
import formatter.FormatRules

class MethodFormatter : Formatter {
    override fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String {
        val method = astNode as Method
        val newlineBefore = "\n".repeat(rules.newlineBeforePrintln)
        return buildString {
            append(newlineBefore)
            append(method.identifier)
            append("(")
            append(
                when (val methodValue = method.value) {
                    is StringOperator -> "\"${methodValue.value}\""
                    is NumberOperator -> methodValue.value.toString()
                    is BinaryOperation -> BinaryOperationFormatter().formatNode(methodValue, rules)
                    is IdentifierOperator -> methodValue.identifier
                    else -> ""
                },
            )
            append(");\n")
        }
    }
}
