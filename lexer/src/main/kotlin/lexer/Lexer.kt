package lexer

import lexer.tokenMakers.IdentifierTokenMaker
import lexer.tokenMakers.NewLineTokenMaker
import lexer.tokenMakers.NumberTokenMaker
import lexer.tokenMakers.StringTokenMaker
import lexer.tokenMakers.SymbolTokenMaker
import lexer.tokenMakers.TokenMaker
import token.Token
import token.TokenType

class Lexer(private val tokenMakers: Map<Char, TokenMaker>) {
    fun makeTokens(inputText: String): List<Token> {
        var position = 0
        var positionX = 1
        var positionY = 1
        val tokenList = mutableListOf<Token>()

        val input = inputText.replace("\r\n", "\n").replace("\r", "\n")

        while (position < input.length) {
            val currentChar = input[position]

            // Ignore spaces
            if (currentChar == ' ') {
                position++
                positionX++
                continue
            }

            // Get the token maker for the current character
            val tokenMaker = tokenMakers[currentChar]
            if (tokenMaker != null) {
                val token = tokenMaker.makeToken(input, position, positionX, positionY)
                if (token != null) {
                    tokenList.add(token)
                    position += token.value.length
                    if (token.type == TokenType.NEW_LINE) {
                        positionY++
                        positionX = 1
                        continue
                    }
                    positionX += token.value.length
                    // Skip the quotes of strings
                    if (token.type == TokenType.STRING) {
                        position += 2
                        positionX += 2
                    }
                }
            } else {
                throw Exception("Unrecognized character $currentChar at position $positionX:$positionY")
            }
        }

        return tokenList
    }

    companion object {
        fun getDefaultLexer(): Lexer {
            return getLexerByVersion("1.1")
        }

        fun getLexerByVersion(version: String): Lexer {
            return Lexer(
                ('0'..'9').associateWith { NumberTokenMaker() } +
                    ('a'..'z').associateWith { IdentifierTokenMaker(version) } +
                    ('A'..'Z').associateWith { IdentifierTokenMaker(version) } +
                    mapOf(
                        Pair('\"', StringTokenMaker()),
                        Pair('\'', StringTokenMaker()),
                        Pair('(', SymbolTokenMaker(version)),
                        Pair(')', SymbolTokenMaker(version)),
                        Pair('=', SymbolTokenMaker(version)),
                        Pair(':', SymbolTokenMaker(version)),
                        Pair('+', SymbolTokenMaker(version)),
                        Pair('-', SymbolTokenMaker(version)),
                        Pair('*', SymbolTokenMaker(version)),
                        Pair('/', SymbolTokenMaker(version)),
                        Pair(';', SymbolTokenMaker(version)),
                        Pair('{', SymbolTokenMaker(version)),
                        Pair('}', SymbolTokenMaker(version)),
                        Pair('\n', NewLineTokenMaker()),
                    ),
            )
        }
    }
}
