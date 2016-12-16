package cc.ethon.coldspot.frontend.ast;

public class ExpressionStatementNode extends StatementNode {

	private final ExpressionNode expression;

	public ExpressionStatementNode(int instructionIndex, ExpressionNode expression) {
		super(instructionIndex);
		this.expression = expression;
	}

	public ExpressionNode getExpression() {
		return expression;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return expression.toString();
	}

}
