package formatter.formatters

import ast.ASTNode
import ast.SimpleAssignation
import formatter.FormatRules

class SimpleAssignationFormatter : Formatter {
    override fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String {
        val simpleAssignation = astNode as SimpleAssignation
        return buildString {
            append(simpleAssignation.identifier)
            append(
                if (rules.spaceAroundAssignment) {
                    " = " // Space before and after assignation
                } else {
                    "=" // No space around assignation
                },
            )
            append(AssignationValueFormatter().formatNode(simpleAssignation.value, rules))
            append(";\n")
        }
    }
}
