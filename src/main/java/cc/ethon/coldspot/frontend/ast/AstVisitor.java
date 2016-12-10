package cc.ethon.coldspot.frontend.ast;

public interface AstVisitor<T> {

	public T visit(ClassNode classNode);

	public T visit(MethodNode methodNode);

	public T accept(VariableExpressionNode variableExpressionNode);

	public T accept(StatementBlock statementBlock);

	public T accept(ReturnStatementNode returnStatementNode);

	public T accept(BinaryExpressionNode binaryExpressionNode);

	public T accept(VariableDeclarationStatementNode variableDeclarationNode);

}
