package edu.neumont.csc330.tokenizer;

public class TokenLocation {
    private String fileName;
    private int lineNumber;
    private int columnNumber;

    public TokenLocation(String fileName, int lineNumber, int columnNumber) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
