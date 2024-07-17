package formatter.formatters

import ast.ASTNode
import ast.DeclarationAssignation
import formatter.FormatRules

class DeclarationAssignationFormatter : Formatter {
    override fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String {
        val declarationAssignation = astNode as DeclarationAssignation
        return buildString {
            append(
                if (declarationAssignation.isConst) {
                    "const "
                } else {
                    "let "
                },
            )
            append(DeclarationFormatter().formatNode(declarationAssignation.declaration, rules).drop(4).dropLast(2))
            append(
                if (rules.spaceAroundAssignment) {
                    " = "
                } else {
                    "="
                },
            )
            append(AssignationValueFormatter().formatNode(declarationAssignation.value, rules))
            append(";\n")
        }
    }
}
