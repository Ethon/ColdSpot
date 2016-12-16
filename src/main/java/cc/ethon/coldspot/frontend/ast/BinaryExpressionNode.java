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
	public int getPrecedence() {
		switch (operator) {
		case "*":
		case "/":
		case "%":
			return 3;

		case "+":
		case "-":
			return 4;

		case "<<":
		case ">>":
		case ">>>":
			return 5;

		case "<":
		case ">":
		case "<=":
		case ">=":
			return 6;

		case "==":
		case "!=":
			return 7;

		case "&":
			return 8;

		case "^":
			return 9;

		case "|":
			return 10;

		case "&&":
			return 11;

		case "||":
			return 12;

		default:
			throw new UnsupportedOperationException("Unsupported operator " + operator);
		}
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return String.format("(%s %s %s)", left, operator, right);
	}

}
