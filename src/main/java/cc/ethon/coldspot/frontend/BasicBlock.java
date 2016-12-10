package cc.ethon.coldspot.frontend;

import java.awt.Label;
import java.util.List;

import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;

class BasicBlock {

	public enum LeftBy {
		RETURN, JUMP
	}

	private final StatementBlock statements;
	private LeftBy leftBy;
	private Label jumpTarget;

	public BasicBlock(StatementBlock statements) {
		super();
		this.statements = statements;
	}

	public int getFirstInstructionIndex() {
		return statements.getSmallestInstructionIndexWithChildren();
	}

	public int getLastInstructionIndex() {
		final List<StatementNode> list = statements.getStatements();
		return list.get(list.size() - 1).getInstructionIndex();
	}

	public boolean containsInstructionIndex(int instructionIndex) {
		return instructionIndex >= getFirstInstructionIndex() && instructionIndex <= getLastInstructionIndex();
	}

	public void injectStatementBefore(StatementNode statement) {
		final int instructionIndex = statement.getInstructionIndex();
		if (!containsInstructionIndex(instructionIndex)) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < statements.getStatements().size(); i++) {
			final StatementNode cur = statements.getStatements().get(i);
			final int curInstructionIndex = cur.getInstructionIndex();
			if (curInstructionIndex >= instructionIndex) {
				statements.getStatements().add(i, statement);
				return;
			}
		}
		throw new IllegalStateException();
	}

	public StatementBlock getStatements() {
		return statements;
	}

	public LeftBy getLeftBy() {
		return leftBy;
	}

	public void setLeftBy(LeftBy leftBy) {
		this.leftBy = leftBy;
	}

	public Label getJumpTarget() {
		return jumpTarget;
	}

	public void setJumpTarget(Label jumpTarget) {
		this.jumpTarget = jumpTarget;
	}

}
