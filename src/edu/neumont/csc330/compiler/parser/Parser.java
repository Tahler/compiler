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
        List<Node> children = new ArrayList<>();
        while(!roller.isEmpty()) {
            Node node = roller.pop();
            children.add(node);
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
                    // TODO: deal with look ahead
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
                    case ASSIGNMENT:
                        nextState = ReduceState.ASSIGNMENT;
                        break;
                    case DECLARATION_ASSIGNMENT:
                        nextState = ReduceState.REDUCE_TO_LINE_STATEMENT_BODY;
                        break;
                    case SEMICOLON:
                        nextState = ReduceState.SEMICOLON;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
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
            case EXPRESSION__EQUALS:
                switch (node.getType()) {
                    case IDENTIFIER:
                        nextState = ReduceState.REDUCE_TO_ASSIGNMENT;
                        break;
                    default:
                        nextState = ReduceState._INVALID;
                        break;
                }
                break;
            case ASSIGNMENT:
                switch (node.getType()) {
                    case DATA_TYPE:
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

        }
        return nextState;
    }
}

