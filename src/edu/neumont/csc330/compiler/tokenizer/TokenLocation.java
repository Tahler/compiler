package edu.neumont.csc330.compiler.tokenizer;

public class TokenLocation {
    private int lineNumber;
    private int columnNumber;

    public TokenLocation(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return "TokenLocation{" +
                "lineNumber=" + lineNumber +
                ", columnNumber=" + columnNumber +
                '}';
    }
}
