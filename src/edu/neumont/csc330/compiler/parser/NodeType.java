package edu.neumont.csc330.compiler.parser;

import edu.neumont.csc330.compiler.tokenizer.TokenType;

public enum NodeType {
    // data types
    INT,
    DOUBLE,
    STRING,
    VOID,

    // key words
    ELSE,
    FOR,
    IF,
    WHILE,
    RETURN,

    // key symbols
    SEMICOLON,
    COMMA,

    // assignment
    EQUALS,
    PLUS_PLUS,

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
    MAIN,

    // Complex types
    PROGRAM,
    FUNCTION_LIST_WITH_MAIN,
    FUNCTION_LIST,
    COMPARISON_OPERATOR,
    RETURN_TYPE,
    DATA_TYPE,
    BINARY_OPERATOR,
    UNARY_OPERATOR,
    EXPRESSION,
    LITERAL,
    FUNCTION,
    MAIN_FUNCTION,
    PARAMETER,
    ARGUMENT_LIST,
    FUNCTION_CALL,
    STATEMENT_LIST,
    STATEMENT,
    LINE_STATEMENT,
    LINE_STATEMENT_BODY,
    DECLARATION,
    IDENTIFIER_LIST,
    ASSIGNMENT,
    DECLARATION_ASSIGNMENT,
    RETURN_STATEMENT,
    BLOCK_STATEMENT,
    FOR_STATEMENT,
    WHILE_STATEMENT,
    IF_STATEMENT,
    IF_SEGMENT,
    ELSE_SEGMENT,
    BLOCK,;

    public static NodeType fromTokenType(TokenType type) {
        return NodeType.valueOf(type.name());
    }
}
