package sca.analyzers

import ast.ASTNode
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue

class SimpleAssignationAnalyzer : StaticCodeAnalyzer {
    override fun analyzeNode(
        astNode: ASTNode,
        rules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue> {
        return emptyList()
    }
}
