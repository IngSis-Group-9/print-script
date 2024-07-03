import lexer.Lexer
import lexer.TokenProvider
import org.junit.jupiter.api.Test
import token.Token
import token.TokenType
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TokenProviderTest {
    @Test
    fun `test 001 - simple statement`() {
        val tokenProvider = TokenProvider(FileInputStream(File("src/test/resources/test_01.txt")), Lexer.getDefaultLexer())
        val tokens = tokenProvider.readStatement()
        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(input), COLON, STRING_TYPE, EQ, STRING(Hello World), SEMICOLON, NEW_LINE]"

        assertEquals(expectedTokensString, listToString(tokens))
    }

    @Test
    fun `test 002 - conditional statement with else`() {
        val tokenProvider = TokenProvider(FileInputStream(File("src/test/resources/test_02.txt")), Lexer.getDefaultLexer())
        val tokens = tokenProvider.readStatement()
        val expectedTokensString = "[IF_KEYWORD, LPAREN, BOOLEAN(true), RPAREN, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(true), RPAREN, SEMICOLON, NEW_LINE, RBRACE, ELSE_KEYWORD, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(false), RPAREN, SEMICOLON, NEW_LINE, RBRACE, NEW_LINE]"

        assertEquals(expectedTokensString, listToString(tokens))
    }

    @Test
    fun `test 003 - conditional statement without else`() {
        val tokenProvider = TokenProvider(FileInputStream(File("src/test/resources/test_03.txt")), Lexer.getDefaultLexer())
        val tokens = tokenProvider.readStatement()
        val expectedTokensString = "[IF_KEYWORD, LPAREN, BOOLEAN(true), RPAREN, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(true), RPAREN, SEMICOLON, NEW_LINE, RBRACE, NEW_LINE]"

        assertEquals(expectedTokensString, listToString(tokens))
    }

    @Test
    fun `test 004 - separate correctly each statement`() {
        val tokenProvider = TokenProvider(FileInputStream(File("src/test/resources/test_04.txt")), Lexer.getDefaultLexer())
        val expectedTokens =
            listOf(
                "[LET_KEYWORD, IDENTIFIER(a), COLON, STRING_TYPE, EQ, STRING(hello), SEMICOLON, NEW_LINE]",
                "[IF_KEYWORD, LPAREN, BOOLEAN(true), RPAREN, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(true), RPAREN, SEMICOLON, NEW_LINE, RBRACE, NEW_LINE]",
                "[PRINTLN_FUNCTION, LPAREN, IDENTIFIER(a), RPAREN, SEMICOLON, NEW_LINE]",
            )
        val actualTokens: MutableList<String> = mutableListOf()
        while (tokenProvider.hasNextStatement()) {
            actualTokens.add(listToString(tokenProvider.readStatement()))
        }
        assertTrue { expectedTokens.size == actualTokens.size }
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun `test 006 - else statement from string`() {
        val inputString =
            """
            if (true) {
                println("Outer true branch");
                if (false) {
                    println("Inner true branch");
                } else {
                    println("Inner false branch");
                }
            } else {
                println("Outer false branch");
            }
            """.trimIndent()

        val tokenProvider = TokenProvider(ByteArrayInputStream(inputString.toByteArray()), Lexer.getDefaultLexer())
        val tokens = tokenProvider.readStatement()
        val expectedTokensString = "[IF_KEYWORD, LPAREN, BOOLEAN(true), RPAREN, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(Outer true branch), RPAREN, SEMICOLON, NEW_LINE, IF_KEYWORD, LPAREN, BOOLEAN(false), RPAREN, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(Inner true branch), RPAREN, SEMICOLON, NEW_LINE, RBRACE, ELSE_KEYWORD, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(Inner false branch), RPAREN, SEMICOLON, NEW_LINE, RBRACE, NEW_LINE, RBRACE, ELSE_KEYWORD, LBRACE, NEW_LINE, PRINTLN_FUNCTION, LPAREN, STRING(Outer false branch), RPAREN, SEMICOLON, NEW_LINE, RBRACE, NEW_LINE]"

        assertEquals(expectedTokensString, listToString(tokens))
    }

    private fun listToString(tokens: List<Token>): String {
        return tokens.map {
            when (it.type) {
                TokenType.IDENTIFIER, TokenType.NUMBER, TokenType.STRING, TokenType.BOOLEAN -> "${it.type.name}(${it.value})"
                else -> it.type.name
            }
        }.toString()
    }
}
