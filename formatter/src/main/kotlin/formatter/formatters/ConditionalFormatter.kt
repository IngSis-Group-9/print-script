package formatter.formatters

import ast.ASTNode
import ast.BinaryOperation
import ast.Conditional
import ast.Declaration
import ast.DeclarationAssignation
import ast.Method
import ast.SimpleAssignation
import formatter.FormatRules

class ConditionalFormatter : Formatter {
    override fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String {
        val conditional = astNode as Conditional
        val indentSize = rules.nSpacesIndentationForIfStatement
        val tabs = "\t".repeat(indentSize)

        val formattedThen =
            conditional.then.joinToString("") {
                getFormatterForNode(it).formatNode(it, rules).replaceIndent(tabs)
            }
        val formattedOtherwise =
            conditional.otherwise?.joinToString("") {
                getFormatterForNode(it).formatNode(it, rules).replaceIndent(tabs)
            } ?: ""

        return buildString {
            append("if (")
            append(BinaryOperationFormatter().formatNode(conditional.condition, rules))
            append(") {\n")
            append(formattedThen)
            append("\n}")
            if (conditional.otherwise != null) {
                append(" else {\n")
                append(formattedOtherwise)
                append("\n}")
            }
            append("\n")
        }
    }

    fun getFormatterForNode(node: ASTNode): Formatter {
        return when (node) {
            is Declaration -> DeclarationFormatter()
            is DeclarationAssignation -> DeclarationAssignationFormatter()
            is SimpleAssignation -> SimpleAssignationFormatter()
            is Method -> MethodFormatter()
            is BinaryOperation -> BinaryOperationFormatter()
            is Conditional -> ConditionalFormatter()
            else -> throw IllegalArgumentException("No formatter found for node type ${node::class}")
        }
    }
}
