package edu.neumont.csc330.tokenizer;

import java.util.Arrays;
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
    }

    public static void printUsage() {
        System.err.println(USAGE);
    }
}
