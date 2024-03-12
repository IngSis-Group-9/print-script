class Lexer (
    private val input: String,
    private var position: Int = 0,
    private var positionX: Int = 1,
    private var positionY: Int = 1,
    private var tokenList: List<Token> = listOf(),
) {
    fun makeTokens(): List<Token> {
        while (position < input.length) {
            val currentChar = input[position]
            when {
                currentChar.isDigit() -> {
                    tokenList += makeNumber()
                }
                currentChar.isLetter() -> {
                    tokenList += makeIdentifier()
                }
                currentChar == '(' -> {
                    tokenList += Token(TokenType.LPAREN, "(", Position(positionX, positionY), Position(positionX+1, positionY))
                    position++
                    positionX++
                }
                currentChar == ')' -> {
                    tokenList += Token(TokenType.RPAREN, ")", Position(positionX, positionY), Position(positionX+1, positionY))
                    position++
                    positionX++
                }
                currentChar == '=' -> {
                    tokenList += Token(TokenType.EQ, "=", Position(positionX, positionY), Position(positionX+1, positionY))
                    position++
                    positionX++
                }
                currentChar == ':' -> {
                    tokenList += Token(TokenType.COLON, ":", Position(positionX, positionY), Position(positionX+1, positionY))
                    position++
                    positionX++
                }
                currentChar == ' ' -> {
                    position++
                    positionX++
                }
                // Para interpretar un salto de linea estamos asumiendo que se hace con \n
                // Pero puede ser que sea con ;
                currentChar == ';' -> {
                    tokenList += Token(TokenType.SEMICOLON, ";", Position(positionX, positionY), Position(positionX+1, positionY))
                    position++
                    positionX = 1
                    positionY ++
                }
                else -> {
                    println("Error: Caracter no reconocido")
                    return listOf()
                }
            }
        }
        return tokenList
    }

    private fun makeIdentifier(): Token {
        var identifier = ""
        // Mientras sea una letra o un digito, lo agregamos al identificador
        while (position < input.length && (input[position].isLetter() || input[position].isDigit())) {
            identifier += input[position]
            position++
            positionX++
        }
        // Si el identificador es una palabra reservada, devolvemos el token correspondiente
        return when (identifier) {
            "let" -> Token(TokenType.LET_KEYWORD, "let", Position(positionX - 3, positionY), Position(positionX, positionY))
            "println" -> Token(TokenType.PRINTLN_FUNCTION, "println", Position(positionX - 7, positionY), Position(positionX, positionY))
            "number" -> Token(TokenType.NUMBER_TYPE, "number", Position(positionX - 6, positionY), Position(positionX, positionY))
            "string" -> Token(TokenType.STRING_TYPE, "string", Position(positionX - 6, positionY), Position(positionX, positionY))
            else -> Token(TokenType.IDENTIFIER, identifier, Position(positionX - identifier.length, positionY), Position(positionX, positionY))
        }
        // falta resolver como diferenciar entre STRING_TYPE y STRING (Debe ser manejando las comillas "")
    }

    // Lo hacemos caracter por caracter porque no sabemos cuantos digitos tiene el numero
    private fun makeNumber(): Token {
        var number = ""
        while (position < input.length && input[position].isDigit()) {
            number += input[position]
            position++
            positionX++
        }
        return Token(TokenType.NUMBER, number, Position(positionX - number.length, positionY), Position(positionX, positionY))
    }


}