package cli.commands

import ast.ASTNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import lexer.TokenProvider
import parser.Parser
import java.io.File
import kotlin.math.ceil

interface Command {
    fun execute()

    fun fillAstListWithProgress(
        file: File,
        parser: Parser,
        tokenProvider: TokenProvider,
    ): MutableList<ASTNode> {
        val astList = mutableListOf<ASTNode>()

        val totalLines = file.readLines().size
        val progressInterval = totalLines / 10 // Actualizar la barra de progreso cada 10%

        var linesProcessed = 0
        var progressCounter = 0

        // Mutex para evitar condiciones de carrera al actualizar el progreso
        val progressMutex = Mutex()

        runBlocking {
            launch(Dispatchers.IO) {
                while (tokenProvider.hasNextStatement()) {
                    val tokens = tokenProvider.readStatement()
                    val ast = parser.generateAST(tokens)
                    ast?.let {
                        // Incrementar el contador de líneas procesadas
                        linesProcessed++
                        // Incrementar el contador de progreso
                        progressCounter++
                        // Actualizar la barra de progreso si es necesario
                        if (progressCounter >= progressInterval || linesProcessed == totalLines) {
                            // Bloquear el mutex antes de actualizar el progreso
                            progressMutex.withLock {
                                val progress = (linesProcessed.toDouble() / totalLines.toDouble()) * 100
                                printProgress(progress)
                                // Reiniciar el contador de progreso
                                progressCounter = 0
                            }
                        }
                        astList.add(it)
                    } ?: run {
                        throw IllegalArgumentException("Error: Invalid syntax in tokens: $tokens")
                    }
                }
            }
        }
        return astList
    }

    fun printProgress(progress: Double) {
        // Limpiar la línea anterior antes de imprimir el progreso actualizado
        print("\r" + " ".repeat(50) + "\r")
        // Calcular el número de bloques llenos y vacíos para la barra de progreso
        val numBlocksFilled = ceil(progress / 2).toInt()
        val numBlocksEmpty = 50 - numBlocksFilled
        // Imprimir la barra de progreso
        print("[" + "#".repeat(numBlocksFilled) + "-".repeat(numBlocksEmpty) + "]  ${progress.toInt()}% \n")
    }
}
