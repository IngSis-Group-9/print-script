package version_1

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

    PRINTLN_FUNCTION,
    IF_FUNCTION,
    ELSE,
    READINPUT_FUNCTION,
    READENV_FUNCTION,

    LPAREN,
    RPAREN,
    LKEY,
    RKEY,

    EQ,
    COLON,

    PLUS,
    MINUS,
    TIMES,
    DIV,
}