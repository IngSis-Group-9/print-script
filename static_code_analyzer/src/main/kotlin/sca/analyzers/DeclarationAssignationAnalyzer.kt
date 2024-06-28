package sca.analyzers

import ast.ASTNode
import ast.BinaryOperation
import ast.BooleanOperator
import ast.DeclarationAssignation
import ast.NumberOperator
import ast.StringOperator
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue
import token.Position

class DeclarationAssignationAnalyzer : StaticCodeAnalyzer {
    override fun analyzeNode(
        astNode: ASTNode,
        rules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue> {
        val declarationAssignation = astNode as DeclarationAssignation
        val issues = mutableListOf<StaticCodeIssue>()
        val lineIndex = 1
        var columnIndex = 1
        if (!checkTypeMatching(declarationAssignation, rules.typeMatchingCheck)) {
            columnIndex += 3
            issues.add(StaticCodeIssue("Variable declaration does not match the type of the assigned value", Position(lineIndex, columnIndex)))
        }
        if (!checkIdentifierFormat(declarationAssignation.declaration.identifier, rules.identifierNamingCheck)) {
            columnIndex += 1
            issues.add(StaticCodeIssue("The identifier '${declarationAssignation.declaration.identifier}' must be in lower camel case.", Position(lineIndex, columnIndex)))
        }
        return issues
    }

    private fun checkTypeMatching(
        node: ASTNode,
        rule: Boolean,
    ): Boolean {
        if (!rule) return true
        return when (node) {
            is DeclarationAssignation -> {
                when (node.declaration.type) {
                    "string" -> node.value is StringOperator
                    "number" -> node.value is NumberOperator || node.value is BinaryOperation
                    "boolean" -> node.value is BooleanOperator
                    else -> false
                }
            }
            else -> false
        }
    }

    private fun checkIdentifierFormat(
        identifier: String,
        rule: Boolean,
    ): Boolean {
        if (!rule) return true
        return identifier.matches("""^[a-z]+(?:[A-Z][a-z\d]*)*$""".toRegex())
    }
}
