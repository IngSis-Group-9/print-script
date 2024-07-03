package lexer.tokenMakers

import token.Position
import token.Token
import token.TokenType

class NumberTokenMaker : TokenMaker {
    override fun makeToken(
        input: String,
        position: Int,
        positionX: Int,
        positionY: Int,
    ): Token? {
        var number = ""
        var pos = position
        var decimalPointEncountered = false
        while (pos < input.length && input[pos].isDigit() || input[pos] == '.') {
            if (input[pos] == '.') {
                if (decimalPointEncountered) {
                    // Ya se encontró un punto decimal en este número, por lo que no se puede agregar otro
                    throw RuntimeException("Invalid number format at position $positionX:$positionY")
                } else {
                    decimalPointEncountered = true
                }
            }
            number += input[pos]
            pos++
        }
        return if (number.isNotEmpty()) {
            Token(
                TokenType.NUMBER,
                number,
                Position(positionX, positionY),
                Position(positionX + number.length, positionY),
            )
        } else {
            null
        }
    }
}
