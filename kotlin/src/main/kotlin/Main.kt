
fun main() {

    val example = "let a: string = \"Hello\";" +
                  "println(a);"

    // El LEXER toma un string y lo convierte en una lista de tokens
    val lexer = Lexer(example)
    val tokens = lexer.makeTokens()
    println(tokens)

    // El PARSER toma una lista de tokens y la convierte en un AST
//    val parser = Parser(tokens)
//    val ast = parser.generateAST()
//    println(ast)

    // El INTERPRETE toma un AST y lo ejecuta
//    val interpreter = Interpreter()
//    val result = interpreter.consume(ast)
//    println(result)
}
