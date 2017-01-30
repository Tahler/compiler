package edu.neumont.csc330.compiler.interpreter;

import edu.neumont.csc330.compiler.parser.Node;
import edu.neumont.csc330.compiler.parser.NodeType;
import edu.neumont.csc330.compiler.parser.TokenNode;

import java.util.*;
import java.util.function.Supplier;

public class Interpreter {
    private Map<String, Supplier<Double>> symbolTable;

    private Node mainBlock;
    private Map<String, List<String>> fnArgs;
    private Map<String, Node> fnBlocks;

    public Interpreter() {
        this.symbolTable = new HashMap<>();
        this.fnArgs = new HashMap<>();
        this.fnBlocks = new HashMap<>();
    }

    public void run(Node node) {
        assert node.getType() == NodeType.PROGRAM;

        List<Node> children = node.getChildren().get(0).getChildren();
        declFunctions(children);
        execMain();
    }

    private void execMain() {
        execBlock(this.mainBlock, null);
    }

    private void declFunctions(List<Node> children) {
        for (Node child : children) {
            if (child.getType() == NodeType.FUNCTION) {
                declFunction(child);
            } else {
                assert child.getType() == NodeType.MAIN_FUNCTION;

                List<Node> mainChildren = child.getChildren();
                this.mainBlock = mainChildren.get(mainChildren.size() - 1);
            }
        }
    }

    private void execWriteLine(Node node) {
        assert node.getType() == NodeType.WRITELINE_CALL;

        Node expr = node.getChildren().get(2).getChildren().get(0);
        System.out.println(evalArithExpr(expr));
    }

    private void execBlock(Node block, String returnName) {
        assert block.getType() == NodeType.BLOCK;

        Node statementList = block.getChildren().get(1);
        List<Node> statements = statementList.getChildren();

        for (Node statement : statements) {
            assert statement.getType() == NodeType.STATEMENT;

            Node subStmt = statement.getChildren().get(0);
            if (subStmt.getType() == NodeType.LINE_STATEMENT) {
                Node stmt = subStmt.getChildren().get(0).getChildren().get(0);
                switch (stmt.getType()) {
                    case DECLARATION_ASSIGNMENT:
                        declAssnVar(stmt);
                        break;
                    case DECLARATION:
                        declVar(stmt);
                        break;
                    case ASSIGNMENT:
                        assnVar(stmt);
                        break;
                    case WRITELINE_CALL:
                        execWriteLine(stmt);
                        break;
                    case RETURN_STATEMENT:
                        Node expr = stmt.getChildren().get(1);
                        double retVal = evalArithExpr(expr);
                        this.symbolTable.put(returnName, () -> retVal);
                        break;
                    case FUNCTION_CALL:
                        execFunctionCall(stmt);
                        break;
                    default:
                        System.out.println(stmt.getType());
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
                        Node whileBlock = stmt.getChildren().get(4);
                        while (evalBoolExpr(expr)) {
                            execBlock(whileBlock, null);
                        }
                        break;
                    }
                    case FOR_STATEMENT: {
                        Node initStmt = stmt.getChildren().get(2).getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0);
                        Node expr = stmt.getChildren().get(3);
                        Node post = stmt.getChildren().get(5);
                        Node forBlock = stmt.getChildren().get(7);
                        for (declAssnVar(initStmt);
                             evalBoolExpr(expr);
                             execPost(post)) {
                            execBlock(forBlock, null);
                        }
                        break;
                    }
                    default:
                        throw new RuntimeException("unreachable!");
                }
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

    private void execIf(Node ifStatement) {
        assert ifStatement.getType() == NodeType.IF_STATEMENT;

        List<Node> segments = ifStatement.getChildren();
        Node ifSeg = segments.get(0);
        Node expr = ifSeg.getChildren().get(2);
        Node ifBlock = ifSeg.getChildren().get(4);
        if (evalBoolExpr(expr)) {
            execBlock(ifBlock, null);
        } else {
            if (segments.size() == 2) {
                Node elseSeg = segments.get(1);
                Node elseBlock = elseSeg.getChildren().get(1);
                execBlock(elseBlock, null);
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
            } else if (part.getType() == NodeType.IDENTIFIER) {
                String id = getTokenString(part);
                value = symbolTable.get(id).get();
            } else {
                assert part.getType() == NodeType.FUNCTION_CALL;
                value = execFunctionCall(part);
            }
        } else if (numParts == 2) {
            Node unaryOp = parts.get(0);
            Node right = parts.get(1);
            // Only unary op is '-'
            value = -evalArithExpr(right);
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
        Node idOrIdList = children.get(1);
        switch (idOrIdList.getType()) {
            case IDENTIFIER: {
                String identifier = getTokenString(children.get(1));
                symbolTable.put(identifier, () -> 0.0);
                break;
            }
            case IDENTIFIER_LIST: {
                List<Node> idList = children.get(1).getChildren();
                for (int i = 0; i < idList.size(); i += 2) {
                    String identifier = getTokenString(idList.get(i));
                    symbolTable.put(identifier, () -> 0.0);
                }
                break;
            }
            default:
                throw new RuntimeException("unreachable!");
        }
    }

    private void assnVar(Node statement) {
        assert statement.getType() == NodeType.ASSIGNMENT;

        List<Node> children = statement.getChildren();
        String identifier = getTokenString(children.get(0));
        Node expr = children.get(2);
        double val = evalArithExpr(expr);
        symbolTable.put(identifier, () -> val);
    }

    private void declFunction(Node node) {
        List<Node> children = node.getChildren();

//        NodeType returnType = children.get(0).getChildren().get(0).getType();
//
//        switch (returnType) {
//            case VOID:
//                break;
//            case DOUBLE:
//                break;
//        }
        String id = getTokenString(children.get(1));
        Node parameter = children.get(3);
        List<String> argsList = parameter.getChildren().size() == 0
                ? Collections.emptyList()
                : Collections.singletonList(getTokenString(parameter.getChildren().get(0).getChildren().get(1)));
        this.fnArgs.put(id, argsList);

        Node block = children.get(5);
        this.fnBlocks.put(id, block);
    }

    private double execFunctionCall(Node node) {
        List<Node> children = node.getChildren();

        // Set the arg names,
        String id = getTokenString(children.get(0));
        List<Node> args = children.get(2).getChildren();
        List<String> params = this.fnArgs.get(id);
        for (int i = 0; i < args.size(); i++) {
            String paramName = params.get(i);
            Node argExpr = args.get(i);
            double argVal = evalArithExpr(argExpr);
            this.symbolTable.put(paramName, () -> argVal);
        }
        Node block = this.fnBlocks.get(id);
        execBlock(block, id);
        return this.symbolTable.containsKey(id)
                ? this.symbolTable.get(id).get()
                : Double.MAX_VALUE;
    }

    private static String getTokenString(Node node) {
        assert node instanceof TokenNode;

        return ((TokenNode) node).getToken().getValue();
    }
}
