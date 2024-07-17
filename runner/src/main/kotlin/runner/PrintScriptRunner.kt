package runner

import ast.ASTNode
import formatter.ExecuteFormatter
import formatter.FormatRules
import interpreter.ExecuteInterpreter
import interpreter.inputs.ReadEnvSource
import interpreter.inputs.ReadInputSource
import interpreter.response.InterpreterResponse
import lexer.Lexer
import lexer.TokenProvider
import parser.Parser
import sca.ExecuteSca
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue
import java.io.InputStream

class PrintScriptRunner {
    fun interpretSnippet(
        snippet: InputStream,
        version: PrintScriptVersion,
        readInputSource: ReadInputSource? = null,
        readEnvSource: ReadEnvSource? = null,
    ): InterpreterResponse {
        val interpreter = ExecuteInterpreter.getInterpreterByVersion(version.value, readInputSource, readEnvSource)
        return interpreter.interpretAST(getASTList(snippet, version))
    }

    fun formatSnippet(
        snippet: InputStream,
        version: PrintScriptVersion,
        formatRules: FormatRules,
    ): String {
        val formatter = ExecuteFormatter.getFormatterByVersion(version.value)
        val astList = getASTList(snippet, version)
        return astList.joinToString { formatter.formatNode(it, formatRules) }
    }

    fun analyzeSnippet(
        snippet: InputStream,
        version: PrintScriptVersion,
        staticCodeAnalyzerRules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue> {
        val sca = ExecuteSca.getSCAByVersion(version.value)
        val astList = getASTList(snippet, version)
        return astList.flatMap { sca.analyzeNode(it, staticCodeAnalyzerRules) }
    }

    private fun getASTList(
        snippet: InputStream,
        version: PrintScriptVersion,
    ): List<ASTNode> {
        val lexer = Lexer.getLexerByVersion(version.value)
        val tokenProvider = TokenProvider(snippet, lexer)
        val parser = Parser.getParserByVersion(version.value)

        val astList = mutableListOf<ASTNode>()
        while (tokenProvider.hasNextStatement()) {
            val statement =
                runCatching { tokenProvider.readStatement() }
                    .getOrElse {
                        throw Exception("Error while lexing statement: ${it.message}", it)
                    }

            val ast =
                runCatching { parser.generateAST(statement) }
                    .getOrElse { throw Exception("Error while parsing snippet: ${it.message}", it) }

            ast?.let { astList.add(it) } ?: throw Exception("Invalid statement for parser")
        }
        return astList
    }
}

enum class PrintScriptVersion(val value: String) {
    V1("1.0"),
    V2("1.1"),
}
