package cc.ethon.coldspot.frontend;

import java.util.List;

import org.objectweb.asm.Label;

import cc.ethon.coldspot.frontend.ast.ExpressionNode;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;

class BasicBlock {

	public enum LeftBy {
		RETURN, JUMP, CONDITONAL_JUMP
	}

	private final StatementBlock statements;
	private LeftBy leftBy;
	private Label jumpTarget;
	private ExpressionNode jumpCondition;

	public BasicBlock(StatementBlock statements) {
		super();
		this.statements = statements;
	}

	public int getFirstInstructionIndex() {
		if (leftBy == LeftBy.CONDITONAL_JUMP) {
			return Math.min(statements.getSmallestInstructionIndexWithChildren(), jumpCondition.getSmallestInstructionIndexWithChildren());
		}
		return statements.getSmallestInstructionIndexWithChildren();
	}

	public int getLastInstructionIndex() {
		final List<StatementNode> list = statements.getStatements();
		if (leftBy == LeftBy.CONDITONAL_JUMP) {
			return Math.max(list.get(list.size() - 1).getInstructionIndex(), jumpCondition.getInstructionIndex());
		}
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

	public ExpressionNode getJumpCondition() {
		return jumpCondition;
	}

	public void setJumpCondition(ExpressionNode jumpCondition) {
		this.jumpCondition = jumpCondition;
	}

	@Override
	public String toString() {
		return "BasicBlock [statements=" + statements + ", leftBy=" + leftBy + ", jumpTarget=" + jumpTarget + ", jumpCondition=" + jumpCondition + "]";
	}

}
