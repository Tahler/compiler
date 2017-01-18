package edu.neumont.csc330.tokenizer;

public enum TokenType {
    // data types
    INT,
    DOUBLE,
    STRING,
    VOID,

    // key words
    RETURN,
    IF,
    ELSE,
    FOR,

    // key symbols
    SEMICOLON,
    COMMA,
    // assignment
    EQUALS,
    // arithmetic
    PLUS,
    MINUS,
    ASTERISK,
    FORWARD_SLASH,
    // comparison
    EQUALS_EQUALS,
    NOT_EQUALS,
    LESS_THAN,
    GREATER_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN_OR_EQUAL,

    // braces
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    OPEN_CURLY,
    CLOSE_CURLY,
    OPEN_SQUARE,
    CLOSE_SQUARE,

    // literals
    INT_LITERAL,
    FLOAT_LITERAL,
    STRING_LITERAL,

    IDENTIFIER,

    TRASH_WORD,
}
