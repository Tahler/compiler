package edu.neumont.csc330.compiler.parser;

import java.util.ArrayList;
import java.util.List;

class Node {
    List<Node> children;
    NodeType type;

    public Node(NodeType type) {
        this(type, null);
    }

    public Node(NodeType type, List<Node> children) {
        this.children = (children == null) ? new ArrayList<>() : children;
        this.type = type;
    }
}
