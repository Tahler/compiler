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
        System.out.println(evalExpr(expr));
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
            throw new RuntimeException("unimplemented!");
            // TODO:
        }
    }

    private double evalExpr(Node expr) {
        assert expr.getType() == NodeType.EXPRESSION;

        double value = Double.MAX_VALUE;
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
        }
        // TODO: things like i + 3
        // children.size is 3 and not parens -> evalExpr binOp evalExpr
        // children.size is 3 and parens -> evalExpr children[1]
        return value;
    }

    private void declAssnVar(Node statement) {
        assert statement.getType() == NodeType.DECLARATION_ASSIGNMENT;

        List<Node> children = statement.getChildren();

        // data type is double. always
        String identifier = getTokenString(children.get(0).getChildren().get(1));
        Node expr = children.get(2);
        symbolTable.put(identifier, () -> evalExpr(expr));
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
        symbolTable.put(identifier, () -> evalExpr(expr));
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
