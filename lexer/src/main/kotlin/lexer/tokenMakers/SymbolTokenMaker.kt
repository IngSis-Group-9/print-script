package lexer.tokenMakers

import token.Position
import token.Token
import token.TokenType

class SymbolTokenMaker(private val version: String) : TokenMaker {
    private val versionSymbols =
        mapOf(
            "1.0" to setOf('(', ')', '=', ':', '+', '-', '*', '/', ';'),
            "1.1" to setOf('(', ')', '=', ':', '+', '-', '*', '/', ';', '{', '}'),
        )

    override fun makeToken(
        input: String,
        position: Int,
        positionX: Int,
        positionY: Int,
    ): Token? {
        val symbol = input[position]

        val supportedSymbols = versionSymbols[version] ?: throw Exception("Unsupported version: $version")

        // Check if the selected version has the symbol
        return if (symbol in supportedSymbols) {
            return when (symbol) {
                '(' -> Token(TokenType.LPAREN, "(", Position(positionX, positionY), Position(positionX + 1, positionY))
                ')' -> Token(TokenType.RPAREN, ")", Position(positionX, positionY), Position(positionX + 1, positionY))
                '=' -> Token(TokenType.EQ, "=", Position(positionX, positionY), Position(positionX + 1, positionY))
                ':' -> Token(TokenType.COLON, ":", Position(positionX, positionY), Position(positionX + 1, positionY))
                '+' -> Token(TokenType.PLUS, "+", Position(positionX, positionY), Position(positionX + 1, positionY))
                '-' -> Token(TokenType.MINUS, "-", Position(positionX, positionY), Position(positionX + 1, positionY))
                '*' -> Token(TokenType.TIMES, "*", Position(positionX, positionY), Position(positionX + 1, positionY))
                '/' -> Token(TokenType.DIV, "/", Position(positionX, positionY), Position(positionX + 1, positionY))
                ';' -> Token(TokenType.SEMICOLON, ";", Position(positionX, positionY), Position(positionX + 1, positionY))
                '{' -> Token(TokenType.LBRACE, "{", Position(positionX, positionY), Position(positionX + 1, positionY))
                '}' -> Token(TokenType.RBRACE, "}", Position(positionX, positionY), Position(positionX + 1, positionY))
                else -> null
            }
        } else { // If the symbol is not in the list of symbols, it is an unrecognized character
            null
        }
    }
}
