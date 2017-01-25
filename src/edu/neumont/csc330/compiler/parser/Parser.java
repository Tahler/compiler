package edu.neumont.csc330.compiler.parser;

import edu.neumont.csc330.compiler.tokenizer.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Parser {
    public Node parse(List<Token> tokens) {
        Stack<Node> stack = new Stack<>();

        for (int ii = 0; ii < tokens.size(); ii++) {
            Token curr = tokens.get(ii);
            Token look = (ii < tokens.size()) ? tokens.get(ii + 1) : null;

            stack.push(new TokenNode(curr));
            reduce(stack, look);
        }

        if (stack.size() == 1) {
            Node node = stack.pop();
            // TODO: verify this is a program node
            return node;
        } else {
            throw new RuntimeException("Tokens could not be reduce to a single node");
        }
    }

    public void reduce(Stack<Node> stack, Token look) {
        Stack<Node> roller = new Stack<>();
        ReduceState reduceState = ReduceState._INITIAL;

        do {
            Node node = stack.pop();
            roller.push(node);
            reduceState = advanceState(reduceState, look);
            if (shouldReduce(reduceState, look)) {
                Node reducedNode = collapse(reduceState, look, stack, roller);
                stack.push(reducedNode);
            }
        } while (reduceState != ReduceState._INVALID && !stack.isEmpty());

        while (!roller.isEmpty()) {
            Node node = roller.pop();
            stack.push(node);
        }
    }

    private Node collapse(ReduceState reduceState, Token look, Stack<Node> stack, Stack<Node> roller) {
        return null;
    }

    private boolean shouldReduce(ReduceState reduceState, Token look) {
        return false;
    }

    private ReduceState advanceState(ReduceState reduceState, Token look) {
        // TODO: everything!
        return null;
    }
}

class Node {
    List<Node> children;
}

class TokenNode extends Node {
    Token value;

    public TokenNode(Token value) {
        this.value = value;
    }
}

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
    BLOCK__CLOSE_PARENTHESIS__PARAMETER_LIST,
    BLOCK__CLOSE_PARENTHESIS__PARAMETER_LIST__OPEN_PARENTHESIS,
    BLOCK__CLOSE_PARENTHESIS__PARAMETER_LIST__OPEN_PARENTHESIS_IDENTIFIER,
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
    REDUCE_TO_FUNCTION_CALL,

    STATEMENT,
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
    _INVALID

}
