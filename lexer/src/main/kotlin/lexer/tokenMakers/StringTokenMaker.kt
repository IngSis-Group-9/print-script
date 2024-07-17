package lexer.tokenMakers

import token.Position
import token.Token
import token.TokenType

class StringTokenMaker : TokenMaker {
    override fun makeToken(
        input: String,
        position: Int,
        positionX: Int,
        positionY: Int,
    ): Token? {
        var str = ""
        var pos = position

        // Guarda el tipo de comillas y las saltea
        val quoteType = input[pos]
        pos++

        // Termina cuando encuentra el mismo tipo de comillas
        while (pos < input.length && input[pos] != quoteType) {
            str += input[pos]
            pos++
        }

        return Token(
            TokenType.STRING,
            str,
            Position(positionX, positionY),
            Position(positionX + str.length + 2, positionY),
        )
    }
}
