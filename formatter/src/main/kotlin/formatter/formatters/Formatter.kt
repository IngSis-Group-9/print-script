package formatter.formatters

import ast.ASTNode
import formatter.FormatRules

interface Formatter {
    fun formatNode(
        astNode: ASTNode,
        rules: FormatRules,
    ): String
}
