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
		return statements.get(0).getSmallestInstructionIndexWithChildren();
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.accept(this);
	}

	@Override
	public String toString() {
		return "StatementBlock [statements=" + statements + "]";
	}

}
