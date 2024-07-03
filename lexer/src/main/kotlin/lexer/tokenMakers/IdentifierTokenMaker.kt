package lexer.tokenMakers

import token.Position
import token.Token
import token.TokenType

class IdentifierTokenMaker(private val version: String) : TokenMaker {
    private val versionKeywords =
        mapOf(
            "1.0" to setOf("let", "println", "number", "string"),
            "1.1" to setOf("let", "const", "println", "readInput", "readEnv", "number", "string", "boolean", "if", "else", "true", "false", "newIdentifier1", "newIdentifier2"),
        )

    override fun makeToken(
        input: String,
        position: Int,
        positionX: Int,
        positionY: Int,
    ): Token? {
        var identifier = ""
        var pos = position
        while (pos < input.length && input[pos].isLetter()) {
            identifier += input[pos]
            pos++
        }

        val supportedKeywords = versionKeywords[version] ?: throw Exception("Unsupported version: $version")

        // Check if the selected version has the keyword
        return if (identifier in supportedKeywords) {
            return when (identifier) {
                "let" -> Token(TokenType.LET_KEYWORD, "let", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "const" -> Token(TokenType.CONST_KEYWORD, "const", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "println" -> Token(TokenType.PRINTLN_FUNCTION, "println", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "readInput" -> Token(TokenType.READINPUT_FUNCTION, "readInput", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "readEnv" -> Token(TokenType.READENV_FUNCTION, "readEnv", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "number" -> Token(TokenType.NUMBER_TYPE, "number", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "string" -> Token(TokenType.STRING_TYPE, "string", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "boolean" -> Token(TokenType.BOOLEAN_TYPE, "boolean", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "if" -> Token(TokenType.IF_KEYWORD, "if", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "else" -> Token(TokenType.ELSE_KEYWORD, "else", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "true" -> Token(TokenType.BOOLEAN, "true", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                "false" -> Token(TokenType.BOOLEAN, "false", Position(positionX, positionY), Position(positionX + identifier.length, positionY))
                else -> null
            }
        } else { // If the identifier is not in the list of keywords, it is a regular identifier
            if (identifier.isNotEmpty()) Token(TokenType.IDENTIFIER, identifier, Position(positionX, positionY), Position(positionX + identifier.length, positionY)) else null
        }
    }
}
