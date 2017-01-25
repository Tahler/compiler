package edu.neumont.csc330.compiler.parser;

import edu.neumont.csc330.compiler.tokenizer.Token;

public class TokenNode extends Node {
    Token token;

    public TokenNode(NodeType type, Token token) {
        super(type);
        this.token = token;
    }
}
