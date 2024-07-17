package formatter.formatters

import ast.ASTNode
import ast.BinaryOperation
import ast.BooleanOperator
import ast.IdentifierOperator
import ast.NumberOperator
import ast.StringOperator
import formatter.FormatRules

class AssignationValueFormatter : Formatter {
    override fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String {
        return when (astNode) {
            is StringOperator -> "\"${astNode.value}\""
            is NumberOperator -> "${astNode.value}"
            is IdentifierOperator -> astNode.identifier
            is BooleanOperator -> astNode.value
            is BinaryOperation -> BinaryOperationFormatter().formatNode(astNode, rules)
            else -> ""
        }
    }
}
