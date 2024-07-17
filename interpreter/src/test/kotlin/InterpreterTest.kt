import ast.ASTNode
import ast.BinaryOperation
import ast.BooleanOperator
import ast.Conditional
import ast.Declaration
import ast.DeclarationAssignation
import ast.IdentifierOperator
import ast.Method
import ast.NumberOperator
import ast.SimpleAssignation
import ast.StringOperator
import interpreter.ExecuteInterpreter
import interpreter.VariableManager
import interpreter.response.ErrorResponse
import interpreter.response.SuccessResponse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals

class InterpreterTest {
    private val interpreter = ExecuteInterpreter.getDefaultInterpreter(VariableManager())

    @BeforeEach
    fun setUp() {
        interpreter.getVariableManager().clear()
    }

    // let a: string = "Hello" + 5;
    // println(a);
    @Test
    fun `test 001 - should interpret an AST and execute it`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "string"),
                    BinaryOperation(
                        StringOperator("Hello"),
                        "+",
                        NumberOperator(5),
                    ),
                    false,
                ),
                Method("println", BinaryOperation(IdentifierOperator("a"), "+", StringOperator(""))),
            )
        val result = interpreter.interpretAST(ast)
        // assert result is success response
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("Hello5\n", result.message)
    }

    // let a: number = 1.0;
    // let x: string = a + "Hello";
    // println(x);
    @Test
    fun `test 002 - should interpret an AST and execute it`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "number"),
                    NumberOperator(1),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("x", "string"),
                    BinaryOperation(
                        IdentifierOperator("a"),
                        "+",
                        StringOperator("Hello"),
                    ),
                    false,
                ),
                Method("println", IdentifierOperator("x")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("1Hello\n", result.message)
    }

    // "let a: number = 5 * 5;"
    // "println(a);"
    @Test
    fun `test 003 - should multiply two numbers`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "number"),
                    BinaryOperation(
                        NumberOperator(5),
                        "*",
                        NumberOperator(5),
                    ),
                    false,
                ),
                Method("println", IdentifierOperator("a")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("25\n", result.message)
    }

    // let a: number = 5;
    // let b: number = 5;
    // let c: number = a + b;
    // println(c);
    @Test
    fun `test 004 - should add two numbers`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "number"),
                    NumberOperator(5),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("b", "number"),
                    NumberOperator(5),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("c", "number"),
                    BinaryOperation(
                        IdentifierOperator("a"),
                        "+",
                        IdentifierOperator("b"),
                    ),
                    false,
                ),
                Method("println", IdentifierOperator("c")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("10\n", result.message)
    }

    // let a: number = 10;
    // let b: number = 5;
    // let c: number = a - b;
    // println(c);
    @Test
    fun `test 005 - should subtract two numbers`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "number"),
                    NumberOperator(10),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("b", "number"),
                    NumberOperator(5),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("c", "number"),
                    BinaryOperation(
                        IdentifierOperator("a"),
                        "-",
                        IdentifierOperator("b"),
                    ),
                    false,
                ),
                Method("println", IdentifierOperator("c")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("5\n", result.message)
    }

    // let a: number = 10;
    // let b: number = 5;
    // let c: number = a / b;
    // println(c);
    @Test
    fun `test 006 - should divide two numbers`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "number"),
                    NumberOperator(10),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("b", "number"),
                    NumberOperator(5),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("c", "number"),
                    BinaryOperation(
                        IdentifierOperator("a"),
                        "/",
                        IdentifierOperator("b"),
                    ),
                    false,
                ),
                Method("println", IdentifierOperator("c")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("2\n", result.message)
    }

    // let a: string = "Hello";
    // let b: string = " World";
    // let c: string = a + b;
    // println(c);
    @Test
    fun `test 007 - should concatenate two strings`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "string"),
                    StringOperator("Hello"),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("b", "string"),
                    StringOperator(" World"),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("c", "string"),
                    BinaryOperation(
                        IdentifierOperator("a"),
                        "+",
                        IdentifierOperator("b"),
                    ),
                    false,
                ),
                Method("println", IdentifierOperator("c")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("Hello World\n", result.message)
    }

    // let a: string;
    @Test
    fun `test 008 - should interpret a declaration node`() {
        val ast =
            listOf(
                Declaration("a", "string"),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    // let a: string;
    // a = "Hello";
    @Test
    fun `test 009 - should interpret a simple assignation node`() {
        val ast =
            listOf(
                Declaration("a", "string"),
                SimpleAssignation("a", StringOperator("Hello")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    // "let result: number = 5 + 5 * 10 - 2 / 2;"
    // "println(result);"
    @Test
    fun `test 013 - should interpret a complex number declaration with assignment`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("result", "number"),
                    BinaryOperation(
                        BinaryOperation(
                            NumberOperator(5),
                            "+",
                            BinaryOperation(
                                NumberOperator(5),
                                "*",
                                NumberOperator(10),
                            ),
                        ),
                        "-",
                        BinaryOperation(
                            NumberOperator(2),
                            "/",
                            NumberOperator(2),
                        ),
                    ),
                    false,
                ),
                Method("println", IdentifierOperator("result")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("54\n", result.message)
    }

    // "let x: number = 5.5;"
    @Test
    fun `test 014 - should interpret a number declaration with decimal assignment`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    NumberOperator(5.5),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    // "let sum: number = 5.5 + 5.5;"
    @Test
    fun `test 015 - should interpret a number declaration with decimal addition assignment`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("sum", "number"),
                    BinaryOperation(
                        NumberOperator(5.5),
                        "+",
                        NumberOperator(5.5),
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    // "let diff: number = 10.5 - 5.5;"
    @Test
    fun `test 016 - should interpret a number declaration with decimal subtraction assignment`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("diff", "number"),
                    BinaryOperation(
                        NumberOperator(10.5),
                        "-",
                        NumberOperator(5.5),
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    // "let prod: number = 5.5 * 5.5;"
    @Test
    fun `test 017 - should interpret a number declaration with decimal multiplication assignment`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("prod", "number"),
                    BinaryOperation(
                        NumberOperator(5.5),
                        "*",
                        NumberOperator(5.5),
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    // "let quot: number = 10.5 / 5.5;"
    @Test
    fun `test 018 - should interpret a number declaration with decimal division assignment`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("quot", "number"),
                    BinaryOperation(
                        NumberOperator(10.5),
                        "/",
                        NumberOperator(5.5),
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    // "let result: number = 5.5 + 5.5 * 10.5 - 2.5 / 2.5;"
    @Test
    fun `test 019 - should interpret a complex number declaration with decimal assignment`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("result", "number"),
                    BinaryOperation(
                        BinaryOperation(
                            NumberOperator(5.5),
                            "+",
                            BinaryOperation(
                                NumberOperator(5.5),
                                "*",
                                NumberOperator(10.5),
                            ),
                        ),
                        "-",
                        BinaryOperation(
                            NumberOperator(2.5),
                            "/",
                            NumberOperator(2.5),
                        ),
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    @Test
    fun `test 020 - should throw exception for undeclared variable`() {
        val ast =
            listOf(
                SimpleAssignation("x", NumberOperator(5.5)), // "x" is not declared
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable x not declared", result.message)
    }

    @Test
    fun `test 021 - should throw exception for redeclaration of variable`() {
        val ast =
            listOf(
                Declaration("x", "number"),
                Declaration("x", "number"), // "x" is redeclared
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable x already declared", result.message)
    }

    @Test
    fun `test 022 - should throw exception for type mismatch in assignment`() {
        val ast =
            listOf(
                Declaration("x", "number"),
                SimpleAssignation("x", StringOperator("Hello")), // "x" is a number, but we try to assign a string
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Type mismatch in variable x assignment", result.message)
    }

    @Test
    fun `test 023 - should throw exception for unsupported operation`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    BinaryOperation(
                        StringOperator("Hello"),
                        "-",
                        StringOperator("World"), // "-" operation is not supported for strings
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Unsupported operation: '-' with non-numeric operands", result.message)
    }

    @Test
    fun `test 024 - should throw exception for unexpected method`() {
        val ast =
            listOf(
                Method("unknownMethod", NumberOperator(5)), // "unknownMethod" is not a supported method
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Unsupported method: unknownMethod", result.message)
    }

//    @Test
//    fun `test 025 - should throw exception for unexpected ASTNode type`() {
//        val ast = listOf(
//            object : ASTNode {} // An unexpected ASTNode type
//        )
//        val result = interpreter.interpretAST(ast)
//        assertTrue(result is ErrorResponse)
//        result as ErrorResponse
//        assertEquals("Unsupported method: unknownMethod", result.message)
//    }

    @Test
    fun `test 026 - should throw exception for variable already declared`() {
        val ast =
            listOf(
                Declaration("x", "number"),
                DeclarationAssignation(
                    Declaration("x", "number"), // "x" is already declared
                    NumberOperator(5),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable x already declared", result.message)
    }

    @Test
    fun `test 027 - should throw exception for variable not declared`() {
        val ast =
            listOf(
                SimpleAssignation("x", NumberOperator(5)), // "x" is not declared
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable x not declared", result.message)
    }

//    @Test
//    fun `test 028 - should throw exception for unexpected operation`() {
//        val ast = listOf(
//            DeclarationAssignation(
//                Declaration("x", "number"),
//                object : BinaryNode {} // An unexpected operation
//            )
//        )
//        val interpreter = interpreter.interpreters.Interpreter()
//        assertThrows(Exception::class.java) {
//            interpreter.consume(ast)
//        }
//    }

    @Test
    fun `test 029 - should throw exception for variable not declared in operation`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    IdentifierOperator("y"), // "y" is not declared
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable y not declared", result.message)
    }

    @Test
    fun `test 030 - should throw exception for unsupported operation with non-numeric operands`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    BinaryOperation(
                        StringOperator("Hello"),
                        "*",
                        StringOperator("World"), // "*" operation is not supported for strings
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Unsupported operation: '*' with non-numeric operands", result.message)
    }

    @Test
    fun `test 031 - should throw exception for unsupported operation with non-numeric operands in division`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    BinaryOperation(
                        StringOperator("Hello"),
                        "/",
                        StringOperator("World"), // "/" operation is not supported for strings
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Unsupported operation: '/' with non-numeric operands", result.message)
    }

    @Test
    fun `test 032 - should throw exception for invalid operation symbol`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    BinaryOperation(
                        NumberOperator(5),
                        "%", // "%" is not a valid operation
                        NumberOperator(5),
                    ),
                    false,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("% is not a valid operation", result.message)
    }

    @Test
    fun `test 033 - const assignation shouldn't be able to override`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    NumberOperator(1),
                    true,
                ),
                SimpleAssignation(
                    "x",
                    NumberOperator(10),
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable x is constant", result.message)
    }

    @Test
    fun `test 034 - Conditional statement true`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "boolean"),
                    BooleanOperator("true"),
                    false,
                ),
                Conditional(
                    IdentifierOperator("x"),
                    listOf(
                        Method("println", StringOperator("true")),
                    ),
                    listOf(
                        Method("println", StringOperator("false")),
                    ),
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("true\n", result.message)
    }

    @Test
    fun `test 035 - Conditional statement false`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "boolean"),
                    BooleanOperator("false"),
                    false,
                ),
                Conditional(
                    IdentifierOperator("x"),
                    listOf(
                        Method("println", StringOperator("true")),
                    ),
                    listOf(
                        Method("println", StringOperator("false")),
                    ),
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("false\n", result.message)
    }

    @Test
    fun `test 035 - Conditional statement false without else body`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "boolean"),
                    BooleanOperator("false"),
                    false,
                ),
                Conditional(
                    IdentifierOperator("x"),
                    listOf(
                        Method("println", StringOperator("true")),
                    ),
                    null,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals(null, result.message)
    }

    @Test
    fun `test 036 - when define variable in if statement to not appear out of scope`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "boolean"),
                    BooleanOperator("true"),
                    false,
                ),
                Conditional(
                    IdentifierOperator("x"),
                    listOf(
                        DeclarationAssignation(
                            Declaration("y", "number"),
                            NumberOperator(5),
                            false,
                        ),
                    ),
                    null,
                ),
                Method("println", IdentifierOperator("y")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable y not declared", result.message)
    }

    @Test
    fun `test 037 - change in if statement a variable declare out of the scope`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    NumberOperator(5),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("y", "boolean"),
                    BooleanOperator("true"),
                    false,
                ),
                Conditional(
                    IdentifierOperator("y"),
                    listOf(
                        SimpleAssignation(
                            "x",
                            NumberOperator(10),
                        ),
                    ),
                    null,
                ),
                Method("println", IdentifierOperator("x")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("10\n", result.message)
    }

    @Test
    fun `test 038 - when variable define in outer if statement shouldn't be able to define in inner if statement`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "boolean"),
                    BooleanOperator("true"),
                    false,
                ),
                Conditional(
                    IdentifierOperator("x"),
                    listOf(
                        DeclarationAssignation(
                            Declaration("y", "number"),
                            NumberOperator(5),
                            false,
                        ),
                        Conditional(
                            BooleanOperator("true"),
                            listOf(
                                DeclarationAssignation(
                                    Declaration("y", "number"),
                                    NumberOperator(5),
                                    false,
                                ),
                            ),
                            null,
                        ),
                    ),
                    null,
                ),
                Method("println", IdentifierOperator("y")),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
        assertEquals("Variable y already declared", result.message)
    }

    @Test
    fun `test 039 - when variable define in outer if statement should be able to use in inner if statement`() {
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "boolean"),
                    BooleanOperator("true"),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("y", "number"),
                    NumberOperator(5),
                    false,
                ),
                Conditional(
                    IdentifierOperator("x"),
                    listOf(
                        Method("println", IdentifierOperator("y")),
                    ),
                    null,
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("5\n", result.message)
    }

    @Test
    fun `test 040 - readInput function`() {
        val ast =
            listOf(
                Method("println", Method("readInput", StringOperator("Enter a value:"))),
            )
        val input = "Test input"
        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val result = interpreter.interpretAST(ast)
        assertTrue(result is SuccessResponse)
        result as SuccessResponse
        assertEquals("${input}\n", result.message)
    }

    @Test
    fun `test 041 - condition statement without boolean operator`() {
        val ast =
            listOf(
                Conditional(
                    NumberOperator(5),
                    listOf(
                        Method("println", StringOperator("true")),
                    ),
                    listOf(
                        Method("println", StringOperator("false")),
                    ),
                ),
            )
        val result = interpreter.interpretAST(ast)
        assertTrue(result is ErrorResponse)
        result as ErrorResponse
    }

    @Test
    fun `test 042 - get interpreter version 1_0`() {
        val interpreter = ExecuteInterpreter.getInterpreterByVersion("1.0")
        val astNode: List<ASTNode> =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    NumberOperator(5),
                    false,
                ),
                Method("println", IdentifierOperator("x")),
                Declaration("y", "number"),
            )
        assertTrue(interpreter.interpretAST(astNode) is SuccessResponse)
        val conditionalNode =
            listOf(
                Conditional(
                    BooleanOperator("true"),
                    listOf(
                        Method("println", StringOperator("true")),
                    ),
                    listOf(
                        Method("println", StringOperator("false")),
                    ),
                ),
            )
        val result = interpreter.interpretAST(conditionalNode)
        assertTrue(result is ErrorResponse)
        assertEquals("Unsupported ASTNode: Conditional", (result as ErrorResponse).message)
    }

    @Test
    fun `test 042 - get interpreter version 1_1`() {
        val interpreter = ExecuteInterpreter.getInterpreterByVersion("1.1")
        val astNode: List<ASTNode> =
            listOf(
                DeclarationAssignation(
                    Declaration("x", "number"),
                    NumberOperator(5),
                    false,
                ),
                Method("println", IdentifierOperator("x")),
                Declaration("y", "number"),
                Conditional(
                    BooleanOperator("true"),
                    listOf(
                        Method("println", StringOperator("true")),
                    ),
                    listOf(
                        Method("println", StringOperator("false")),
                    ),
                ),
            )
        assertTrue(interpreter.interpretAST(astNode) is SuccessResponse)
    }
//    @Test
//    fun `test 033 - should throw exception for unexpected binary operation node`() {
//        val ast = listOf(
//            DeclarationAssignation(
//                Declaration("x", "number"),
//                object : BinaryNode {} // An unexpected binary operation node
//            )
//        )
//        val interpreter = interpreter.interpreters.Interpreter()
//        assertThrows(Exception::class.java) {
//            interpreter.consume(ast)
//        }
//    }
}
