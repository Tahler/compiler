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
            Token look = (ii < tokens.size()) ? tokens.get(ii + 1) : null;

            NodeType type = NodeType.fromTokenType(curr.getType());

            stack.push(new TokenNode(type, curr));
            reduce(stack, look);
        }

        if (stack.size() == 1) {
            Node node = stack.pop();
                if (node.type == NodeType.PROGRAM) {
                    return node;
                } else {
                    throw new RuntimeException("Tokens could not be reduced to a program node")
                }
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

