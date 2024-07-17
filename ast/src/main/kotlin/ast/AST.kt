package ast

sealed interface ASTNode

sealed interface Assignation : ASTNode

sealed interface ValueNode : ASTNode

data class UnhandledNode(val boolean: Boolean) : ASTNode

// let x: number;
data class Declaration(val identifier: String, val type: String) : ASTNode

// let x: number = 5;
data class DeclarationAssignation(val declaration: Declaration, val value: ValueNode, val isConst: Boolean) : Assignation

// x = 5 + 5;
data class SimpleAssignation(val identifier: String, val value: ValueNode) : Assignation

// println(x);
data class Method(val identifier: String, val value: ValueNode) : ValueNode

// if (x > 5) { println(x); } else { println(5); }
data class Conditional(val condition: ValueNode, val then: List<ASTNode>, val otherwise: List<ASTNode>?) : ASTNode

// NodeL(value) (+ - * /) NodeR(value)
data class BinaryOperation(val left: ValueNode, val symbol: String, val right: ValueNode) : ValueNode

// Represents any String like "Hello"
data class StringOperator(val value: String) : ValueNode

// Represents any Number like 5
data class NumberOperator(val value: Number) : ValueNode

// Represents any Identifier like x
data class IdentifierOperator(val identifier: String) : ValueNode

// Represents a Boolean like true or false
data class BooleanOperator(val value: String) : ValueNode
