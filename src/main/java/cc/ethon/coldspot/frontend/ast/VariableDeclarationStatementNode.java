package cc.ethon.coldspot.frontend.ast;

import java.util.Optional;

import cc.ethon.coldspot.common.type.Type;

public class VariableDeclarationStatementNode extends StatementNode {

	private Type type;
	private String name;
	private Optional<ExpressionNode> value;

	public VariableDeclarationStatementNode(int instructionIndex) {
		super(instructionIndex);
		value = Optional.empty();
	}

	public void updateInstructionIndex(int instructionIndex) {
		this.instructionIndex = instructionIndex;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Optional<ExpressionNode> getValue() {
		return value;
	}

	public void setValue(Optional<ExpressionNode> value) {
		this.value = value;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.accept(this);
	}

	@Override
	public String toString() {
		if (value.isPresent()) {
			return String.format("%s %s = %s", type.getClass().getSimpleName(), getName(), value.get());
		} else {
			return String.format("%s %s", type.getClass().getSimpleName(), getName());
		}
	}

}
