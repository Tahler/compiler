package edu.neumont.csc330.tokenizer;

public class Main {
    public static final String USAGE = "Usage: tkn [input-file]";

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }
        String inputFilePath = args[1];
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.tokenize(new CharacterIterator(inputFilePath));
    }

    public static void printUsage() {
        System.err.println(USAGE);
    }
}
