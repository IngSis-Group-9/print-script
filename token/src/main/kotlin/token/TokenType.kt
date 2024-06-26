package token

enum class TokenType {
    STRING_TYPE,
    NUMBER_TYPE,
    BOOLEAN_TYPE,

    STRING,
    NUMBER,
    BOOLEAN,

    IDENTIFIER,
    SEMICOLON,
    NEW_LINE,

    LET_KEYWORD,
    CONST_KEYWORD,

    IF_KEYWORD,
    ELSE_KEYWORD,

    PRINTLN_FUNCTION,
    READINPUT_FUNCTION,
    READENV_FUNCTION,

    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    EQ,
    COLON,

    PLUS,
    MINUS,
    TIMES,
    DIV,

    UNKNOWN,
}
