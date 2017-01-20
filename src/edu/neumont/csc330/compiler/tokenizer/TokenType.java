package edu.neumont.csc330.compiler.tokenizer;

public enum TokenType {
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

    TRASH_WORD;

    public static TokenType fromState(State endState) {
        TokenType type;
        switch (endState) {
            case DOUBLE:
                type = TokenType.DOUBLE;
                break;
            case ELSE:
                type = TokenType.ELSE;
                break;
            case FOR:
                type = TokenType.FOR;
                break;
            case IF:
                type = TokenType.IF;
                break;
            case INT:
                type = TokenType.INT;
                break;
            case RETURN:
                type = TokenType.RETURN;
                break;
            case STRING:
                type = TokenType.STRING;
                break;
            case VOID:
                type = TokenType.VOID;
                break;
            case WHILE:
                type = TokenType.WHILE;
                break;
            case _EQUALS:
                type = TokenType.EQUALS;
                break;
            case _EQUALS_EQUALS:
                type = TokenType.EQUALS_EQUALS;
                break;
            case _NOT_EQUALS:
                type = TokenType.NOT_EQUALS;
                break;
            case _LESS_THAN:
                type = TokenType.LESS_THAN;
                break;
            case _LESS_THAN_EQUAL:
                type = TokenType.LESS_THAN_OR_EQUAL;
                break;
            case _GREATER_THAN:
                type = TokenType.GREATER_THAN;
                break;
            case _GREATER_THAN_EQUAL:
                type = TokenType.GREATER_THAN_OR_EQUAL;
                break;
            case _OPEN_PARENTHESIS:
                type = TokenType.OPEN_PARENTHESIS;
                break;
            case _CLOSE_PARENTHESIS:
                type = TokenType.CLOSE_PARENTHESIS;
                break;
            case _OPEN_CURLY:
                type = TokenType.OPEN_CURLY;
                break;
            case _CLOSE_CURLY:
                type = TokenType.CLOSE_CURLY;
                break;
            case _OPEN_SQUARE:
                type = TokenType.OPEN_SQUARE;
                break;
            case _CLOSE_SQUARE:
                type = TokenType.CLOSE_SQUARE;
                break;
            case _INT_LITERAL:
                type = TokenType.INT_LITERAL;
                break;
            case _FLOAT_LITERAL:
                type = TokenType.FLOAT_LITERAL;
                break;
            case _COMMA:
                type = TokenType.COMMA;
                break;
            case _SEMICOLON:
                type = TokenType.SEMICOLON;
                break;
            case _PLUS:
                type = TokenType.PLUS;
                break;
            case _PLUS_PLUS:
                type = TokenType.PLUS_PLUS;
                break;
            case _MINUS:
                type = TokenType.MINUS;
                break;
            case _ASTERISK:
                type = TokenType.ASTERISK;
                break;
            case _FORWARD_SLASH:
                type = TokenType.FORWARD_SLASH;
                break;
            case _IDENTIFIER:
                type = TokenType.IDENTIFIER;
                break;
            case PRIVATE:
            case PUBLIC:
                type = TokenType.TRASH_WORD;
                break;
            case _INVALID:
                type = null;
                break;
            default:
                type = TokenType.IDENTIFIER;
                break;
        }
        return type;
    }
}
