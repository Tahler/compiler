package edu.neumont.csc330.tokenizer;

public class Token {
    private TokenLocation location;
    private TokenType type;
    private String value;

    public Token(TokenLocation location, TokenType type, String value) {
        this.location = location;
        this.type = type;
        this.value = value;
    }

    public TokenLocation getLocation() {
        return location;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "location=" + location +
                ", type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
