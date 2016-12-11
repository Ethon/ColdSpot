package cc.ethon.coldspot.frontend.ast;

public class AssignmentStatementNode extends StatementNode {

	private final ExpressionNode left, right;

	public AssignmentStatementNode(int instructionIndex, ExpressionNode left, ExpressionNode right) {
		super(instructionIndex);
		this.left = left;
		this.right = right;
	}

	public ExpressionNode getLeft() {
		return left;
	}

	public ExpressionNode getRight() {
		return right;
	}

	@Override
	public int getSmallestInstructionIndexWithChildren() {
		return Math.min(left.getSmallestInstructionIndexWithChildren(), right.getSmallestInstructionIndexWithChildren());
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.accept(this);
	}

	@Override
	public String toString() {
		return "AssignmentStatementNode [left=" + left + ", right=" + right + "]";
	}

}
