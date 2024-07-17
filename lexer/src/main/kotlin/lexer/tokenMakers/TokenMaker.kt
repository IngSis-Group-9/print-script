package lexer.tokenMakers

import token.Token

interface TokenMaker {
    fun makeToken(
        input: String,
        position: Int,
        positionX: Int,
        positionY: Int,
    ): Token?
}
