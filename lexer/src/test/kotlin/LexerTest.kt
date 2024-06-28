import lexer.Lexer
import lexer.TokenProvider
import lexer.tokenMakers.NewLineTokenMaker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import token.Token
import token.TokenType
import java.io.ByteArrayInputStream
import kotlin.test.Test

class LexerTest {
    @Test
    fun `test 001 - should make a list of tokens`() {
        val example = "let a: number = 5 * 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, NUMBER_TYPE, EQ, NUMBER(5), TIMES, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 002 - should make a list of tokens`() {
        val example = "println(a);"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[PRINTLN_FUNCTION, LPAREN, IDENTIFIER(a), RPAREN, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 003 - should make a list of tokens`() {
        val example = "let x: number;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(x), COLON, NUMBER_TYPE, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 004 - should make a list of tokens`() {
        val example = "x = 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[IDENTIFIER(x), EQ, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 005 - should make a list of tokens`() {
        val example = "let y: string = 'Hello';"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(y), COLON, STRING_TYPE, EQ, STRING(Hello), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 006 - should make a list of tokens`() {
        val example = "let z: number = 5.5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(z), COLON, NUMBER_TYPE, EQ, NUMBER(5.5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 007 - should make a list of tokens`() {
        val example = "let a: string = 'Hello' + ' world!';"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, STRING_TYPE, EQ, STRING(Hello), PLUS, STRING( world!), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 008 - should make a list of tokens`() {
        val example = "let b: string = 'Hello ' + 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(b), COLON, STRING_TYPE, EQ, STRING(Hello ), PLUS, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 009 - should make a list of tokens`() {
        val example = "println(168);"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[PRINTLN_FUNCTION, LPAREN, NUMBER(168), RPAREN, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 010 - should make a list of tokens`() {
        val example = "println(a);"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[PRINTLN_FUNCTION, LPAREN, IDENTIFIER(a), RPAREN, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 011 - should generate specific tokens`() {
        val example = "let x: number = 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(x), COLON, NUMBER_TYPE, EQ, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 012 - should make a list of tokens for addition`() {
        val example = "let sum: number = 5 + 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(sum), COLON, NUMBER_TYPE, EQ, NUMBER(5), PLUS, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 013 - should make a list of tokens for subtraction`() {
        val example = "let diff: number = 10 - 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(diff), COLON, NUMBER_TYPE, EQ, NUMBER(10), MINUS, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 014 - should make a list of tokens for multiplication`() {
        val example = "let prod: number = 5 * 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(prod), COLON, NUMBER_TYPE, EQ, NUMBER(5), TIMES, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 015 - should make a list of tokens for division`() {
        val example = "let quot: number = 10 / 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(quot), COLON, NUMBER_TYPE, EQ, NUMBER(10), DIV, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 016 - should make a list of tokens for addition with decimals`() {
        val example = "let sum: number = 5.5 + 5.5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(sum), COLON, NUMBER_TYPE, EQ, NUMBER(5.5), PLUS, NUMBER(5.5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 017 - should make a list of tokens for subtraction with decimals`() {
        val example = "let diff: number = 10.5 - 5.5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(diff), COLON, NUMBER_TYPE, EQ, NUMBER(10.5), MINUS, NUMBER(5.5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 018 - should make a list of tokens for multiplication with decimals`() {
        val example = "let prod: number = 5.5 * 5.5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(prod), COLON, NUMBER_TYPE, EQ, NUMBER(5.5), TIMES, NUMBER(5.5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 019 - should make a list of tokens for division with decimals`() {
        val example = "let quot: number = 10.5 / 5.5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(quot), COLON, NUMBER_TYPE, EQ, NUMBER(10.5), DIV, NUMBER(5.5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 020 - should make a list of tokens for multiple operations`() {
        val example = "let result: number = 5 + 5 * 10 - 2 / 2;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(result), COLON, NUMBER_TYPE, EQ, NUMBER(5), PLUS, NUMBER(5), TIMES, NUMBER(10), MINUS, NUMBER(2), DIV, NUMBER(2), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 021 - should make a list of tokens for multiple operations with decimals`() {
        val example = "let result: number = 5.5 + 5.5 * 10.5 - 2.5 / 2.5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(result), COLON, NUMBER_TYPE, EQ, NUMBER(5.5), PLUS, NUMBER(5.5), TIMES, NUMBER(10.5), MINUS, NUMBER(2.5), DIV, NUMBER(2.5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 022 - should read a statement with NEW_LINE`() {
        val example = "let a: number = 5 * 5;\nlet b: number = 10 / 2;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, NUMBER_TYPE, EQ, NUMBER(5), TIMES, NUMBER(5), SEMICOLON, NEW_LINE, LET_KEYWORD, IDENTIFIER(b), COLON, NUMBER_TYPE, EQ, NUMBER(10), DIV, NUMBER(2), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 023 - should read a statement with empty string`() {
        val lexer = Lexer.getDefaultLexer()
        assertThrows(RuntimeException::class.java) {
            lexer.makeTokens("let a: number = 1.2.3;")
        }
    }

    @Test
    fun `test 024 - should read a statement with empty string`() {
        val example = "let a: string = '';"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, STRING_TYPE, EQ, STRING(), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 025 - should return null for non-newline character`() {
        val tokenMaker = NewLineTokenMaker()
        val result = tokenMaker.makeToken("a", 0, 0, 0)
        assertNull(result)
    }

    // lexer.TokenProvider tests
    @Test
    fun `test 026 - should read a statement`() {
        val example = "let a: number = 5 * 5;"
        val lexer = Lexer.getDefaultLexer()
        val tokenProvider = TokenProvider(example.byteInputStream(), lexer)
        val actualTokens = tokenProvider.readStatement()

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, NUMBER_TYPE, EQ, NUMBER(5), TIMES, NUMBER(5), SEMICOLON, NEW_LINE]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 027 - should not find semicolon in statement`() {
        val input = ByteArrayInputStream("print Hello World".toByteArray())
        val lexer = Lexer.getDefaultLexer()
        val tokenProvider = TokenProvider(input, lexer)
        tokenProvider.readStatement()
        assertFalse(tokenProvider.hasNextStatement())
    }

    @Test
    fun `test 028 - should find semicolon in statement`() {
        val input = ByteArrayInputStream("print Hello World;".toByteArray())
        val lexer = Lexer.getDefaultLexer()
        val tokenProvider = TokenProvider(input, lexer)
        tokenProvider.readStatement()
        assertFalse(tokenProvider.hasNextStatement())
    }

    @Test
    fun `test 029 - should read a statement with if else`() {
        val example = "if (true) { println('Hello'); }"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[IF_KEYWORD, LPAREN, BOOLEAN(true), RPAREN, LBRACE, PRINTLN_FUNCTION, LPAREN, STRING(Hello), RPAREN, SEMICOLON, RBRACE]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 030 - should read a statement with if else`() {
        val example = "if (true) { println('Hello'); }\nelse { println('World'); }"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[IF_KEYWORD, LPAREN, BOOLEAN(true), RPAREN, LBRACE, PRINTLN_FUNCTION, LPAREN, STRING(Hello), RPAREN, SEMICOLON, RBRACE, NEW_LINE, ELSE_KEYWORD, LBRACE, PRINTLN_FUNCTION, LPAREN, STRING(World), RPAREN, SEMICOLON, RBRACE]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 031 - should read a statement with boolean values`() {
        val example = "let a: boolean = true;\nlet b: boolean = false;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, BOOLEAN_TYPE, EQ, BOOLEAN(true), SEMICOLON, NEW_LINE, LET_KEYWORD, IDENTIFIER(b), COLON, BOOLEAN_TYPE, EQ, BOOLEAN(false), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 032 - should read a statement with const keyword`() {
        val example = "const a: number = 5;"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[CONST_KEYWORD, IDENTIFIER(a), COLON, NUMBER_TYPE, EQ, NUMBER(5), SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 033 - should read a statement with readInput function`() {
        val example = "let a: string = readInput();"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, STRING_TYPE, EQ, READINPUT_FUNCTION, LPAREN, RPAREN, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 034 - should read a statement with readEnv function`() {
        val example = "let a: string = readEnv();"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, STRING_TYPE, EQ, READENV_FUNCTION, LPAREN, RPAREN, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 035 - should read a statement with readInput function with argument`() {
        val example = "let a: string = readInput('Enter a value: ');"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, STRING_TYPE, EQ, READINPUT_FUNCTION, LPAREN, STRING(Enter a value: ), RPAREN, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
    }

    @Test
    fun `test 036 - should read a statement with readEnv function with argument`() {
        val example = "let a: string = readEnv('HOME');"
        val lexer = Lexer.getDefaultLexer()
        val actualTokens = lexer.makeTokens(example)

        val expectedTokensString = "[LET_KEYWORD, IDENTIFIER(a), COLON, STRING_TYPE, EQ, READENV_FUNCTION, LPAREN, STRING(HOME), RPAREN, SEMICOLON]"
        val actualTokensString = listToString(actualTokens)

        assertEquals(expectedTokensString, actualTokensString)
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
