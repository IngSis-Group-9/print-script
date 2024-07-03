package formatter.formatters

import ast.ASTNode
import ast.Declaration
import formatter.FormatRules

class DeclarationFormatter : Formatter {
    override fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String {
        val declaration = astNode as Declaration
        return buildString {
            append("let ")
            append(declaration.identifier)
            append(
                if (rules.spaceBeforeColon && rules.spaceAfterColon) {
                    " : " // Space before and after colon
                } else if (rules.spaceAfterColon) {
                    ": " // Space after colon
                } else if (rules.spaceBeforeColon) {
                    " :" // Space before colon
                } else {
                    ":"
                },
            )
            append(declaration.type)
            append(";\n")
        }
    }
}
