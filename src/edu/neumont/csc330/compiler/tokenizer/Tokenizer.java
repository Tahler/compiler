package edu.neumont.csc330.compiler.tokenizer;

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
        int tokenColumnStart = 0;
        int lineNumber = 1;
        int columnNumber = 0;
        StringBuilder currentToken = new StringBuilder();
        while (iterator.hasNext()) {
            Character nextChar = iterator.next();
            if (currentState != State._IDLE && nextCharTerminatesCurrentToken(currentState, nextChar)) {
                TokenLocation location = new TokenLocation(lineNumber, tokenColumnStart);
                TokenType type = TokenType.fromState(currentState);
                String value = currentToken.toString();
                Token token = new Token(location, type, value);
                if (token.getType() != TokenType.TRASH_WORD) {
                    tokens.add(token);
                }
                currentToken = new StringBuilder();
                currentState = State._IDLE;
            }

            if (currentState == State._IDLE) {
                tokenColumnStart = columnNumber;
            }

            switch (currentState) {
                case D:
                    if (nextChar == 'o') {
                        currentState = State.DO;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case DO:
                    if (nextChar == 'u') {
                        currentState = State.DOU;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case DOU:
                    if (nextChar == 'b') {
                        currentState = State.DOUB;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case DOUB:
                    if (nextChar == 'l') {
                        currentState = State.DOUBL;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case DOUBL:
                    if (nextChar == 'e') {
                        currentState = State.DOUBLE;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case DOUBLE:
                    currentState = State._IDENTIFIER;
                    break;
                case E:
                    if (nextChar == 'l') {
                        currentState = State.EL;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case EL:
                    if (nextChar == 's') {
                        currentState = State.ELS;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case ELS:
                    if (nextChar == 'e') {
                        currentState = State.ELSE;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case ELSE:
                    currentState = State._IDENTIFIER;
                    break;
                case F:
                    if (nextChar == 'o') {
                        currentState = State.FO;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case FO:
                    if (nextChar == 'r') {
                        currentState = State.FOR;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case FOR:
                    currentState = State._IDENTIFIER;
                    break;
                case I:
                    if (nextChar == 'f') {
                        currentState = State.IF;
                    } else if (nextChar == 'n') {
                        currentState = State.IN;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case IF:
                    currentState = State._IDENTIFIER;
                    break;
                case IN:
                    if (nextChar == 't') {
                        currentState = State.INT;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case INT:
                    currentState = State._IDENTIFIER;
                    break;
                case M:

                    if (nextChar == 'a') {
                        currentState = State.MA;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case MA:
                    if (nextChar == 'i') {
                        currentState = State.MAI;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case MAI:
                    if (nextChar == 'n') {
                        currentState = State.MAIN;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case MAIN:
                    currentState = State._IDENTIFIER;
                    break;
                case P:
                    if (nextChar == 'u') {
                        currentState = State.PU;
                    } else if (nextChar == 'r') {
                        currentState = State.PR;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PU:
                    if (nextChar == 'b') {
                        currentState = State.PUB;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PUB:
                    if (nextChar == 'l') {
                        currentState = State.PUBL;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PUBL:
                    if (nextChar == 'i') {
                        currentState = State.PUBLI;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PUBLI:
                    if (nextChar == 'c') {
                        currentState = State.PUBLIC;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PUBLIC:
                    currentState = State._IDENTIFIER;
                    break;
                case PR:
                    if (nextChar == 'i') {
                        currentState = State.PRI;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PRI:
                    if (nextChar == 'v') {
                        currentState = State.PRIV;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PRIV:
                    if (nextChar == 'a') {
                        currentState = State.PRIVA;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PRIVA:
                    if (nextChar == 't') {
                        currentState = State.PRIVAT;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PRIVAT:
                    if (nextChar == 'e') {
                        currentState = State.PRIVATE;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case PRIVATE:
                    currentState = State._IDENTIFIER;
                    break;
                case R:
                    if (nextChar == 'e') {
                        currentState = State.RE;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case RE:
                    if (nextChar == 't') {
                        currentState = State.RET;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case RET:
                    if (nextChar == 'u') {
                        currentState = State.RETU;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case RETU:
                    if (nextChar == 'r') {
                        currentState = State.RETUR;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case RETUR:
                    if (nextChar == 'n') {
                        currentState = State.RETURN;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case RETURN:
                    currentState = State._IDENTIFIER;
                    break;
                case S:
                    if (nextChar == 't') {
                        currentState = State.ST;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case ST:
                    if (nextChar == 'r') {
                        currentState = State.STR;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case STR:
                    if (nextChar == 'i') {
                        currentState = State.STRI;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case STRI:
                    if (nextChar == 'n') {
                        currentState = State.STRIN;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case STRIN:
                    if (nextChar == 'g') {
                        currentState = State.STRING;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case STRING:
                    currentState = State._IDENTIFIER;
                    break;
                case V:
                    if (nextChar == 'o') {
                        currentState = State.VO;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case VO:
                    if (nextChar == 'i') {
                        currentState = State.VOI;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case VOI:
                    if (nextChar == 'd') {
                        currentState = State.VOID;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case VOID:
                    currentState = State._IDENTIFIER;
                    break;
                case W:
                    if (nextChar == 'h') {
                        currentState = State.WH;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case WH:
                    if (nextChar == 'i') {
                        currentState = State.WHI;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case WHI:
                    if (nextChar == 'l') {
                        currentState = State.WHIL;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case WHIL:
                    if (nextChar == 'e') {
                        currentState = State.WHILE;
                    } else {
                        currentState = State._IDENTIFIER;
                    }
                    break;
                case WHILE:
                    currentState = State._IDENTIFIER;
                    break;
                case _EQUALS:
                    if (nextChar == '=') {
                        currentState = State._EQUALS_EQUALS;
                    } else {
                        currentState = State._INVALID;
                    }
                    break;
                case _EQUALS_EQUALS:
                    currentState = State._INVALID;
                    break;
                case _NOT:
                    if (nextChar == '=') {
                        currentState = State._NOT_EQUALS;
                    } else {
                        currentState = State._INVALID;
                    }
                    break;
                case _NOT_EQUALS:
                    currentState = State._INVALID;
                    break;
                case _LESS_THAN:
                    if (nextChar == '=') {
                        currentState = State._LESS_THAN_EQUAL;
                    } else {
                        currentState = State._INVALID;
                    }
                    break;
                case _LESS_THAN_EQUAL:
                    currentState = State._INVALID;
                    break;
                case _GREATER_THAN:
                    if (nextChar == '=') {
                        currentState = State._GREATER_THAN_EQUAL;
                    } else {
                        currentState = State._INVALID;
                    }
                    break;
                case _GREATER_THAN_EQUAL:
                    currentState = State._INVALID;
                    break;
                case _OPEN_PARENTHESIS:
                    currentState = State._INVALID;
                    break;
                case _CLOSE_PARENTHESIS:
                    currentState = State._INVALID;
                    break;
                case _OPEN_CURLY:
                    currentState = State._INVALID;
                    break;
                case _CLOSE_CURLY:
                    currentState = State._INVALID;
                    break;
                case _OPEN_SQUARE:
                    currentState = State._INVALID;
                    break;
                case _CLOSE_SQUARE:
                    currentState = State._INVALID;
                    break;
                case _INT_LITERAL:
                    if (nextChar == '.') {
                        currentState = State._FLOAT_LITERAL;
                    } else if (!Character.isDigit(nextChar)) {
                        currentState = State._INVALID;
                    }
                    break;
                case _FLOAT_LITERAL:
                    if (!Character.isDigit(nextChar)) {
                        currentState = State._INVALID;
                    }
                    break;
                case _COMMA:
                    currentState = State._INVALID;
                    break;
                case _SEMICOLON:
                    currentState = State._INVALID;
                    break;
                case _PLUS:
                    if (nextChar == '+') {
                        currentState = State._PLUS_PLUS;
                    } else {
                        currentState = State._INVALID;
                    }
                    break;
                case _PLUS_PLUS:
                    currentState = State._INVALID;
                    break;
                case _MINUS:
                    currentState = State._INVALID;
                    break;
                case _ASTERISK:
                    currentState = State._INVALID;
                    break;
                case _FORWARD_SLASH:
                    currentState = State._INVALID;
                    break;
                case _PARTIAL_STRING_LITERAL:
                    if (nextChar == '"') {
                        currentState = State._STRING_LITERAL;
                    }
                    break;
                case _STRING_LITERAL:
                    currentState = State._INVALID;
                    break;
                case _INVALID: // NUMBER INTO LETTER
                    currentState = State._INVALID;
                    break;
                case _IDLE:
                    switch (nextChar) {
                        case 'd':
                            currentState = State.D;
                            break;
                        case 'e':
                            currentState = State.E;
                            break;
                        case 'f':
                            currentState = State.F;
                            break;
                        case 'i':
                            currentState = State.I;
                            break;
                        case 'M':
                            currentState = State.M;
                            break;
                        case 'p':
                            currentState = State.P;
                            break;
                        case 'r':
                            currentState = State.R;
                            break;
                        case 's':
                            currentState = State.S;
                            break;
                        case 'v':
                            currentState = State.V;
                            break;
                        case 'w':
                            currentState = State.W;
                            break;
                        case '=':
                            currentState = State._EQUALS;
                            break;
                        case '{':
                            currentState = State._OPEN_CURLY;
                            break;
                        case '}':
                            currentState = State._CLOSE_CURLY;
                            break;
                        case '[':
                            currentState = State._OPEN_SQUARE;
                            break;
                        case ']':
                            currentState = State._CLOSE_SQUARE;
                            break;
                        case '(':
                            currentState = State._OPEN_PARENTHESIS;
                            break;
                        case ')':
                            currentState = State._CLOSE_PARENTHESIS;
                            break;
                        case '*':
                            currentState = State._ASTERISK;
                            break;
                        case '+':
                            currentState = State._PLUS;
                            break;
                        case '/':
                            currentState = State._FORWARD_SLASH;
                            break;
                        case '-':
                            currentState = State._MINUS;
                            break;
                        case '!':
                            currentState = State._NOT;
                            break;
                        case '>':
                            currentState = State._GREATER_THAN;
                            break;
                        case '<':
                            currentState = State._LESS_THAN;
                            break;
                        case ',':
                            currentState = State._COMMA;
                            break;
                        case ';':
                            currentState = State._SEMICOLON;
                            break;
                        case '"':
                            currentState = State._PARTIAL_STRING_LITERAL;
                            break;
                        default:
                            if (Character.isDigit(nextChar)) {
                                currentState = State._INT_LITERAL;
                            } else if (Character.isAlphabetic(nextChar)) {
                                currentState = State._IDENTIFIER;
                            } else {
                                if (nextChar == '\n') {
                                    lineNumber++;
                                    columnNumber = 0;
                                }
                                currentState = State._IDLE;
                            }
                            break;
                    }
                    break;
                case _IDENTIFIER:
                    // TODO: same as String? Nothing should cancel me if i got here to begin with
                    break;
            }

            if (currentState != State._IDLE) {
                currentToken.append(nextChar);
            }
            columnNumber++;
        }
        return tokens;
    }

    private boolean nextCharTerminatesCurrentToken(State currentState, Character nextChar) {
        // always allow characters while reading a string literal
        if (currentState == State._PARTIAL_STRING_LITERAL) {
            return false;
        }
        // always terminate after finding a string
        if (currentState == State._STRING_LITERAL) {
            return true;
        }
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
            case '+':
                // a plus will start next token if NOT after another plus
                shouldTerminate = currentState != State._PLUS;
                break;
            default:
                // other characters will start next token if an ending special char state
                shouldTerminate = currentState == State._EQUALS
                        || currentState == State._EQUALS_EQUALS
                        || currentState == State._LESS_THAN
                        || currentState == State._LESS_THAN_EQUAL
                        || currentState == State._GREATER_THAN
                        || currentState == State._GREATER_THAN_EQUAL
                        || currentState == State._PLUS
                        || currentState == State._MINUS
                        || currentState == State._ASTERISK
                        || currentState == State._FORWARD_SLASH
                        || currentState == State._COMMA
                        || currentState == State._SEMICOLON
                        || currentState == State._OPEN_CURLY
                        || currentState == State._CLOSE_CURLY
                        || currentState == State._OPEN_PARENTHESIS
                        || currentState == State._CLOSE_PARENTHESIS
                        || currentState == State._OPEN_SQUARE
                        || currentState == State._CLOSE_SQUARE;
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
    M,
    MA,
    MAI,
    MAIN,
    P,
    PU,
    PUB,
    PUBL,
    PUBLI,
    PUBLIC,
    PR,
    PRI,
    PRIV,
    PRIVA,
    PRIVAT,
    PRIVATE,
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
    _STRING_LITERAL,

    _COMMA,
    _SEMICOLON,

    _PLUS,
    _MINUS,
    _ASTERISK,
    _FORWARD_SLASH,

    _PLUS_PLUS,

    _INVALID,

    _IDLE,

    _IDENTIFIER,
}
