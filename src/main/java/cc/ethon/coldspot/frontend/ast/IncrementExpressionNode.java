package cc.ethon.coldspot.frontend.ast;

public class IncrementExpressionNode extends ExpressionNode {

	private final VariableDeclarationStatementNode toIncrement;
	private final int incrementBy;

	public IncrementExpressionNode(int instructionIndex, VariableDeclarationStatementNode toIncrement, int incrementBy) {
		super(instructionIndex);
		this.toIncrement = toIncrement;
		this.incrementBy = incrementBy;
	}

	public VariableDeclarationStatementNode getToIncrement() {
		return toIncrement;
	}

	public int getIncrementBy() {
		return incrementBy;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int getPrecedence() {
		if (incrementBy == -1 || incrementBy == 1) {
			// Can be mapped by prefix unary operator.
			return 2;
		} else {
			return 4; // Binary + -
		}
	}

	@Override
	public String toString() {
		return String.format("(%s += %d)", toIncrement.getName(), incrementBy);
	}

}
