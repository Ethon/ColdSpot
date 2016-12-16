package cc.ethon.coldspot.frontend.ast;

import java.util.List;

public class AbstractAstVisitor<T> implements AstVisitor<T> {

	private void accept(List<? extends AstNode> nodes) {
		for (final AstNode astNode : nodes) {
			astNode.accept(this);
		}
	}

	@Override
	public T visit(ClassNode classNode) {
		accept(classNode.getPrivateMethods());
		accept(classNode.getProtectedMethods());
		accept(classNode.getPublicMethods());
		return null;
	}

	@Override
	public T visit(MethodNode methodNode) {
		methodNode.getBody().accept(this);
		return null;
	}

	@Override
	public T visit(VariableExpressionNode variableExpressionNode) {
		return null;
	}

	@Override
	public T visit(StatementBlock statementBlock) {
		accept(statementBlock.getStatements());
		return null;
	}

	@Override
	public T visit(ReturnStatementNode returnStatementNode) {
		if (returnStatementNode.getResult().isPresent()) {
			returnStatementNode.getResult().get().accept(this);
		}
		return null;
	}

	@Override
	public T visit(BinaryExpressionNode binaryExpressionNode) {
		binaryExpressionNode.getLeft().accept(this);
		binaryExpressionNode.getRight().accept(this);
		return null;
	}

	@Override
	public T visit(VariableDeclarationStatementNode variableDeclarationNode) {
		if (variableDeclarationNode.getValue().isPresent()) {
			variableDeclarationNode.getValue().get().accept(this);
		}
		return null;
	}

	@Override
	public T visit(LiteralExpressionNode literalExpressionNode) {
		return null;
	}

	@Override
	public T visit(AssignmentStatementNode assignmentStatementNode) {
		assignmentStatementNode.getLeft().accept(this);
		assignmentStatementNode.getRight().accept(this);
		return null;
	}

	@Override
	public T visit(IncrementExpressionNode incrementExpressionNode) {
		return null;
	}

	@Override
	public T visit(DoWhileLoopStatementNode doWhileLoopStatementNode) {
		doWhileLoopStatementNode.getCondition().accept(this);
		doWhileLoopStatementNode.getBody().accept(this);
		return null;
	}

	@Override
	public T visit(ExpressionStatementNode expressionStatementNode) {
		return expressionStatementNode.getExpression().accept(this);
	}

}
