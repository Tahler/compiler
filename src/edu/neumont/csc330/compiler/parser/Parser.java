package edu.neumont.csc330.compiler.parser;

import edu.neumont.csc330.compiler.tokenizer.Token;
import edu.neumont.csc330.compiler.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

    public Node parse(List<Token> tokens) {
        Stack<Node> stack = new Stack<>();

        for (int ii = 0; ii < tokens.size(); ii++) {
            Token curr = tokens.get(ii);
            Token look = (ii < tokens.size() - 1) ? tokens.get(ii + 1) : null;

            NodeType type = NodeType.fromTokenType(curr.getType());

            stack.push(new TokenNode(type, curr));
            reduce(stack, look);
        }

        if (stack.size() == 1) {
            Node node = stack.pop();
//                if (node.type == NodeType.PROGRAM) {
                    return node;
//                } else {
//                    throw new RuntimeException("Tokens could not be reduced to a program node");
//                }
        } else {
            while (!stack.isEmpty()) {
                System.out.println(stack.pop().getType());
            }
//            throw new RuntimeException("Tokens could not be reduced to a single node");
            return null;
        }
    }

    public void reduce(Stack<Node> stack, Token look) {
        Stack<Node> roller = new Stack<>();
        ReduceState reduceState = ReduceState._INITIAL;

        do {
            Node node = stack.pop();
            roller.push(node);
            reduceState = advanceState(reduceState, node, look);
            if (shouldReduce(reduceState, look)) {
                Node reducedNode = collapse(reduceState, look, stack, roller);
                stack.push(reducedNode);
                reduceState = ReduceState._INITIAL;
            }
        } while (reduceState != ReduceState._INVALID && !stack.isEmpty());

        while (!roller.isEmpty()) {
            Node node = roller.pop();
            stack.push(node);
        }
    }

    private Node collapse(ReduceState reduceState, Token look, Stack<Node> stack, Stack<Node> roller) {

        String reduceTo = reduceState.name().substring(ReduceState.REDUCE_IDENTIFIER.length() + 1);
        if (reduceTo.contains("FROM")) {
            reduceTo = reduceTo.substring(0, reduceTo.indexOf("FROM") - 1);
        }
        NodeType type = NodeType.valueOf(reduceTo);

        // TODO: handle more than simple case
        // TODO: lists should consume the children lists and make their children their own
        List<Node> children = new ArrayList<>();
        switch (reduceState) {
            case REDUCE_TO_ARGUMENT_LIST_FROM_EMTPY:
                while (!roller.isEmpty()) {
                    stack.push(roller.pop());
                }
                break;
            case REDUCE_TO_EXPRESSION_FROM_IDENTIFIER:
            case REDUCE_TO_UNARY_OPERATOR:
            case REDUCE_TO_DATA_TYPE:
            case REDUCE_TO_RETURN_TYPE_FROM_DATA_TYPE:
                stack.push(roller.pop());
                children.add(roller.pop());
                assert (roller.isEmpty());
                break;
            default:
            switch (type) {
                case STATEMENT_LIST:
                case IDENTIFIER_LIST:
                    while (!roller.isEmpty()) {
                        Node node = roller.pop();
                        if (node.getType() == type) {
                            children.addAll(node.getChildren());
                        } else {
                            children.add(node);
                        }
                    }
                    break;
                default:
                    while (!roller.isEmpty()) {
                        Node node = roller.pop();
                        children.add(node);
                    }
                    break;
            }
        }

        Node reduced = new Node(type, children);
        return reduced;
    }

    private boolean shouldReduce(ReduceState reduceState, Token look) {
        return reduceState.name().startsWith(ReduceState.REDUCE_IDENTIFIER);
    }

    private ReduceState advanceState(ReduceState reduceState, Node node, Token look) {
//        System.out.println(reduceState +" : " + node.getType());
        // TODO: everything!
        ReduceState nextState = ReduceState._INVALID;
        switch (reduceState) {
            case _INITIAL:
                switch (node.getType()) {
                    // TODO: These do not actually belong in initial -- but make it possible if there's nothing above it
                    // will also require one of the special reduce cases
                    case DOUBLE:
                        nextState = ReduceState.DOUBLE;
                        break;
                    case  INT:
                        nextState = ReduceState.INT;
                        break;
                    case IDENTIFIER:
                        // TODO: when should this become an expression?
                        nextState = ReduceState.IDENTIFIER;
                        break;
                    case FLOAT_LITERAL:
                    case INT_LITERAL:
                        nextState = ReduceState.REDUCE_TO_LITERAL;
                        break;
                    case PLUS:
                    case ASTERISK:
                    case FORWARD_SLASH:
                        nextState = ReduceState.REDUCE_TO_BINARY_OPERATOR;
                        break;
                    case LITERAL:
                        nextState = ReduceState.REDUCE_TO_EXPRESSION;
                        break;
                    case EXPRESSION:
                        nextState = ReduceState.EXPRESSION;
                        break;
                    case DECLARATION_ASSIGNMENT:
                    case ASSIGNMENT:
                    case RETURN_STATEMENT:
                        nextState = ReduceState.REDUCE_TO_LINE_STATEMENT_BODY;
                        break;
                    case DECLARATION:
                        if (look.getType() == TokenType.CLOSE_PARENTHESIS) {
                            nextState = ReduceState.REDUCE_TO_PARAMETER;
                        } else if (look.getType() != TokenType.EQUALS) {
                            nextState = ReduceState.REDUCE_TO_LINE_STATEMENT_BODY;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    case IDENTIFIER_LIST:
                        nextState = ReduceState.IDENTIFIER_LIST;
                        break;
                    case SEMICOLON:
                        nextState = ReduceState.SEMICOLON;
                        break;
                    case LINE_STATEMENT:
                    case BLOCK_STATEMENT:
                        nextState = ReduceState.REDUCE_TO_STATEMENT;
                        break;
                    case STATEMENT:
                        nextState = ReduceState.REDUCE_TO_STATEMENT_LIST;
                        break;
                    case LESS_THAN:
                    case LESS_THAN_OR_EQUAL:
                    case EQUALS_EQUALS:
                    case GREATER_THAN:
                    case GREATER_THAN_OR_EQUAL:
                        nextState = ReduceState.REDUCE_TO_COMPARISON_OPERATOR;
                        break;
                    case STATEMENT_LIST:
                        nextState = ReduceState.STATEMENT_LIST;
                        break;
                    case FUNCTION_LIST_WITH_MAIN:
                        if (look == null) {
                            nextState = ReduceState.REDUCE_TO_PROGRAM;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    case CLOSE_CURLY:
                        nextState = ReduceState.CLOSE_CURLY;
                        break;
                    case BLOCK:
                        nextState = ReduceState.BLOCK;
                        break;
                    case WHILE_STATEMENT:
                    case IF_STATEMENT:
                        nextState = ReduceState.REDUCE_TO_BLOCK_STATEMENT;
                        break;
                    case IF_SEGMENT:
                        if (look != null && look.getType() == TokenType.ELSE) {
                            nextState = ReduceState._INVALID;
                        } else {
                            nextState = ReduceState.REDUCE_TO_IF_STATEMENT;
                        }
                        break;
                    case ELSE_SEGMENT:
                        nextState = ReduceState.ELSE_SEGMENT;
                        break;
                    case MINUS:
                        nextState = ReduceState.MINUS;
                        break;
                    case RETURN:
                        nextState = ReduceState.RETURN;
                        break;
                    case VOID:
                        nextState = ReduceState.REDUCE_TO_RETURN_TYPE;
                        break;
                    case OPEN_PARENTHESIS:
                        nextState = ReduceState.OPEN_PARENTHESIS;
                        break;
                    case CLOSE_PARENTHESIS:
                        nextState = ReduceState.CLOSE_PARENTHESIS;
                        break;
                    case FUNCTION_CALL:
                        if (look.getType() == TokenType.SEMICOLON) {
                            nextState = ReduceState.REDUCE_TO_LINE_STATEMENT_BODY;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case IDENTIFIER_LIST:
                switch (node.getType()) {
                    case DATA_TYPE:
                        if (look.getType() != TokenType.COMMA) {
                            nextState = ReduceState.REDUCE_TO_DECLARATION;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    default:
                        nextState = ReduceState._INVALID;

                }
                break;
            case EXPRESSION:
                switch (node.getType()) {
                    case COMPARISON_OPERATOR:
                        nextState = ReduceState.EXPRESSION__COMPARISON_OPERATOR;
                        break;
                    case EQUALS:
                        nextState = ReduceState.EXPRESSION__EQUALS;
                        break;
                    case UNARY_OPERATOR:
                        nextState = ReduceState.REDUCE_TO_EXPRESSION;
                        break;
                    case RETURN:
                        if (look.getType() == TokenType.SEMICOLON) {
                            nextState = ReduceState.REDUCE_TO_RETURN_STATEMENT;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    case BINARY_OPERATOR:
                        nextState = ReduceState.EXPRESSION__BINARY_OPERATOR;
                        break;
                    default:
                       nextState = ReduceState._INVALID;
                       break;
                }
                break;
            case EXPRESSION__BINARY_OPERATOR:
                switch (node.getType()) {
                    case EXPRESSION:
                        nextState = ReduceState.REDUCE_TO_EXPRESSION;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case EXPRESSION__COMPARISON_OPERATOR:
                switch (node.getType()) {
                    case EXPRESSION:
                        nextState = ReduceState.REDUCE_TO_EXPRESSION;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case IDENTIFIER:
                switch (node.getType()) {
                    case DATA_TYPE:
                        if (look.getType() != TokenType.COMMA) {
                            nextState = ReduceState.REDUCE_TO_DECLARATION;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    case COMMA:
                        nextState = ReduceState.IDENTIFIER__COMMA;
                        break;
                    case COMPARISON_OPERATOR:
                    case UNARY_OPERATOR:
                    case BINARY_OPERATOR:
                    case EQUALS:
                    case OPEN_PARENTHESIS:
                    case RETURN:
                        nextState = ReduceState.REDUCE_TO_EXPRESSION_FROM_IDENTIFIER;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case IDENTIFIER__COMMA:
                switch (node.getType()) {
                    case IDENTIFIER:
                    case IDENTIFIER_LIST:
                        nextState = ReduceState.REDUCE_TO_IDENTIFIER_LIST;
                }
                break;
            case EXPRESSION__EQUALS:
                switch (node.getType()) {
                    case IDENTIFIER:
                        if (look.getType() != TokenType.FORWARD_SLASH) { // TODO: this should really be all binary operators
                            nextState = ReduceState.REDUCE_TO_ASSIGNMENT;
                        }
                        break;
                    case DECLARATION:
                        nextState = ReduceState.REDUCE_TO_DECLARATION_ASSIGNMENT;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case SEMICOLON:
                switch (node.getType()) {
                    case LINE_STATEMENT_BODY:
                        nextState = ReduceState.REDUCE_TO_LINE_STATEMENT;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                }
                break;
            case STATEMENT_LIST:
                switch (node.getType()) {
                    case STATEMENT_LIST:
                        nextState = ReduceState.REDUCE_TO_STATEMENT_LIST;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                }
                break;
            case CLOSE_CURLY:
                switch (node.getType()) {
                    case STATEMENT_LIST:
                        nextState = ReduceState.CLOSE_CURLY__STATEMENT_LIST;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                }
                break;
            case CLOSE_CURLY__STATEMENT_LIST:
                switch (node.getType()) {
                    case OPEN_CURLY:
                        nextState = ReduceState.REDUCE_TO_BLOCK;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK:
                switch (node.getType()) {
                    case CLOSE_PARENTHESIS:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS;
                        break;
                    case ELSE:
                        nextState = ReduceState.REDUCE_TO_ELSE_SEGMENT;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS:
                switch (node.getType()) {
                    case EXPRESSION:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__EXPRESSION;
                        break;
                    case PARAMETER:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__PARAMETER;
                        break;
                    case IDENTIFIER:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__IDENTIFIER;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__EXPRESSION:
                switch (node.getType()) {
                    case OPEN_PARENTHESIS:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__EXPRESSION__OPEN_PARENTEHSIS;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__EXPRESSION__OPEN_PARENTEHSIS:
                switch (node.getType()) {
                    case WHILE:
                        nextState = ReduceState.REDUCE_TO_WHILE_STATEMENT;
                        break;
                    case IF:
                        nextState = ReduceState.REDUCE_TO_IF_SEGMENT;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case ELSE_SEGMENT:
                switch (node.getType()) {
                    case IF_SEGMENT:
                        nextState = ReduceState.REDUCE_TO_IF_STATEMENT;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case MINUS:
                switch (node.getType()) {
                    case EXPRESSION:
                        break;
                    case EQUALS:
                        nextState = ReduceState.REDUCE_TO_UNARY_OPERATOR;
                        break;
                }
                break;
            case DOUBLE:
            case INT:
                switch (node.getType()) {
                    case OPEN_CURLY:
                    case STATEMENT_LIST:
                    case OPEN_PARENTHESIS:
                        nextState = ReduceState.REDUCE_TO_DATA_TYPE;
                        break;
                    default:
                        nextState = ReduceState.REDUCE_TO_RETURN_TYPE_FROM_DATA_TYPE;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__PARAMETER:
                switch (node.getType()) {
                    case OPEN_PARENTHESIS:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__PARAMETER__OPEN_PARENTHESIS;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__PARAMETER__OPEN_PARENTHESIS:
                switch (node.getType()) {
                    case IDENTIFIER:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__PARAMETER__OPEN_PARENTHESIS__IDENTIFIER;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__PARAMETER__OPEN_PARENTHESIS__IDENTIFIER:
                switch (node.getType()) {
                    case RETURN_TYPE:
                        nextState = ReduceState.REDUCE_TO_FUNCTION;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case OPEN_PARENTHESIS:
                switch (node.getType()) {
                    case IDENTIFIER:
                        if (look.getType() == TokenType.CLOSE_PARENTHESIS) {
                            nextState = ReduceState.REDUCE_TO_ARGUMENT_LIST_FROM_EMTPY;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case CLOSE_PARENTHESIS:
                switch (node.getType()) {
                    case ARGUMENT_LIST:
                        nextState = ReduceState.CLOSE_PARENTHESIS__ARGUMENT_LIST;
                        break;
                    case EXPRESSION:
                        nextState = ReduceState.CLOSE_PARENTHESIS__EXPRESSION;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case CLOSE_PARENTHESIS__ARGUMENT_LIST:
                switch (node.getType()) {
                    case OPEN_PARENTHESIS:
                        nextState = ReduceState.CLOSE_PARENTHESIS__ARGUMENT_LIST__OPEN_PARENTHESIS;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case CLOSE_PARENTHESIS__ARGUMENT_LIST__OPEN_PARENTHESIS:
                switch (node.getType()) {
                    case IDENTIFIER:
                        nextState = ReduceState.REDUCE_TO_FUNCTION_CALL;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__IDENTIFIER:
                switch (node.getType()) {
                    case CLOSE_SQUARE:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE:
                switch (node.getType()) {
                    case OPEN_SQUARE:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE:
                switch (node.getType()) {
                    case STRING:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING:
                switch (node.getType()) {
                    case OPEN_PARENTHESIS:
                        nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING__OPEN_PARENTHESIS;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING__OPEN_PARENTHESIS:
                switch (node.getType()) {
                    case MAIN:
                         nextState = ReduceState.BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING__OPEN_PARENTHESIS__MAIN;
                         break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case BLOCK__CLOSE_PARENTHESIS__IDENTIFIER__CLOSE_SQUARE__OPEN_SQUARE__STRING__OPEN_PARENTHESIS__MAIN:
                switch (node.getType()) {
                    case RETURN_TYPE:
                        nextState = ReduceState.REDUCE_TO_MAIN_FUNCTION;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;

                }
                break;
            case CLOSE_PARENTHESIS__EXPRESSION:
                switch (node.getType()) {
                    case OPEN_PARENTHESIS:
                        if (look.getType() != TokenType.OPEN_CURLY) {
                            nextState = ReduceState.REDUCE_TO_EXPRESSION;
                        } else {
                            nextState = ReduceState._INVALID;
                        }
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
        }
        return nextState;
    }
}

