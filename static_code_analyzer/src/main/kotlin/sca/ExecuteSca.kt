package sca

import ast.ASTNode
import ast.Conditional
import ast.Declaration
import ast.DeclarationAssignation
import ast.Method
import ast.SimpleAssignation
import sca.analyzers.ConditionalAnalyzer
import sca.analyzers.DeclarationAssignationAnalyzer
import sca.analyzers.MethodAnalyzer
import sca.analyzers.SimpleAssignationAnalyzer
import sca.analyzers.StaticCodeAnalyzer
import kotlin.reflect.KClass

class ExecuteSca(private val scaList: Map<KClass<out ASTNode>, StaticCodeAnalyzer>) : StaticCodeAnalyzer {
    override fun analyzeNode(
        astNode: ASTNode,
        rules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue> {
        val issues = mutableListOf<StaticCodeIssue>()
        val analyzer = scaList[astNode::class]

        if (analyzer != null) {
            analyzer.analyzeNode(astNode, rules).forEach {
                issues.add(it)
            }
        } else {
            throw IllegalArgumentException("No analyzer for this node")
        }

        return issues
    }

    companion object {
        fun getDefaultSCA(): ExecuteSca {
            return getSCAByVersion("1.1")
        }

        fun getSCAByVersion(version: String): ExecuteSca {
            return ExecuteSca(
                mapOf(
                    DeclarationAssignation::class to DeclarationAssignationAnalyzer(),
                    Declaration::class to SimpleAssignationAnalyzer(),
                    SimpleAssignation::class to SimpleAssignationAnalyzer(),
                    Method::class to MethodAnalyzer(),
                    Conditional::class to ConditionalAnalyzer(),
                ),
            )
        }
    }
}
