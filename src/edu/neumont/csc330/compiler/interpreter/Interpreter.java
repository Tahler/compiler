package edu.neumont.csc330.compiler.interpreter;

import edu.neumont.csc330.compiler.parser.Node;
import edu.neumont.csc330.compiler.parser.NodeType;
import edu.neumont.csc330.compiler.parser.TokenNode;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Interpreter {
    // TODO: consider a backlog of functions? that way they can be called again? or evaluate main after everything?
    private HashMap<String, Supplier<Double>> symbolTable;

    public Interpreter() {
        this.symbolTable = new HashMap<>();
    }

    public void run(Node node) {
        NodeType type = node.getType();
        List<Node> children = node.getChildren();

        switch (type) {
            case PROGRAM:
                // TODO:
                // Find functions before executing. Create a list of functions with a list of parameter IDs
                // When calling a function, symbolTable.put(paramList[i], arg)
//                break;
            case FUNCTION_LIST_WITH_MAIN:
            case FUNCTION_LIST:
                children.forEach(this::run);
                break;

            case MAIN_FUNCTION:
                Node block = children.get(children.size() - 1);
                execBlock(block);
                break;

            case FUNCTION:
                // TODO: put a `() -> execBlock` in the symbol table
                break;

//            case FUNCTION_CALL:
//                // TODO: set the IDs of the parameters in the original func decl to the passed in vals
//                execFunctionCall(node);
//                break;
//
            default:
                throw new RuntimeException("unreachable!");
        }
    }

    private void execWriteLine(Node node) {
        assert node.getType() == NodeType.WRITELINE_CALL;

        Node expr = node.getChildren().get(2).getChildren().get(0);
        System.out.println(evalArithExpr(expr));
    }

    private void execBlock(Node block) {
        assert block.getType() == NodeType.BLOCK;

        Node statementList = block.getChildren().get(1);
        List<Node> statements = statementList.getChildren();
        statements.forEach(this::execStatement);
    }

    private void execStatement(Node statement) {
        assert statement.getType() == NodeType.STATEMENT;

        Node subStmt = statement.getChildren().get(0);
        if (subStmt.getType() == NodeType.LINE_STATEMENT) {
            Node stmt = subStmt.getChildren().get(0).getChildren().get(0);
            switch (stmt.getType()) {
                case DECLARATION_ASSIGNMENT:
                    declAssnVar(stmt);
                    break;
                case ASSIGNMENT:
                    assnVar(stmt);
                    break;
                case WRITELINE_CALL:
                    execWriteLine(stmt);
                    break;
                default:
                    throw new RuntimeException("unimpl!");
            }
        } else {
            assert subStmt.getType() == NodeType.BLOCK_STATEMENT;

            Node stmt = subStmt.getChildren().get(0);
            switch (stmt.getType()) {
                case IF_STATEMENT: {
                    execIf(stmt);
                    break;
                }
                case WHILE_STATEMENT: {
                    Node expr = stmt.getChildren().get(2);
                    Node block = stmt.getChildren().get(4);
                    while (evalBoolExpr(expr)) {
                        execBlock(block);
                    }
                    break;
                }
                case FOR_STATEMENT: {
                    Node expr = stmt.getChildren().get(3);
                    Node post = stmt.getChildren().get(5);
                    Node block = stmt.getChildren().get(7);
                    for (stmt.getChildren().get(2).getChildren().forEach(this::execStatement);
                         evalBoolExpr(expr);
                         execPost(post)) {
                        execBlock(block);
                    }
                    break;
                }
                default:
                    throw new RuntimeException("unreachable!");
            }
        }
    }

    private void execPost(Node post) {
        // only supports identifier++

        String id = getTokenString(post.getChildren().get(0));
        double curr = symbolTable.get(id).get();
        double inc = curr + 1;
        symbolTable.put(id, () -> inc);
    }

    private void execLineStatementBody(Node subStmt) {

    }

    private void execIf(Node ifStatement) {
        assert ifStatement.getType() == NodeType.IF_STATEMENT;

        List<Node> segments = ifStatement.getChildren();
        Node ifSeg = segments.get(0);
        Node expr = ifSeg.getChildren().get(2);
        Node ifBlock = ifSeg.getChildren().get(4);
        if (evalBoolExpr(expr)) {
            execBlock(ifBlock);
        } else {
            if (segments.size() == 2) {
                Node elseSeg = segments.get(1);
                Node elseBlock = elseSeg.getChildren().get(1);
                execBlock(elseBlock);
            }
        }
    }

    private boolean evalBoolExpr(Node expr) {
        assert expr.getType() == NodeType.EXPRESSION;

        List<Node> parts = expr.getChildren();
        assert parts.size() == 3;

        Node left = parts.get(0);
        Node op = parts.get(1).getChildren().get(0);
        Node right = parts.get(2);

        boolean value;
        switch (op.getType()) {
            case LESS_THAN:
                value = evalArithExpr(left) < evalArithExpr(right);
                break;
            case LESS_THAN_OR_EQUAL:
                value = evalArithExpr(left) <= evalArithExpr(right);
                break;
            case GREATER_THAN:
                value = evalArithExpr(left) > evalArithExpr(right);
                break;
            case GREATER_THAN_OR_EQUAL:
                value = evalArithExpr(left) >= evalArithExpr(right);
                break;
            case EQUALS_EQUALS:
                value = evalArithExpr(left) == evalArithExpr(right);
                break;
            case NOT_EQUALS:
                value = evalArithExpr(left) != evalArithExpr(right);
                break;

            default:
                throw new RuntimeException("unreachable!");
        }
        return value;
    }

    private double evalArithExpr(Node expr) {
        // TODO: extract
        if (expr.getType() == NodeType.IDENTIFIER) {
            String id = getTokenString(expr);
            return symbolTable.get(id).get();
        }

        assert expr.getType() == NodeType.EXPRESSION;

        double value;
        List<Node> parts = expr.getChildren();
        int numParts = parts.size();
        if (numParts == 1) {
            Node part = parts.get(0);
            if (part.getType() == NodeType.LITERAL) {
                String literal = getTokenString(part.getChildren().get(0));
                value = Double.parseDouble(literal);
            } else {
                assert part.getType() == NodeType.IDENTIFIER;

                String id = getTokenString(part);
                value = symbolTable.get(id).get();
            }
        } else if (numParts == 3) {
            Node inner = parts.get(1);
            if (parts.get(0).getType() == NodeType.OPEN_PARENTHESIS) {
                assert parts.get(2).getType() == NodeType.CLOSE_PARENTHESIS;
                assert inner.getType() == NodeType.EXPRESSION;

                value = evalArithExpr(inner);
            } else {
                assert inner.getType() == NodeType.BINARY_OPERATOR;

                Node left = parts.get(0);
                Node right = parts.get(2);
                Node op = inner.getChildren().get(0);
                switch (op.getType()) {
                    case PLUS:
                        value = evalArithExpr(left) + evalArithExpr(right);
                        break;
                    case MINUS:
                        value = evalArithExpr(left) - evalArithExpr(right);
                        break;
                    case ASTERISK:
                        value = evalArithExpr(left) * evalArithExpr(right);
                        break;
                    case FORWARD_SLASH:
                        value = evalArithExpr(left) / evalArithExpr(right);
                        break;
                    default:
                        throw new RuntimeException("unreachable!");
                }
            }
        } else {
            throw new RuntimeException("unreachable!");
        }
        return value;
    }

    private void declAssnVar(Node statement) {
        assert statement.getType() == NodeType.DECLARATION_ASSIGNMENT;

        List<Node> children = statement.getChildren();

        // data type is double. always
        String identifier = getTokenString(children.get(0).getChildren().get(1));
        Node expr = children.get(2);
        symbolTable.put(identifier, () -> evalArithExpr(expr));
    }

    private void declVar(Node statement) {
        assert statement.getType() == NodeType.DECLARATION;

        List<Node> children = statement.getChildren();
        String identifier = getTokenString(children.get(1));
        symbolTable.put(identifier, () -> 0.0);
    }

    private void assnVar(Node statement) {
        assert statement.getType() == NodeType.ASSIGNMENT;

        List<Node> children = statement.getChildren();
        String identifier = getTokenString(children.get(0));
        Node expr = children.get(2);
        symbolTable.put(identifier, () -> evalArithExpr(expr));
    }

    private void declFunction(Node node) {
        List<Node> children = node.getChildren();

        NodeType returnType = children.get(0).getChildren().get(0).getType();

        switch (returnType) {
            case VOID:
                break;
            case DOUBLE:
                break;
        }
        String identifier = getTokenString(children.get(1));
        // skip open paren
        Node parameter = children.get(3);
        // skip close paren
        Node block = children.get(5);

//        symbolTable.put(identifier, new Symbol(identifier));
    }

    private void execFunctionCall(Node node) {
        List<Node> children = node.getChildren();

//        this.symbolTable.get()
    }

    private static String getTokenString(Node node) {
        assert node instanceof TokenNode;

        return ((TokenNode) node).getToken().getValue();
    }
}

// ExpressionNode : Node
//  getValue()
//      recursively flip through children and call getValue
