package cc.ethon.coldspot.frontend.ast;

import java.util.Optional;

public class ReturnStatementNode extends StatementNode {

	private final Optional<ExpressionNode> result;

	public ReturnStatementNode(int instructionIndex, Optional<ExpressionNode> result) {
		super(instructionIndex);
		this.result = result;
	}

	public Optional<ExpressionNode> getResult() {
		return result;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.accept(this);
	}

}
