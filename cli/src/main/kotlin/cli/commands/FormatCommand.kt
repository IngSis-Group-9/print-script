package cli.commands

import formatter.ExecuteFormatter
import formatter.FormatRules
import lexer.Lexer
import lexer.TokenProvider
import parser.Parser
import java.io.File
import java.io.FileInputStream

class FormatCommand(private val file: File, private val configFilePath: String, private val lexer: Lexer, private val parser: Parser, private val formatter: ExecuteFormatter) :
    Command {
    override fun execute() {
        println("Formatting...")

        val tokenProvider = TokenProvider(FileInputStream(file), lexer)
        val astList = fillAstListWithProgress(file, parser, tokenProvider)

        var formattedAst = ""
        for (ast in astList) {
            formattedAst += formatter.formatNode(ast, FormatRules(configFilePath))
        }

        println(formattedAst)

        file.writeText(formattedAst)

        println("File formatted successfully")
    }
}
