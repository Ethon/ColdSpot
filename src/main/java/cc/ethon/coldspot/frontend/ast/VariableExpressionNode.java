package cc.ethon.coldspot.frontend.ast;

public class VariableExpressionNode extends ExpressionNode {

	private final VariableDeclarationStatementNode declaration;

	public VariableExpressionNode(int instructionIndex, VariableDeclarationStatementNode declaration) {
		super(instructionIndex);
		this.declaration = declaration;
	}

	public VariableDeclarationStatementNode getDeclaration() {
		return declaration;
	}

	public String getName() {
		return declaration.getName();
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return declaration.getName();
	}

}
