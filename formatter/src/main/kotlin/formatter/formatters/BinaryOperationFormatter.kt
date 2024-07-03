package formatter.formatters

import ast.ASTNode
import ast.BinaryOperation
import ast.IdentifierOperator
import ast.NumberOperator
import ast.StringOperator
import ast.ValueNode
import formatter.FormatRules

class BinaryOperationFormatter : Formatter {
    override fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String {
        val valueNode = astNode as ValueNode
        return buildString {
            append(
                when (valueNode) {
                    is StringOperator -> "\"${valueNode.value}\""
                    is NumberOperator -> valueNode.value
                    is IdentifierOperator -> valueNode.identifier
                    is BinaryOperation -> "${formatNode(valueNode.left, rules)} ${valueNode.symbol} ${formatNode(valueNode.right, rules)}"
                    else -> ""
                },
            )
        }
    }
}
