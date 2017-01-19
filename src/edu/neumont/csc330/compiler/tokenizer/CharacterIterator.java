package edu.neumont.csc330.compiler.tokenizer;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;

public class CharacterIterator implements Iterator<Character> {
    private Reader src;
    private Optional<Character> nextChar;

    public CharacterIterator(String path) {
        try {
            this.src = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.nextChar = Optional.empty();
        this.next();
    }

    @Override
    public boolean hasNext() {
        return this.nextChar.isPresent();
    }

    @Override
    public Character next() {
        Character current = this.nextChar.orElse(null);
        int next = this.nextByte();
        this.nextChar = next == -1
                ? Optional.empty()
                : Optional.of((char) next);
        return current;
    }

    private int nextByte() {
        try {
            return this.src.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
