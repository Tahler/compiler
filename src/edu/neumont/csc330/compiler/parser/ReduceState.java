package edu.neumont.csc330.compiler.parser;

enum ReduceState {
    // Multi token
    FUNCTION_LIST_WITH_MAIN,
    FUNCTION_LIST,
    REDUCE_TO_FUNCTION_LIST_WITH_MAIN,

    FUNCTION,
    REDUCE_TO_FUNCTION_LIST,

    CLOSE_PARENTHESIS,
    CLOSE_PARENTHESIS__EXPRESSION,
    EXPRESSION,
    EXPRESSION__BINARY_OPERATOR,
    PLUS_PLUS,
    REDUCE_TO_EXPRESSION,

    BLOCK,
    BLOCK__CLOSE_PARENTHESIS,
    BLOCK__CLOSE_PARENTHESIS__PARAMETER,
    BLOCK__CLOSE_PARENTHESIS__PARAMETER__OPEN_PARENTHESIS,
    BLOCK__CLOSE_PARENTHESIS__PARAMETER__OPEN_PARENTHESIS_IDENTIFIER,
    REDUCE_TO_FUNCTION,

    BLOCK__CLOSE_PARENTHESIS__IDENTIFIER,
    BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE,
    BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE,
    BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING,
    BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING__OPEN_PARENTHESIS,
    BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING__OPEN_PARENTHESIS__MAIN,
    REDUCE_TO_MAIN_FUNCTION,

    IDENTIFIER,
    REDUCE_TO_PARAMETER,

    OPEN_PARENTHESIS,
    OPEN_PARENTHESIS__IDENTIFIER,
    REDUCE_TO_PARAMETER_LIST,

    PARAMETER,
    PARAMETER__COMMA,
    REDUCE_TO_NON_EMPTY_PARAMETER_LIST,

    // OPEN_PARENTHESIS
    REDUCE_TO_ARGUMENT_LIST,

//    EXPRESSION,
    EXPRESSION__OPEN_PARENTHESIS,
    EXPRESSION__COMMA,
    REDUCE_TO_NON_EMPTY_ARGUMENT_LIST,

//    CLOSE_PARENTHESIS,
    CLOSE_PARENTHESIS__ARGUMENT_LIST,
    CLOSE_PARENTHESIS__ARGUMENT_LIST__OPEN_PARENTHESIS,
    REDUCE_TO_WRITELINE_CALL,
    REDUCE_TO_FUNCTION_CALL,

    STATEMENT,
    STATEMENT_LIST,
    REDUCE_TO_STATEMENT_LIST,

    SEMICOLON,
    REDUCE_TO_LINE_STATEMENT,

    IDENTIFIER_LIST,
    REDUCE_TO_DECLARATION,

//    IDENTIFIER,
    IDENTIFIER__COMMA,
    REDUCE_TO_IDENTIFIER_LIST,

//    EXPRESSION,
    EXPRESSION__EQUALS,
    REDUCE_TO_ASSIGNMENT,

    ASSIGNMENT,
    REDUCE_TO_DECLARATION_ASSIGNMENT,

//    EXPRESSION,
    REDUCE_TO_RETURN_STATEMENT,

//    BLOCK,
//    BLOCK__CLOSE_PARENTHESIS,
    BLOCK__CLOSE_PARENTHESIS__LINE_STATEMENT,
    BLOCK__CLOSE_PARENTHESIS__LINE_STATEMENT__SEMICOLON,
    BLOCK__CLOSE_PARENTHESIS__LINE_STATEMENT__SEMICOLON__EXPRESSION,
    BLOCK__CLOSE_PARENTHESIS__LINE_STATEMENT__SEMICOLON__EXPRESSION__LINE_STATEMENT,
    BLOCK__CLOSE_PARENTHESIS__LINE_STATEMENT__SEMICOLON__EXPRESSION__LINE_STATEMENT__OPEN_PARENTHESIS,
    REDUCE_TO_FOR_STATEMENT,

//    BLOCK,
//    BLOCK__CLOSE_PARENTHESIS,
    BLOCK__CLOSE_PARENTHESIS__EXPRESSION,
    BLOCK__CLOSE_PARENTHESIS__EXPRESSION__OPEN_PARENTHESIS,
    REDUCE_TO_WHILE_STATEMENT,

    ELSE_SEGMENT,
    REDUCE_TO_IF_STATEMENT,

//    BLOCK,
//    BLOCK__CLOSE_PARENTHESIS,
//    BLOCK__CLOSE_PARENTHESIS__EXPRESSION,
    BLOCK__CLOSE_PARENTHESIS__EXPRESSION__OPEN_PARENTEHSIS,
    REDUCE_TO_IF_SEGMENT,

//    BLOCK,
    REDUCE_TO_ELSE_SEGMENT,

    CLOSE_CURLY,
    CLOSE_CURLY__STATEMENT_LIST,
    REDUCE_TO_BLOCK,

    // Single token
    REDUCE_TO_PROGRAM,
    REDUCE_TO_COMPARISON_OPERATOR,
    REDUCE_TO_RETURN_TYPE,
    REDUCE_TO_DATA_TYPE,
    REDUCE_TO_BINARY_OPERATOR,
    REDUCE_TO_UNARY_OPERATOR,
    REDUCE_TO_LITERAL,
    REDUCE_TO_LINE_STATEMENT_BODY,
    REDUCE_TO_STATEMENT,
    REDUCE_TO_BLOCK_STATEMENT,

    // Special
    _INITIAL,
    _INVALID, EXPRESSION__COMPARISON_OPERATOR, REDUCE_TO_EXPRESSION_FROM_IDENTIFIER, MINUS, RETURN, DOUBLE, INT, REDUCE_TO_RETURN_TYPE_FROM_DATA_TYPE, BLOCK__CLOSE_PARENTHESIS__PARAMETER__OPEN_PARENTHESIS__IDENTIFIER, REDUCE_TO_ARGUMENT_LIST_FROM_EMTPY, REDUCE_TO_BINARY_OPERATOR_FROM_MINUS, BLOCK__CLOSE__PARENTHESIS__LINE_STATEMENT_BODY, BLOCK__CLOSE__PARENTHESIS__LINE_STATEMENT_BODY__SEMICOLON, BLOCK__CLOSE__PARENTHESIS__LINE_STATEMENT_BODY__SEMICOLON__EXPRESSION, BLOCK__CLOSE__PARENTHESIS__LINE_STATEMENT_BODY__SEMICOLON__EXPRESSION__STATEMENT_LIST, BLOCK__CLOSE__PARENTHESIS__LINE_STATEMENT_BODY__SEMICOLON__EXPRESSION__STATEMENT_LIST__OPEN_PARENTHESIS, REDUCE_TO_PARAMETER_FROM_EMPTY, OPEN_PARENTHESIS_IDENTIFIER;

    public static final String REDUCE_IDENTIFIER = "REDUCE_TO";
}
