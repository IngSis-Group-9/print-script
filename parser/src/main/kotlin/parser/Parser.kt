import ast.ASTNode
import builder.ConditionalASTBuilder
import parser.builder.ASTBuilder
import parser.builder.AssignationASTBuilder
import parser.builder.DeclarationASTBuilder
import parser.builder.MethodASTBuilder
import token.Token

class Parser(private val astBuilders: List<ASTBuilder<ASTNode>>) {
    fun generateAST(tokens: List<Token>): ASTNode? {
        if (tokens.isNotEmpty()) {
            for (astBuilder in astBuilders) {
                if (astBuilder.verify((tokens))) {
                    return astBuilder.build((tokens))
                }
            }
        }
        return null
    }

    companion object {
        fun getDefaultParser(): Parser {
            return getParserByVersion("1.1")
        }

        fun getParserByVersion(version: String): Parser {
            return Parser(
                listOf(
                    DeclarationASTBuilder(version),
                    AssignationASTBuilder(version),
                    MethodASTBuilder(version),
                    ConditionalASTBuilder(version),
                ),
            )
        }
    }
}
