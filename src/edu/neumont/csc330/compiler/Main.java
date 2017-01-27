package edu.neumont.csc330.compiler;

import edu.neumont.csc330.compiler.parser.Parser;
import edu.neumont.csc330.compiler.tokenizer.CharacterIterator;
import edu.neumont.csc330.compiler.tokenizer.Token;
import edu.neumont.csc330.compiler.tokenizer.Tokenizer;
import edu.neumont.csc330.compiler.parser.Node;

import java.util.List;

public class Main {
    public static final String USAGE = "Usage: tkn [input-file]";

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }
        String inputFilePath = args[0];
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.tokenize(new CharacterIterator(inputFilePath));
        System.out.println(tokens);

        Parser parser = new Parser();
        Node rootNode = parser.parse(tokens);
        printTree(rootNode, 0);
    }

    public static void printTree(Node node, int depth) {
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < depth; ii++) {
            sb.append("\t");
        }
        sb.append(node.getType());
        System.out.println(sb.toString());
        List<Node> children = node.getChildren();
        for(Node child : children) {
            printTree(child, depth + 1);
        }
    }

    public static void printUsage() {
        System.err.println(USAGE);
    }
}
