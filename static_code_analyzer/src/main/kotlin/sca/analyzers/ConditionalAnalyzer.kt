package sca.analyzers

import ast.ASTNode
import ast.Conditional
import ast.DeclarationAssignation
import ast.IdentifierOperator
import ast.Method
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue
import token.Position

class ConditionalAnalyzer : StaticCodeAnalyzer {
    private val declarationAssignation = DeclarationAssignationAnalyzer()
    private val methodAnalyzer = MethodAnalyzer()

    override fun analyzeNode(
        astNode: ASTNode,
        rules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue> {
        val conditionalNode = astNode as Conditional
        val issues = mutableListOf<StaticCodeIssue>()

        if (conditionalNode.condition !is IdentifierOperator) {
            issues.add(StaticCodeIssue("The condition must be an identifier", Position(1, 1)))
        }

        conditionalNode.then.forEach {
            if (it is DeclarationAssignation) {
                issues.addAll(declarationAssignation.analyzeNode(it, rules))
            } else if (it is Method) {
                issues.addAll(methodAnalyzer.analyzeNode(it, rules))
            }
        }

        conditionalNode.otherwise?.let {
            it.forEach { node ->
                if (node is DeclarationAssignation) {
                    issues.addAll(declarationAssignation.analyzeNode(node, rules))
                } else if (node is Method) {
                    issues.addAll(methodAnalyzer.analyzeNode(node, rules))
                }
            }
        }

        return issues
    }
}
