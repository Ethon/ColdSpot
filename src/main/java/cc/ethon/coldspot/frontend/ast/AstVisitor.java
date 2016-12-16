package cc.ethon.coldspot.frontend.ast;

public interface AstVisitor<T> {

	public T visit(ClassNode classNode);

	public T visit(MethodNode methodNode);

	public T visit(VariableExpressionNode variableExpressionNode);

	public T visit(StatementBlock statementBlock);

	public T visit(ReturnStatementNode returnStatementNode);

	public T visit(BinaryExpressionNode binaryExpressionNode);

	public T visit(VariableDeclarationStatementNode variableDeclarationNode);

	public T visit(LiteralExpressionNode literalExpressionNode);

	public T visit(AssignmentStatementNode assignmentStatementNode);

	public T visit(IncrementExpressionNode incrementExpressionNode);

	public T visit(DoWhileLoopStatementNode doWhileLoopStatementNode);

	public T visit(ExpressionStatementNode expressionStatementNode);

}
