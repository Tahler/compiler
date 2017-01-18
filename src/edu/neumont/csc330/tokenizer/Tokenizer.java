package edu.neumont.csc330.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Tokenizer {
    private static final Character[] ARITHMETIC_OPERATORS = new Character[] {
            '+',
            '-',
            '*',
            '/',
    };

    private static final Character[] BRACES = new Character[] {
            '{',
            '}',
            '(',
            ')',
            '[',
            ']',
    };

    private static final Character[] SPECIAL_CHARACTERS = new Character[] {
            ';',
            ',',
    };

    private static final Character[] WHITESPACE_CHARACTERS = new Character[] {
            ' ',
            '\t',
            '\n',
            '\r',
    };

    public Tokenizer() {

    }

    public List<Token> tokenize(Iterator<Character> iterator) {
        List<Token> tokens = new ArrayList<>();
        State currentState = State._IDLE;
        int lineNumber = 0, tokenColumnStart = 0, column = -1;
        while (iterator.hasNext()) {
            Character nextChar = iterator.next();
            if (isStartOfNextToken(currentState, nextChar)) {
                // flush current -- move to idle
            }

            switch (currentState) {
                case D:
                    break;
                case DO:
                    break;
                case DOU:
                    break;
                case DOUB:
                    break;
                case DOUBL:
                    break;
                case DOUBLE:
                    break;
                case E:
                    break;
                case EL:
                    break;
                case ELS:
                    break;
                case ELSE:
                    break;
                case F:
                    break;
                case FO:
                    break;
                case FOR:
                    break;
                case I:
                    break;
                case IF:
                    break;
                case IN:
                    break;
                case INT:
                    break;
                case P:
                    break;
                case PU:
                    break;
                case PUB:
                    break;
                case PUBL:
                    break;
                case PUBLI:
                    break;
                case PUBLIC:
                    break;
                case PR:
                    break;
                case PRI:
                    break;
                case PRIV:
                    break;
                case PRIVA:
                    break;
                case PRIVAT:
                    break;
                case PRIVATE:
                    break;
                case R:
                    break;
                case RE:
                    break;
                case RET:
                    break;
                case RETU:
                    break;
                case RETUR:
                    break;
                case RETURN:
                    break;
                case S:
                    break;
                case ST:
                    break;
                case STR:
                    break;
                case STRI:
                    break;
                case STRIN:
                    break;
                case STRING:
                    break;
                case V:
                    break;
                case VO:
                    break;
                case VOI:
                    break;
                case VOID:
                    break;
                case W:
                    break;
                case WH:
                    break;
                case WHI:
                    break;
                case WHIL:
                    break;
                case WHILE:
                    break;
                case _EQUALS:
                    break;
                case _EQUALS_EQUALS:
                    break;
                case _NOT:
                    break;
                case _NOT_EQUALS:
                    break;
                case _LESS_THAN:
                    break;
                case _LESS_THAN_EQUAL:
                    break;
                case _GREATER_THAN:
                    break;
                case _GREATER_THAN_EQUAL:
                    break;
                case _OPEN_PARENTHESIS:
                    break;
                case _CLOSE_PARENTHESIS:
                    break;
                case _OPEN_CURLY:
                    break;
                case _CLOSE_CURLY:
                    break;
                case _OPEN_SQUARE:
                    break;
                case _CLOSE_SQUARE:
                    break;
                case _INT_LITERAL:
                    break;
                case _FLOAT_LITERAL:
                    break;
                case _COMMA:
                    break;
                case _SEMICOLON:
                    break;
                case _PLUS:
                    break;
                case _MINUS:
                    break;
                case _ASTERISK:
                    break;
                case _FORWARD_SLASH:
                    break;
                case _PARTIAL_STRING_LITERAL:
                    break;
                case _INVALID: // NUMBER INTO LETTER
                    break;
                case _IDENTIFIER:
                    break;
            }
        }
        return tokens;
    }

    private boolean isStartOfNextToken(State currentState, Character nextChar) {
        if (isStartingChar(nextChar)) {
            return true;
        }

        boolean shouldTerminate;
        switch (nextChar) {
            case '=':
                // equals will start next token if NOT preceded by an equals, lt, or gt sign
                shouldTerminate = currentState != State._EQUALS
                        && currentState != State._LESS_THAN
                        && currentState != State._GREATER_THAN;
                break;
            case '"':
                // a quote will start next token if NOT currently in the _PARTIAL_STRING_LITERAL state
                shouldTerminate = currentState != State._PARTIAL_STRING_LITERAL;
                break;
            default:
                shouldTerminate = false;
                break;
        }
        return shouldTerminate;
    }

    /**
     * Returns true if and only if the character DEFINITELY starts a new token.
     */
    private boolean isStartingChar(Character character) {
        return isWhitespace(character)
                || isArithmeticOperator(character)
                || isBrace(character)
                || isSpecial(character)
                || character == '<'
                || character == '>';
    }

    private boolean isWhitespace(Character character) {
        return Arrays.stream(WHITESPACE_CHARACTERS).anyMatch(c -> c.equals(character));
    }

    private boolean isArithmeticOperator(Character character) {
        return Arrays.stream(ARITHMETIC_OPERATORS).anyMatch(c -> c.equals(character));
    }

    private boolean isBrace(Character character) {
        return Arrays.stream(BRACES).anyMatch(c -> c.equals(character));
    }

    private boolean isSpecial(Character character) {
        return Arrays.stream(SPECIAL_CHARACTERS).anyMatch(c -> c.equals(character));
    }
}

enum State {
    D,
    DO,
    DOU,
    DOUB,
    DOUBL,
    DOUBLE,
    E,
    EL,
    ELS,
    ELSE,
    F,
    FO,
    FOR,
    I,
    IF,
    IN,
    INT,
    P,
    PR,
    PRI,
    PRIV,
    PRIVA,
    PRIVAT,
    PRIVATE,
    PU,
    PUB,
    PUBL,
    PUBLI,
    PUBLIC,
    R,
    RE,
    RET,
    RETU,
    RETUR,
    RETURN,
    S,
    ST,
    STR,
    STRI,
    STRIN,
    STRING,
    V,
    VO,
    VOI,
    VOID,
    W,
    WH,
    WHI,
    WHIL,
    WHILE,

    _EQUALS,
    _EQUALS_EQUALS,
    _NOT,
    _NOT_EQUALS,
    _LESS_THAN,
    _LESS_THAN_EQUAL,
    _GREATER_THAN,
    _GREATER_THAN_EQUAL,

    _OPEN_PARENTHESIS,
    _CLOSE_PARENTHESIS,
    _OPEN_CURLY,
    _CLOSE_CURLY,
    _OPEN_SQUARE,
    _CLOSE_SQUARE,

    _INT_LITERAL,
    _FLOAT_LITERAL,
    _PARTIAL_STRING_LITERAL,

    _COMMA,
    _SEMICOLON,

    _PLUS,
    _MINUS,
    _ASTERISK,
    _FORWARD_SLASH,

    _INVALID,

    _IDLE,

    _IDENTIFIER,
}
