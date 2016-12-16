package cc.ethon.coldspot.frontend.ast;

public class DoWhileLoopStatementNode extends StatementNode {

	private final ExpressionNode condition;
	private final StatementBlock body;

	public DoWhileLoopStatementNode(int instructionIndex, ExpressionNode condition, StatementBlock body) {
		super(instructionIndex);
		this.condition = condition;
		this.body = body;
	}

	public ExpressionNode getCondition() {
		return condition;
	}

	public StatementBlock getBody() {
		return body;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
