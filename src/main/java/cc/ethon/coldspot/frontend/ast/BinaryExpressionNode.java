package cc.ethon.coldspot.frontend.ast;

public class BinaryExpressionNode extends ExpressionNode {

	private final ExpressionNode left, right;
	private final String operator;

	public BinaryExpressionNode(int instructionIndex, ExpressionNode left, ExpressionNode right, String operator) {
		super(instructionIndex);
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	public ExpressionNode getLeft() {
		return left;
	}

	public ExpressionNode getRight() {
		return right;
	}

	public String getOperator() {
		return operator;
	}

	@Override
	public int getSmallestInstructionIndexWithChildren() {
		return getLeft().getSmallestInstructionIndexWithChildren();
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.accept(this);
	}

	@Override
	public String toString() {
		return "BinaryExpressionNode [left=" + left + ", right=" + right + ", operator=" + operator + "]";
	}

}
