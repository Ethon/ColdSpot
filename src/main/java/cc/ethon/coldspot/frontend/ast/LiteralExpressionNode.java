package cc.ethon.coldspot.frontend.ast;

public class LiteralExpressionNode extends ExpressionNode {

	private final String literal;

	public LiteralExpressionNode(int instructionIndex, String literal) {
		super(instructionIndex);
		this.literal = literal;
	}

	public String getLiteral() {
		return literal;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.accept(this);
	}

}
