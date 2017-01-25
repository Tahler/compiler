package edu.neumont.csc330.compiler.parser;

import edu.neumont.csc330.compiler.tokenizer.Token;
import edu.neumont.csc330.compiler.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
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
            throw new RuntimeException("Tokens could not be reduced to a single node");
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
        NodeType type = NodeType.valueOf(reduceTo);

        // TODO: handle more than simple case
        // TODO: lists should consume the children lists and make their children their own
        List<Node> children = new ArrayList<>();
        switch (type) {
            case STATEMENT_LIST:
            case IDENTIFIER_LIST:
                while(!roller.isEmpty()) {
                    Node node = roller.pop();
                    if (node.getType() == type) {
                        children.addAll(node.getChildren());
                    } else {
                        children.add(node);
                    }
                }
                break;
            default:
                while(!roller.isEmpty()) {
                    Node node = roller.pop();
                    children.add(node);
                }
                break;
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
                    case  INT:
                        nextState = ReduceState.REDUCE_TO_DATA_TYPE;
                        break;
                    case IDENTIFIER:
                        nextState = ReduceState.IDENTIFIER;
                        break;
                    case FLOAT_LITERAL:
                    case INT_LITERAL:
                        nextState = ReduceState.REDUCE_TO_LITERAL;
                        break;
                    case LITERAL:
                        nextState = ReduceState.REDUCE_TO_EXPRESSION;
                        break;
                    case EXPRESSION:
                        nextState = ReduceState.EXPRESSION;
                        break;
                    case DECLARATION_ASSIGNMENT:
                    case ASSIGNMENT:
                        nextState = ReduceState.REDUCE_TO_LINE_STATEMENT_BODY;
                        break;
                    case DECLARATION:
                        if (look.getType() != TokenType.EQUALS) {
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
                    case EQUALS:
                        nextState = ReduceState.EXPRESSION__EQUALS;
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
                        nextState = ReduceState.REDUCE_TO_ASSIGNMENT;
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

        }
        return nextState;
    }
}

