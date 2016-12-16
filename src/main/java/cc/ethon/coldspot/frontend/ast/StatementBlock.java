package cc.ethon.coldspot.frontend.ast;

import java.util.ArrayList;
import java.util.List;

public class StatementBlock extends StatementNode {

	private final List<StatementNode> statements;

	public StatementBlock(int instructionIndex) {
		super(instructionIndex);
		statements = new ArrayList<StatementNode>();
	}

	public List<StatementNode> getStatements() {
		return statements;
	}

	@Override
	public int getSmallestInstructionIndexWithChildren() {
		if (statements.isEmpty()) {
			return instructionIndex;
		}

		int minInstructionIndex = instructionIndex;
		for (final StatementNode statementNode : statements) {
			minInstructionIndex = Math.min(minInstructionIndex, statementNode.getSmallestInstructionIndexWithChildren());
		}
		return minInstructionIndex;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("{\n");
		for (final StatementNode statementNode : statements) {
			b.append("\t");
			b.append(statementNode);
			b.append("\n");
		}
		b.append("}\n");
		return b.toString();
	}

}
